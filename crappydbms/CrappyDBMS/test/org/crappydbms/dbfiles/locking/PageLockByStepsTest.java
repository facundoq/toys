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

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * @author Facundo Manuel Quiroga Jan 15, 2009
 */
public class PageLockByStepsTest extends TestCase {


	public void testReaderUpgraderReader() {
		int numberOfTests = 500;
		ArrayList<TestContext> testContexts = new ArrayList<TestContext>();
		
		for (int i = 0; i < numberOfTests; i++) {
			testContexts.add(new TestContext(3));
		}
		for (int i = 0; i < numberOfTests; i++) {
			TestContext testContext = testContexts.get(i);
			new Thread(new Reader(testContext,  i*0, 0, 3,false)).start();
			new Thread(new Upgrader(testContext, i* 1, 1, 2, 5, false,false)).start();
			new Thread(new Reader(testContext, i*2, 4, 6,false)).start();
			testContext.checkErrors();
			//System.out.println(i);
		}
/*
		for (int i = 0; i < numberOfTests; i++) {
			testContexts.get(i).checkErrors();
		}*/
	}
	
	
	public class TestContext {
		protected int interrupted;
		protected volatile int numberOfTasks;
		protected int deadlocked;
		protected volatile int finished;
		protected int otherError;
		protected volatile int step;
		protected PageLock pageLock;
		
		public TestContext(int numberOfTasks){
			this.numberOfTasks = numberOfTasks;
			this.step = 0;
			this.interrupted = 0;
			this.deadlocked = 0;
			this.finished = 0;
			this.otherError = 0;
			this.pageLock = new PageLock();
		}
		
		public void checkErrors() {
			while (numberOfTasks<this.finished){
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			Assert.assertEquals("No tasks should have errors", 0, interrupted);
			Assert.assertEquals("No tasks should have errors", 0, deadlocked);
			Assert.assertEquals("No tasks should have errors", 0, otherError);
		}

		public synchronized void interrupted() {
			interrupted++;
		}

		public synchronized void deadlocked() {
			deadlocked++;
		}

		public synchronized void finished() {
			finished++;
		}

		public synchronized void otherError() {
			otherError++;
		}

		public int getErrors() {
			return interrupted + deadlocked + otherError;
		}

	}

	public abstract class MockPageLockUser implements Runnable {
		protected PageLock pageLock;
		protected int id;
		TestContext testContext;
		boolean  shouldExceedTimeOut;
		
		public MockPageLockUser( int id, TestContext testContext, boolean  shouldExceedTimeOut) {
			this.pageLock = testContext.pageLock;
			this.id = id;
			this.testContext = testContext;
			this.shouldExceedTimeOut = shouldExceedTimeOut;
		}
	}

	public class Reader extends MockPageLockUser {
		protected int getSharedStep, releaseStep;

		public Reader(TestContext testContext, int id, int getSharedStep, int releaseStep, boolean  shouldExceedTimeOut) {
			super( id, testContext,shouldExceedTimeOut);
			this.getSharedStep = getSharedStep;
			this.releaseStep = releaseStep;
		}

		@Override
		public void run() {
			try {
				while (this.testContext.step != this.getSharedStep) {
					Thread.sleep(200);
				}
				this.pageLock.getSharedLock(30000);
				while (this.testContext.step != this.releaseStep) {
					Thread.sleep(200);
				}
				this.pageLock.releaseExclusiveLock();

			} catch (InterruptedException e) {
				this.testContext.interrupted();
			} catch (ExceededWaitTimeException e) {
				this.testContext.otherError();
				e.printStackTrace();
			} catch (Exception e) {
				this.testContext.otherError();
				e.printStackTrace();
			} finally {
				this.testContext.finished();
			}
		}
	}

	public class Writer extends MockPageLockUser {
		protected int getExclusiveStep, releaseStep;

		public Writer(TestContext testContext,  int id, int getExclusiveStep, int releaseStep, boolean  shouldExceedTimeOut) {
			super( id, testContext,shouldExceedTimeOut);
			this.getExclusiveStep = getExclusiveStep;
			this.releaseStep = releaseStep;
		}

		@Override
		public void run() {
			try {
				while (this.testContext.step != this.getExclusiveStep) {
					Thread.sleep(200);
				}
				this.pageLock.getExclusiveLock(3000);
				while (this.testContext.step != this.releaseStep) {
					Thread.sleep(200);
				}
				this.pageLock.releaseExclusiveLock();

			} catch (InterruptedException e) {
				this.testContext.interrupted();
			} catch (ExceededWaitTimeException e) {
				this.testContext.otherError();
				e.printStackTrace();
			} catch (Exception e) {
				this.testContext.otherError();
				e.printStackTrace();
			} finally {
				this.testContext.finished();
			}
		}
	}

	public class Upgrader extends MockPageLockUser {

		protected int getSharedStep, upgradeStep, releaseStep;
		protected boolean willDeadlock;

		public Upgrader(TestContext testContext,  int id, int getSharedStep, int upgradeStep, int releaseStep, boolean willDeadlock, boolean  shouldExceedTimeOut) {
			super( id, testContext,shouldExceedTimeOut);
			this.getSharedStep = getSharedStep;
			this.upgradeStep = upgradeStep;
			this.releaseStep = releaseStep;
			this.willDeadlock = willDeadlock;

		}

		@Override
		public void run() {
			try {
				while (this.testContext.step != this.getSharedStep) {
					Thread.sleep(200);
				}
				this.pageLock.getSharedLock(30000);
				while (this.testContext.step != this.upgradeStep) {
					Thread.sleep(200);
				}
				this.pageLock.upgradeToExclusiveLock(30000);
				while (this.testContext.step != this.releaseStep) {
					Thread.sleep(200);
				}
				this.pageLock.releaseExclusiveLock();

			} catch (InterruptedException e) {
				this.testContext.interrupted();
			} catch (UpgradeWouldCauseDeadlockException e) {
				if (!willDeadlock) {
					this.testContext.deadlocked();
				}
			} catch (ExceededWaitTimeException e) {
				this.testContext.otherError();
				e.printStackTrace();
			} catch (Exception e) {
				this.testContext.otherError();
				e.printStackTrace();
			} finally {
				this.testContext.finished();
			}
		}
	}

	public class Downgrader extends MockPageLockUser {

		protected int getExclusiveStep, downgradeStep, releaseStep;

		public Downgrader(TestContext testContext,  int id, int getExclusiveStep, int downgradeStep, int releaseStep, boolean shouldExceedWaitTime) {
			super( id, testContext, shouldExceedWaitTime);
			this.downgradeStep = downgradeStep;
			this.getExclusiveStep = getExclusiveStep;
			this.releaseStep = releaseStep;
		}

		@Override
		public void run() {
			try {
				while (this.testContext.step != this.getExclusiveStep) {
					Thread.sleep(200);
				}
				this.pageLock.getExclusiveLock(30000);
				while (this.testContext.step != this.downgradeStep) {
					Thread.sleep(200);
				}
				this.pageLock.downgradeToSharedLock();
				while (this.testContext.step != this.releaseStep) {
					Thread.sleep(200);
				}
				this.pageLock.releaseSharedLock();

			} catch (InterruptedException e) {
				this.testContext.interrupted();
			} catch (ExceededWaitTimeException e) {
				this.testContext.otherError();
				e.printStackTrace();
			} catch (Exception e) {
				this.testContext.otherError();
				e.printStackTrace();
			} finally {
				this.testContext.finished();
			}
		}
	}

}
