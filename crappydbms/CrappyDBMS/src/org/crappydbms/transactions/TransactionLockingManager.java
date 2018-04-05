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
package org.crappydbms.transactions;

import java.util.HashMap;
import java.util.Iterator;

import org.crappydbms.dbfiles.FilePageID;
import org.crappydbms.dbfiles.locking.ExceededWaitTimeException;
import org.crappydbms.dbfiles.locking.PageLock;
import org.crappydbms.dbfiles.locking.UpgradeWouldCauseDeadlockException;
import org.crappydbms.exceptions.CrappyDBMSError;

/**
 * @author Facundo Manuel Quiroga Jan 14, 2009
 */
public class TransactionLockingManager {

	protected HashMap<FilePageID, PageLock> sharedLocks;
	protected HashMap<FilePageID, PageLock> exclusiveLocks;

	protected String lockingStatus;
	protected long startedWaitingForLockAt;

	protected TransactionLockingStatistics transactionLockingStatistics;
	protected BaseTransaction baseTransaction;

	public TransactionLockingManager(BaseTransaction baseTransaction) {
		this.setSharedLocks(new HashMap<FilePageID, PageLock>());
		this.setExclusiveLocks(new HashMap<FilePageID, PageLock>());
		this.setLockingStatus("not waiting for lock");
		this.setStartedWaitingForLockAt(-1);
		this.setBaseTransaction(baseTransaction);
		this.setTransactionLockingStatistics(new TransactionLockingStatistics());
	}

	/**
	 * May lock
	 */
	public void lockShared(FilePageID filePageID) throws TransactionAbortedException {
		// Don't lock the page if it is internal to this transaction - nobody can see it, there is no need to lock
		long maxWaitTime  = -1;
		if (!filePageIsInternal(filePageID)) {
			try {
				PageLock pageLock = this.getExclusiveLocks().get(filePageID);
				// check if transaction already has an exclusive lock on page
				if (pageLock == null) {
					// if not check if it has a shared lock
					pageLock = this.getSharedLocks().get(filePageID);
					if (pageLock == null) {
						// it doesn't have any locks, get a new one
						pageLock = filePageID.getFile().getDBFileLockManager().getPageLockFor(filePageID);
						this.setLockingStatus("waiting for shared lock on " + filePageID);
						maxWaitTime = this.getTransactionLockingStatistics().calculateMaxWaitTimeForSharedLock();
						this.getTransactionLockingStatistics().lockingShared();
						pageLock.getSharedLock(maxWaitTime);
						this.getTransactionLockingStatistics().lockedShared();
						this.getSharedLocks().put(filePageID, pageLock);
					}
				}
			} catch (InterruptedException e) {
				this.getBaseTransaction().abort("Thread interrupted while acquiring a shared lock on "+filePageID);
			} catch (ExceededWaitTimeException e) {
				this.getBaseTransaction().abort("Thread exceeded max waiting time, "+maxWaitTime+" milliseconds waiting for shared lock on"+filePageID);
			} catch (Exception e) {
				this.getBaseTransaction().abort("Unknown error : "+e.getMessage()+" when acquiring shared lock  on "+filePageID);
			} finally {
				this.setLockingStatus("not waiting for lock");
			}
		}
	}

	/**
	 * May lock
	 */
	public void lockExclusive(FilePageID filePageID) throws TransactionAbortedException {
		// Don't lock the page if it is internal to this transaction - nobody can see it, there is no need ot lock
		long maxWaitTime  = -1;
		if (!filePageIsInternal(filePageID)) {
			try {
				PageLock pageLock = this.getExclusiveLocks().get(filePageID);
				// check if transaction already has an exclusive lock on page
				if (pageLock == null) {
					// if not check if it has a shared lock
					pageLock = this.getSharedLocks().get(filePageID);
					if (pageLock != null) {
						this.setLockingStatus("upgrading to exclusive lock on " + filePageID);
						this.getTransactionLockingStatistics().upgradingToExclusiveLock();
						maxWaitTime = this.getTransactionLockingStatistics().calculateMaxWaitTimeForUpgradingLock();
						pageLock.upgradeToExclusiveLock(maxWaitTime);
						this.getTransactionLockingStatistics().upgradedToExclusiveLock();
						this.getSharedLocks().remove(filePageID);
					} else {
						// it doesn't have any locks, get a new one
						pageLock = filePageID.getFile().getDBFileLockManager().getPageLockFor(filePageID);
						this.setLockingStatus("waiting for exclusive lock on " + filePageID);
						this.getTransactionLockingStatistics().lockingExclusive();
						maxWaitTime = this.getTransactionLockingStatistics().calculateMaxWaitTimeForExclusiveLock();
						pageLock.getExclusiveLock(maxWaitTime);
						this.getTransactionLockingStatistics().lockedExclusive();
					}
					this.getExclusiveLocks().put(filePageID, pageLock);
				}
			} catch (UpgradeWouldCauseDeadlockException e) {
				this.getBaseTransaction().abort("Upgrading to an exclusive lock would cause deadlock");
			} catch (InterruptedException e) {
				this.getBaseTransaction().abort("Thread interrupted while acquiring a exclusive lock on "+filePageID);
			} catch (ExceededWaitTimeException e) {
				this.getBaseTransaction().abort("Thread exceeded max waiting time, "+maxWaitTime+" milliseconds waiting for exclusive lock on"+filePageID);
			} catch (Exception e) {
				this.getBaseTransaction().abort("Unknown error : "+e.getMessage()+" when acquiring exclusive lock  on "+filePageID);
			} finally {
				this.setLockingStatus("not waiting for lock");
			}
		}
	}

	public void lockPage(FilePageID filePageID, LockMode lockMode) throws TransactionAbortedException {
		// Don't lock the page if it is internal to this transaction - nobody can see it, there is no need ot lock
		if (!filePageIsInternal(filePageID)) {
			if (lockMode.equals(LockMode.shared())) {
				this.lockShared(filePageID);
			} else if (lockMode.equals(LockMode.exclusive())) {
				this.lockExclusive(filePageID);
			} else {
				throw new CrappyDBMSError("Invalid LockMode");
			}
		}
	}

	protected boolean filePageIsInternal(FilePageID filePageID) {
		return filePageID.getPageNumber() == -1;
	}

	public boolean doTryLockExclusive(FilePageID filePageID) {
		// Don't lock the page if it is internal to this transaction - nobody can see it, there is no need to lock
		if (filePageIsInternal(filePageID)) {
			return true;
		}
		if (this.hasExclusiveLockOn(filePageID) || this.hasSharedLockOn(filePageID)){
			throw new CrappyDBMSError("Caller should check this transaction does not already have an exclusive or shared lock");
		}
		PageLock pageLock = filePageID.getFile().getDBFileLockManager().getPageLockFor(filePageID);
		if (pageLock.tryExclusiveLock()) {
			this.getExclusiveLocks().put(filePageID, pageLock);
			return true;
		} else {
			return false;
		}

	}

	public boolean doTryUpgradeLock(FilePageID filePageID) {
		// Don't lock the page if it is internal to this transaction - nobody can see it, there is no need to lock
		if (filePageIsInternal(filePageID)) {
			return true;
		}
		if (this.hasExclusiveLockOn(filePageID) || !this.hasSharedLockOn(filePageID)){
			throw new CrappyDBMSError("Caller should check this transaction does not already have an exclusive, and has a shared lock");
		}
		PageLock pageLock = this.getSharedLocks().get(filePageID);
		if (pageLock.tryUpgradeLock()) {
			this.getSharedLocks().remove(filePageID);
			this.getExclusiveLocks().put(filePageID, pageLock);
			return true;
		} else {
			return false;
		}

	}

	public void downgradeToShared(FilePageID filePageID) {
		if (!filePageIsInternal(filePageID)) {
			if (!this.hasExclusiveLockOn(filePageID) || this.hasSharedLockOn(filePageID)){
				throw new CrappyDBMSError("Caller should check this transaction has an exclusive lock and does not have a shared lock");
			}
			PageLock pageLock = this.getExclusiveLocks().get(filePageID);
			pageLock.downgradeToSharedLock();
			this.getExclusiveLocks().remove(filePageID);
			this.getSharedLocks().put(filePageID, pageLock);
		}
	}

	public boolean hasExclusiveLockOn(FilePageID filePageID) {
		if (filePageIsInternal(filePageID)) {
			return true;
		}
		return this.getExclusiveLocks().get(filePageID) != null;
	}

	public boolean hasSharedLockOn(FilePageID filePageID) {
		if (filePageIsInternal(filePageID)) {
			return true;
		}
		return this.getSharedLocks().get(filePageID) != null;
	}

	public void releaseExclusiveLock(FilePageID filePageID) {
		if (!filePageIsInternal(filePageID)) {
			if (!this.hasExclusiveLockOn(filePageID) || this.hasSharedLockOn(filePageID)){
				throw new CrappyDBMSError("Caller should check this transaction already has an exclusive lock and does not have a shared lock");
			}
			PageLock pageLock = this.getExclusiveLocks().get(filePageID);
			pageLock.releaseExclusiveLock();
			this.getExclusiveLocks().remove(filePageID);
		}

	}

	public void releaseSharedLock(FilePageID filePageID) {
		if (!filePageIsInternal(filePageID)) {
			if (this.hasExclusiveLockOn(filePageID) || !this.hasSharedLockOn(filePageID)){
				throw new CrappyDBMSError("Caller should check this transaction already has a shared lock and does not have an exclusive lock");
			}
			PageLock pageLock = this.getSharedLocks().get(filePageID);
			pageLock.releaseSharedLock();
			this.getSharedLocks().remove(filePageID);
		}

	}

	protected HashMap<FilePageID, PageLock> getSharedLocks() {
		return this.sharedLocks;
	}

	protected void setSharedLocks(HashMap<FilePageID, PageLock> sharedLocks) {
		this.sharedLocks = sharedLocks;
	}

	public HashMap<FilePageID, PageLock> getExclusiveLocks() {
		return this.exclusiveLocks;
	}

	protected void setExclusiveLocks(HashMap<FilePageID, PageLock> exclusiveLocks) {
		this.exclusiveLocks = exclusiveLocks;
	}

	protected long getStartedWaitingForLockAt() {
		return this.startedWaitingForLockAt;
	}

	protected void setStartedWaitingForLockAt(long startedWaitingForLockAt) {
		this.startedWaitingForLockAt = startedWaitingForLockAt;
	}

	public String getLockingStatus() {
		return this.lockingStatus;
	}

	protected void setLockingStatus(String lockingStatus) {
		this.lockingStatus = lockingStatus;
	}

	protected BaseTransaction getBaseTransaction() {
		return this.baseTransaction;
	}

	protected void setBaseTransaction(BaseTransaction baseTransaction) {
		this.baseTransaction = baseTransaction;
	}

	public void releaseSharedLocks() {
		HashMap<FilePageID, PageLock> sharedLocks = this.getSharedLocks();
		Iterator<FilePageID> sharedLocksIterator = sharedLocks.keySet().iterator();
		while (sharedLocksIterator.hasNext()) {
			FilePageID filePageID = sharedLocksIterator.next();
			PageLock pageLock = sharedLocks.get(filePageID);
			pageLock.releaseSharedLock();
			sharedLocksIterator.remove();
		}
		assert(sharedLocks.isEmpty());
	}

	public void releaseExclusiveLocks() {
		HashMap<FilePageID, PageLock> exclusiveLocks = this.getExclusiveLocks();
		Iterator<FilePageID> exclusiveLocksIterator = exclusiveLocks.keySet().iterator();
		while (exclusiveLocksIterator.hasNext()) {
			FilePageID filePageID = exclusiveLocksIterator.next();
			PageLock pageLock = exclusiveLocks.get(filePageID);
			pageLock.releaseExclusiveLock();
			exclusiveLocksIterator.remove();
		}
		assert(exclusiveLocks.isEmpty());
	}

	protected TransactionLockingStatistics getTransactionLockingStatistics() {
		return this.transactionLockingStatistics;
	}

	protected void setTransactionLockingStatistics(TransactionLockingStatistics transactionLockingStatistics) {
		this.transactionLockingStatistics = transactionLockingStatistics;
	}

}
