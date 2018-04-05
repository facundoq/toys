/* CrappyDBMS, a simple relational DBMS written in Java.
    Copyright (C) 2008  Facundo Manuel Quiroga <facundoq@gmail.com>
 	
 	This file is part of CrappyDBMS.

    CrappyDBMS is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    CrappyDBMS is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with CrappyDBMS.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.crappydbms.dbfiles.locking;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.crappydbms.dbfiles.FilePageID;
import org.crappydbms.exceptions.CrappyDBMSError;

/**
 * Represents the locking state of a page.
 * Tries to be fair by making readers wait when trying to acquire the lock and 
 * there is a writer trying to acquire an exclusive lock, or a reader trying to upgrade to an exclusive lock.  
 * Allows the caller to wait for a maximum amount of time for a lock, in which case an exception is thrown,
 *
 * Implementation details:
 * 
 * We say for a thread t and a lock l,, it can happen that:
 * A(t,l) = It holds the lock, if it has acquired and not yet released it
 * B(t,l) = It is waiting for a lock, if it has been put to sleep on a condition variable
 * C(t.l)  = It is about to try to use a lock, when it had been waiting for a lock, and has been signaled or timed out, and hence in the queue for the monitors lock.
 * 
 * In order to avoid livelocks, it can never happen that:
 *  No thread is holding the lock, AND no thread is about to try use a lock, and use AND at least one other thread is waiting to use it.
 * Which gives the invariant:
 * NOT  ( there exists an l such that ( (for all t, not (A(t,l))  AND not (B(t,l))) and ( there exists a t such C(t,l) ) )  
 * 
 *  // remove from here
 * Hence, the PageLock invariant:
 * No thread is using the lock AND no thread is waiting to use the lock 
 * OR 
 * At least one thread is using the lock AND no other thread is waiting to use it
 * OR
 * At least one thread is using the lock, AND at least one other thread is waiting to use it.
 * OR
 * No thread is using the lock AND at least one thread is about to try to use the lock, and
 * OR  
 * In all of the above, a thread may or may not be about to try to use a lock. 
 *  // to here
 * Every time a thread releases a lock, it must restore the invariant. 
 * Also, as an improvement, whenever a writer or upgrader times out, it must, not restore the invariant,but check for unnecessary waiting
 * This is because, for example, it could happen that a reader has the lock, and a writer comes and starts waiting. Another readers comes, and noticing the writer waiting, waits as well.
 * Should the writer timeout while waiting, and leave, the second reader would be waiting on the queue, and would only get in after the reader finishes and restores the invariant, which would be a waste of time.  
 * 
 * Additionally, and also in order to improve throughput and provide some fairness, the lock has the following behavior
 * A thread can only get a shared lock if there aren't any other threads waiting to acquire an exclusive lock (by upgrading or normal acquiring)
 * 
 * 
 * 
 * 
 * @author Facundo Manuel Quiroga Dec 22, 2008
 */
public class PageLock {

	/**
	 * It is an int, but there can only be one writer anyway..
	 */
	protected int writers;
	protected int readers;

	protected int readersWaitingCount;
	protected int writersWaitingCount;
	protected int upgradersWaitingCount;

	protected ReentrantLock lock;

	protected Condition readersWaiting;
	protected Condition writersWaiting;
	protected Condition upgradersWaiting;

	protected FilePageID filePageID;
	
	public PageLock(FilePageID filePageID) {
		this.filePageID = filePageID;
		this.readersWaitingCount = 0;
		this.writersWaitingCount = 0;
		this.upgradersWaitingCount = 0;
		this.readers = 0;
		this.writers = 0;
		this.lock = new ReentrantLock();
		this.readersWaiting = this.lock.newCondition();
		this.writersWaiting = this.lock.newCondition();
		this.upgradersWaiting = this.lock.newCondition();
	}
	
	public PageLock() {
		this(null);
	}

	public int getReaders() {
		return this.readers;
	}

	public int getWriters() {
		return this.writers;
	}
	// TODO remove this, add to documentation
	protected void restoreInvariant() {
		if (readers == 0) {
			if (writers == 0) { // if there are no writers or readers, restore invariant
				if (writersWaitingCount > 0) {
					this.writersWaiting.signal();
				} else if (readersWaitingCount > 0) {
					this.readersWaiting.signal();
				}
			} // if there is a writer, invariant holds 
		} else { // readers > 0, but variant may not hold..
			if (readers == 1 && upgradersWaitingCount == 1) {
				//there is a reader, but it is waiting to upgrade, restore invariant
				upgradersWaiting.signal();
			} else {
				if (readersWaitingCount > 0 && upgradersWaitingCount == 0 && writersWaitingCount == 0) {
					//  
					this.readersWaiting.signal();
				}
			}
		}
	}

	/**
	 * Attempts obtain a shared lock; locks if it can't yet.
	 * 
	 * @throws InterruptedException if the thread is interrupted while waiting for the lock.
	 * @throws ExceededWaitTimeException 
	 */
	public void getSharedLock(long maxWaitTime) throws InterruptedException, ExceededWaitTimeException {
		boolean waitedLessThanWaitTime = true;
		this.lock.lock();
		try {
			readersWaitingCount++;
			try {
				while ((writers > 0 || writersWaitingCount > 0 || upgradersWaitingCount > 0) && waitedLessThanWaitTime) {
					waitedLessThanWaitTime = this.readersWaiting.await(maxWaitTime, TimeUnit.MILLISECONDS);
				}
			} finally {
				readersWaitingCount--;
			}
			if (writers > 0 || writersWaitingCount > 0 || upgradersWaitingCount > 0) {
				throw new ExceededWaitTimeException("Max time to wait for a lock was: " + maxWaitTime);
			}
			readers++;
		} finally {
			this.lock.unlock();
		}
	}

	/**
	 * Attempts obtain an exclusive lock; locks if it can't yet.
	 * @throws InterruptedException 
	 * 
	 * @throws InterruptedException 
	 * @throws ExceededWaitTimeException, Exception 
	 * @throws Exception 
	 */
	public void getExclusiveLock(long maxWaitTime) throws InterruptedException, ExceededWaitTimeException, Exception  {
		boolean waitedLessThanWaitTime = true;
		this.lock.lock();
		try {
			writersWaitingCount++;
			try {
				// the first time give way to other writers
				try {
					if ((readers > 0 || writers > 0 || writersWaitingCount > 1) && waitedLessThanWaitTime) {
						waitedLessThanWaitTime = this.writersWaiting.await(maxWaitTime, TimeUnit.MILLISECONDS);
					}
					// after that, don't, because it would livelock all waiting writers.
					while ((readers > 0 || writers > 0) && waitedLessThanWaitTime) {
						waitedLessThanWaitTime = this.writersWaiting.await(maxWaitTime, TimeUnit.MILLISECONDS);
					}
				} catch (InterruptedException e) {
					this.restoreInvariantWriterCancelled();
					throw e;
				} catch (Exception e) {
					this.restoreInvariantWriterCancelled();
					throw e;
				}
			} finally {
				writersWaitingCount--;
			}

			if (readers > 0 || writers > 0) {
				this.restoreInvariantWriterCancelled();
				throw new ExceededWaitTimeException("Max time to wait was: " + maxWaitTime);
			}

			writers++;
		} finally {
			this.lock.unlock();
		}
	}

	private void restoreInvariantWriterCancelled() {
		if (this.readersWaitingCount > 0 && this.writersWaitingCount == 0 && this.upgradersWaitingCount == 0) {
			this.readersWaiting.signalAll();
		}
	}

	/**
	 * Attempts to upgrade a shared lock to an exclusive lock; locks if it can't yet. Assumes a shared lock was already granted. CAREFUL; if two threads request an upgrade on the same lock, both'll
	 * get deadlocked
	 * 
	 * @throws InterruptedException if the thread is interrupted while waiting for the lock.
	 * @throws UpgradeWouldCauseDeadlockException if another thread was already waiting to upgrade to an exclusive lock
	 * @throws ExceededWaitTimeException if the wait time was exceeded
	 */
	public void upgradeToExclusiveLock(long maxWaitTime) throws InterruptedException, UpgradeWouldCauseDeadlockException,
			ExceededWaitTimeException, Exception {
		this.lock.lock();
		boolean waitedLessThanWaitTime = true;
		try {
			if (upgradersWaitingCount > 0) {
				throw new UpgradeWouldCauseDeadlockException();
			}
			upgradersWaitingCount = 1;
			try {
				while (readers > 1 && waitedLessThanWaitTime) {
					waitedLessThanWaitTime = this.upgradersWaiting.await(maxWaitTime, TimeUnit.MILLISECONDS);
				}
			} catch (InterruptedException e) {
				this.restoreInvariantUpgraderCancelled();
				throw e;
			} catch (Exception e) {
				this.restoreInvariantUpgraderCancelled();
				throw e;
			} finally {
				upgradersWaitingCount = 0;
			}
			if (readers > 1) {
				this.restoreInvariantUpgraderCancelled();
				throw new ExceededWaitTimeException("Max time to wait was: " + maxWaitTime);
			}
			readers--;
			writers++;
		} finally {
			this.lock.unlock();
		}
	}

	private void restoreInvariantUpgraderCancelled() {
		if (this.readersWaitingCount > 0 && this.writersWaitingCount == 0) {
			this.readersWaiting.signalAll();
		}
		// if there were any writers waiting, readers >0, so the last reader will wake up one writer
	}

	/**
	 * Attempts to downgrade an exclusive lock to a shared lock; does not lock. Assumes a shared lock was already granted.
	 */
	public void downgradeToSharedLock() {
		this.lock.lock();
		try {
			if (readers != 0  || writers != 1 ){
				this.throwError("There should be no readers("+readers+"), and just one writer ("+writers+")");
			}
			writers--;
			readers++;
			if (readersWaitingCount > 0) {
				readersWaiting.signalAll();
			}
		} finally {
			this.lock.unlock();
		}
	}

	/**
	 * Tries to acquire an exclusive lock; if it can't do so immediately, <b> it does not lock waiting for all readers to get out </b>, and instead returns false
	 * 
	 * @return true if the lock was acquired, false if it could not be acquired.
	 */
	public boolean tryExclusiveLock() {
		boolean result;
		this.lock.lock();
		try {
			if (writers == 0 && readers == 0) {
				writers++;
				result = true;
			} else {
				result = false;
			}

		} finally {
			this.lock.unlock();
		}
		return result;
	}

	/**
	 * Tries to upgrade to an exclusive lock; if it can't do so immediately, <b> it does not lock waiting for all readers to get out </b>, and instead returns false
	 * Assumes the caller already has a shared lock
	 * @return true if the lock was upgraded, false if it could not be upgraded.
	 */
	public boolean tryUpgradeLock() {
		this.lock.lock();
		try {
			if (readers == 0) {
				this.throwError("There should already be a reader (the same thread) if it is trying to upgrade");
			}
			if (writers == 0 && readers == 1) {
				readers--;
				writers++;
				return true;
			} else {
				return false;
			}
		} finally {
			this.lock.unlock();
		}
	}

	/**
	 * Releases a shared lock. Assumes a shared lock was obtained previously. Does not lock.
	 */
	public void releaseSharedLock() {
		this.lock.lock();
		try {
			if (readers == 0  || writers >0 ){
				this.throwError("There should be at least one reader("+readers+"), and no writers ("+writers+")");
			}
			readers--;
			//wake up a waiting writer if there are no more readers  
			if (readers == 0 && writersWaitingCount > 0) {
				writersWaiting.signal();
				// wake up an upgrader if there is just one reader
			} else if (readers == 1 && upgradersWaitingCount > 0) {
				upgradersWaiting.signal();
				// wake up readers if it happens that some writers/upgraders timed out when waiting for the lock and readers got stuck
			} else if (readersWaitingCount > 0 && upgradersWaitingCount == 0 && writersWaitingCount == 0) {
				readersWaiting.signalAll();
			}
		} finally {
			this.lock.unlock();
		}
	}

	/**
	 * Releases an exclusive lock. Assumes an exclusive lock was obtained previously. Does not lock.
	 * Gives preference to waiting writers
	 */
	public void releaseExclusiveLock() {
		this.lock.lock();
		try {
			if (readers != 0  || writers != 1 ){
				this.throwError("There should be no readers("+readers+"), and just one writer ("+writers+")");
			}
			writers--;
			//wake up a writer if it is waiting; if not, wake up all the readers that are waiting, if any 
			if (writersWaitingCount > 0) {
				writersWaiting.signal();
			} else if (readersWaitingCount > 0) {
				readersWaiting.signalAll();
			}
		} finally {
			this.lock.unlock();
		}
	}

	public String toString() {
		return "PageLock writers: " + this.getWriters() + " readers: " + this.getReaders();
	}

	public int getReadersWaitingCount() {
		return this.readersWaitingCount;
	}

	public int getWritersWaitingCount() {
		return this.writersWaitingCount;
	}

	public int getUpgradersWaitingCount() {
		return this.upgradersWaitingCount;
	}
	
	protected void throwError(String reason){
		throw new CrappyDBMSError(reason+ " PL on:"+this.filePageID);
	}
/*
	public void getExclusiveLock() throws InterruptedException {
		this.lock.lock();
		try {
			writersWaitingCount++;
			if (readers > 0 || writers > 0 || writersWaitingCount > 1) {
				this.writersWaiting.await();
			}
			writersWaitingCount--;
			writers++;
		} finally {
			this.lock.unlock();
		}
	}

	public void getSharedLock() throws InterruptedException {
		this.lock.lock();
		try {
			readersWaitingCount++;
			while (writers > 0 || writersWaitingCount > 0 || upgradersWaitingCount > 0) {
				this.readersWaiting.await();
			}
			readersWaitingCount--;
			readers++;
		} finally {
			this.lock.unlock();
		}
	}

	public void upgradeToExclusiveLock() throws InterruptedException, UpgradeWouldCauseDeadlockException {
		this.lock.lock();
		try {
			if (upgradersWaitingCount > 0) {
				throw new UpgradeWouldCauseDeadlockException();
			}
			upgradersWaitingCount = 1;
			while (readers > 1) {
				this.upgradersWaiting.await();
			}
			readers--;
			upgradersWaitingCount = 0;
			writers++;
		} finally {
			this.lock.unlock();
		}
	}*/

}
