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

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * @author Facundo Manuel Quiroga
 * Jan 19, 2009
 * 
 */
public class PageLockTimeOutTest extends TestCase {
	static volatile PageLock pageLock;

	static int finished;

	static synchronized void finished() {
		finished++;
	}

	static int failed;

	static synchronized void failed() {
		failed++;
	}

	static volatile int step;

	public void setUp() {
		finished = 0;
		failed = 0;
		step = 0;
		pageLock = new PageLock();
	}

	public void testReaderTimedoutWriter() {

		// first reader
		new Thread() {
			public void run() {
				try {
					pageLock.getSharedLock(30000);
					step++; //step == 1
					while (step != 2) {
						Thread.sleep(200);
					}
					pageLock.releaseSharedLock();
				} catch (InterruptedException e) {
					failed();
				} catch (ExceededWaitTimeException e) {
					failed();
				}

				finished();
			}
		}.start();
		// writer that will timeout
		new Thread() {
			public void run() {
				try {
					while (step != 1) {
						Thread.sleep(20);
					}
					pageLock.getExclusiveLock(300);
					failed();
				} catch (InterruptedException e) {
					failed();
				} catch (ExceededWaitTimeException e) {

				} catch (Exception e) {
					failed();
				}
				step++; // step == 2
				finished();
			}
		}.start();

		while (finished < 2) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				Assert.fail("interrupted exception");
			}
		}
		Assert.assertEquals("A thread failed", 0, failed);
		Assert.assertEquals("Incorrect number  of finished threads", 2, finished);
	}

	public void testReaderTimedoutUpgrader() {

		// first reader
		new Thread() {
			public void run() {
				try {
					pageLock.getSharedLock(30000);
					step++; //step == 1
					while (step != 2) {
						Thread.sleep(200);
					}
					pageLock.releaseSharedLock();
				} catch (InterruptedException e) {
					failed();
				} catch (ExceededWaitTimeException e) {
					failed();
				} finally {
					finished();
				}

			}
		}.start();
		// upgrader that will timeout
		new Thread() {
			public void run() {
				try {
					while (step != 1) {
						Thread.sleep(20);
					}
					pageLock.getSharedLock(30000);
					pageLock.upgradeToExclusiveLock(300);
					failed();
				} catch (InterruptedException e) {
					failed();
				} catch (ExceededWaitTimeException e) {

				} catch (Exception e) {
					failed();
				} finally {
					step++; // step == 2
					finished();
				}
			}
		}.start();

		while (finished < 2) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				Assert.fail("interrupted exception");
			}
		}
		Assert.assertEquals("A thread failed", 0, failed);
		Assert.assertEquals("Incorrect number  of finished threads", 2, finished);
	}

	public void testWriterTimedoutWriter() {

		// first reader
		new Thread() {
			public void run() {
				try {
					pageLock.getExclusiveLock(30000);
					step++; //step == 1
					while (step != 2) {
						Thread.sleep(200);
					}
					pageLock.releaseExclusiveLock();
				} catch (InterruptedException e) {
					failed();
				} catch (ExceededWaitTimeException e) {
					failed();
				} catch (Exception e) {
					failed();
				}

				finished();
			}
		}.start();
		// writer that will timeout
		new Thread() {
			public void run() {
				try {
					while (step != 1) {
						Thread.sleep(20);
					}
					pageLock.getExclusiveLock(300);
					failed();
				} catch (InterruptedException e) {
					failed();
				} catch (ExceededWaitTimeException e) {

				} catch (Exception e) {
					failed();
				}
				step++; // step == 2
				finished();
			}
		}.start();

		while (finished < 2) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				Assert.fail("interrupted exception");
			}
		}
		Assert.assertEquals("A thread failed", 0, failed);
		Assert.assertEquals("Incorrect number  of finished threads", 2, finished);
	}
}
