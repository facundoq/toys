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

import java.util.ArrayList;
import java.util.Iterator;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * @author Facundo Manuel Quiroga Dec 23, 2008
 * 
 */
public class PageLockTest extends TestCase {
	static int waitTime = 5;

	static int value;

	static int finishedThreads;

	public static synchronized void finished() {
		finishedThreads++;
	}
	static int failed;
	public static synchronized void failed(){
		failed++;
	}
	
	volatile PageLock generalPageLock;

	protected void setUp() throws Exception {
		super.setUp();
		generalPageLock = new PageLock();
		PageLockTest.value = 0;
		PageLockTest.failed = 0;
		PageLockTest.finishedThreads = 0;
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testLockReaders() {
		this.testLock(100, 0, 0, false);
	}

	public void testLockWriters() {
		this.testLock(0, 100, 0, false);
	}

	public void testLockReadersWriters() {
		this.testLock(1, 1, 1, false);
	}

	public void testLockReadersWriters2() {
		this.testLock(100, 100, 1, false);
	}
	public void testLockReadersWriters3() {
		// testing for  deadlock
		this.testLock(0, 0, 4, true);
	}
	public void testLockReadersAndWriters() {
		this.testLock(100, 100, 0, false);
	}



	public void testLock(int readersCount, int writersCount, int readerWritersCount, boolean expectingDeadlock) {
		int finalvalue = writersCount + readerWritersCount;
		int totalThreads = writersCount + readerWritersCount + readersCount;
		Assert.assertEquals(0, generalPageLock.getWriters());
		Assert.assertEquals(0, generalPageLock.getReaders());
		// create readers and writers
		ArrayList<Reader> readers = new ArrayList<Reader>();
		ArrayList<Writer> writers = new ArrayList<Writer>();
		ArrayList<ReaderWriter> readerWriters = new ArrayList<ReaderWriter>();
		for (int i = 0; i < readersCount; i++) {
			readers.add(new Reader(generalPageLock, i));
		}
		for (int i = 0; i < writersCount; i++) {
			writers.add(new Writer(generalPageLock, i));
		}
		for (int i = 0; i < readerWritersCount; i++) {
			readerWriters.add(new ReaderWriter(generalPageLock, i, expectingDeadlock));
		}
		// System.out.println("created");
		// Start them mixed up
		Iterator<Reader> readersIterator = readers.iterator();
		Iterator<ReaderWriter> readerWritersIterator = readerWriters.iterator();
		Iterator<Writer> writersIterator = writers.iterator();
		int entered = 0;
		for (int i = 0; entered < totalThreads; i++) {
			if (i % 3 == 0) {
				if (readersIterator.hasNext()) {
					new Thread(readersIterator.next()).start();
					entered++;
				}
			} else if (i % 3 == 1) {
				if (writersIterator.hasNext()) {
					new Thread(writersIterator.next()).start();
					entered++;
				}
			} else {
				if (readerWritersIterator.hasNext()) {
					new Thread(readerWritersIterator.next()).start();
					entered++;
				}
			}

		}
		// System.out.println("started");
		// wait till they finish
		while (PageLockTest.finishedThreads < totalThreads) {
			try {
				Thread.sleep(1000);
			//	System.out.println("Finished "+PageLockTest.finishedThreads+" of "+totalThreads);
			} catch (InterruptedException e) {
				Assert.fail("should not be interrupted");
			}
		}
		// check that the value holds
		Assert.assertEquals("wrong final value!", finalvalue, PageLockTest.value+PageLockTest.failed);
		Assert.assertEquals(0, generalPageLock.getWriters());
		Assert.assertEquals(0, generalPageLock.getReaders());
	//	 System.out.println("Finished "+PageLockTest.finishedThreads+" of "+totalThreads);
		// System.out.println("expected: "+finalvalue+" got: "+PageLockTest.value);
	}

	public abstract class MockPageLockUser implements Runnable {
		protected PageLock pageLock;
		protected int id;

		public MockPageLockUser(PageLock pageLock, int id) {
			this.pageLock = pageLock;
			this.id = id;
		}
	}

	public class Reader extends MockPageLockUser {

		public Reader(PageLock pageLock, int id) {
			super(pageLock, id);
		}

		@Override
		public void run() {
			int value;
			try {
				 //System.out.println("reader "+id+" getting in");
				this.pageLock.getSharedLock(30000);
				//System.out.println("reader"+id+" in");
				value = PageLockTest.value;
				Thread.sleep(PageLockTest.waitTime);
				Assert.assertEquals("Reader " + id + ": value should not change while im holding the lock", value, PageLockTest.value);
				//System.out.println("reader "+id+"getting out");
				this.pageLock.releaseSharedLock();
				 //System.out.println("reader "+id+" out");
			} catch (InterruptedException e) {
				Assert.fail("should not be interrupted");
			} catch (ExceededWaitTimeException e) {
				Assert.fail("should not exceed wait time"+e);
			} catch (Exception e) {
				Assert.fail("unknown error "+e.getMessage());	
			}
			PageLockTest.finished();
		}
	}

	public class Writer extends MockPageLockUser {

		public Writer(PageLock pageLock, int id) {
			super(pageLock, id);
		}

		@Override
		public void run() {
			try {
				int value;
				try {
					// System.out.println("writer"+id+" getting in");
					this.pageLock.getExclusiveLock(30000);
					// System.out.println("writer"+id+" in");
					PageLockTest.value++;
					value = PageLockTest.value;
					Thread.sleep(PageLockTest.waitTime);
					Assert.assertEquals("Value should not change while im holding the lock", value, PageLockTest.value);
					// System.out.println("writer"+id+"getting out");
					this.pageLock.releaseExclusiveLock();
					// System.out.println("writer "+id+" out");
				} catch (InterruptedException e) {
					Assert.fail("should not be interrupted");
				} catch (ExceededWaitTimeException e) {
					Assert.fail("should not exceed wait time"+e);
				} catch (Exception e) {
					Assert.fail("unknown error "+e.getMessage());
				}
			} finally {
				PageLockTest.finished();
			}
		}
	}

	public class ReaderWriter extends MockPageLockUser {

		protected boolean expectingDeadlock;

		public ReaderWriter(PageLock pageLock, int id, boolean expectingDeadlock) {
			super(pageLock, id);
			this.expectingDeadlock = expectingDeadlock;
		}

		@Override
		public void run() {
			try {
				int value;
				try {
					//System.out.println("readerwriter " + id + " getting in");
					this.pageLock.getSharedLock(30000);
					//System.out.println("readerwriter " + id + " in");
					value = PageLockTest.value;
					Thread.sleep(300);
					Assert.assertEquals("Value should not change while im holding a shared lock", value, PageLockTest.value);
					//System.out.println("readerwriter " + id + " upgrading");
					this.pageLock.upgradeToExclusiveLock(30000);
					//System.out.println("readerwriter " + id + " upgraded");
					Assert.assertEquals("Value should not change while im holding the lock", value, PageLockTest.value);
					PageLockTest.value++;
					value = PageLockTest.value;
					Thread.sleep(PageLockTest.waitTime);
					Assert.assertEquals("Value should not change while im holding the lock", value, PageLockTest.value);
					//System.out.println("readerwriter " + id + " releasing");
					this.pageLock.releaseExclusiveLock();
					//System.out.println("readerwriter " + id + " released");
				} catch (InterruptedException e) {
					Assert.fail("should not be interrupted");
					PageLockTest.failed();
				} catch (UpgradeWouldCauseDeadlockException e) {
					if (!this.expectingDeadlock) {
						Assert.fail("Deadlock occurred in ReaderWriter id " + id);
					}else{
						this.pageLock.releaseSharedLock();
						PageLockTest.failed();
					}
				}
			} catch (ExceededWaitTimeException e) {
				Assert.fail("should not exceed wait time"+e);
			} catch (Exception e) {
				Assert.fail("unknown error "+e.getMessage());
			} finally {
				PageLockTest.finished();
			}
		}
	}
}
