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
package org.crappydbms.main;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;

import junit.framework.Assert;

import org.crappydbms.datadictionary.DataDictionary;
import org.crappydbms.datadictionary.InvalidStoredRelationNameException;
import org.crappydbms.dbfiles.FilePageID;
import org.crappydbms.dbfiles.locking.DBFileLockManager;
import org.crappydbms.dbfiles.locking.PageLock;
import org.crappydbms.logging.CrappyDBMSTestLogger;
import org.crappydbms.queries.aggregates.CountAggregate;
import org.crappydbms.relations.StoredRelation;
import org.crappydbms.relations.tuples.StoredTuple;
import org.crappydbms.transactions.Transaction;
import org.crappydbms.transactions.TransactionAbortedException;

/**
 * @author Facundo Manuel Quiroga Jan 13, 2009
 * 
 */
public class DatabaseMultithreadedAddTest extends CrappyDBMSTestCase {

	protected Logger logger = CrappyDBMSTestLogger.getLogger(DatabaseMultithreadedAddTest.class.getName());

	static volatile int finishedTasks;

	static synchronized void finished() {
		DatabaseMultithreadedAddTest.finishedTasks++;
	}

	private Database theDatabase;
	File directory;

	public void setUp() throws Exception {
		// Create test file
		CrappyDBMSTestCase.deleteTestDirectory();
		directory = CrappyDBMSTestCase.createTestDirectory();
		DataDictionary dataDictionary = new DataDictionary(directory);
		theDatabase = new Database(dataDictionary);
		DatabaseMultithreadedAddTest.finishedTasks = 0;
	}

	public void tearDown() throws Exception {
		super.tearDown();
	}

	public void testAddTuplesAbortPageBufferFull() {
		this.testAddTuples(1, 3, 50000, true);
	}

	public void testAddTuplesSingle() {
		this.testAddTuples(1, 2, 500, false);
	}

	public void testAddTuplesAbortDeadlock() {
		// System.out.println("testAddTuplesAbortDeadlock");
		this.testAddTuples(10, 10, 5, true);
	}

	public void testAddTuples(int numberOfTasks, int transactionsPerTask, int tuplesAddedPerTransaction, boolean shouldAbort) {
		CrappyDBMSTestCase.createTestingAssetsStoredRelationNamed(theDatabase, "assets");
		for (int i = 0; i < numberOfTasks; i++) {
			TuplesABM tuplesABM = new TuplesABM(i, transactionsPerTask, tuplesAddedPerTransaction, this.theDatabase, shouldAbort);
			new Thread(tuplesABM).start();
			// System.out.println("Started thread " + i);
		}
		try {
			while (DatabaseMultithreadedAddTest.finishedTasks < numberOfTasks) {
				Thread.sleep(2000);
				/*
				try {
					this.printTransactionsAndPages(this.theDatabase.getStoredRelationNamed("assets").getDBFile().getDBFileLockManager(),this.theDatabase);
				} catch (InvalidStoredRelationNameException e) {
					throw new CrappyDBMSTestingError(e.getMessage());
				}
			*/
			}
		} catch (InterruptedException e) {
			Assert.fail("Should not be interrupted");
		} 
		CrappyDBMSTestCase.checkDatabaseLocksAreFree(this.theDatabase);
		// System.out.println("done");
	}
	
	protected void printTransactionsAndPages(DBFileLockManager dbFileLockManager,Database database){
		this.printTransactions(database);
		this.printPages(dbFileLockManager);
	}

	private void printPages(DBFileLockManager dbFileLockManager) {
		for (FilePageID filePageId : dbFileLockManager.getPageLocks().keySet()) {
			PageLock pl = dbFileLockManager.getPageLocks().get(filePageId);
			System.out.println(filePageId + " lock:" + " writers: " + pl.getWriters() + " readers: " + pl.getReaders() + " w-writers "
					+ pl.getWritersWaitingCount() + " w-readers " + pl.getReadersWaitingCount() + " w-upgraders "
					+ pl.getUpgradersWaitingCount());
		}
	}

	private void printTransactions(Database database) {
		for (Transaction transaction : database.getTransactionManager().getTransactions()) {
			System.out.println(transaction.toString() + " s " + transaction.getStatus() + " ls " + transaction.getLockingStatus());
		}
	}

	public class TuplesABM implements Runnable {

		protected int id;
		protected int tuplesAddedPerTransaction;
		protected int transactions;
		protected Database database;
		protected Transaction transaction;
		protected boolean shouldAbort;

		public TuplesABM(int id, int transactions, int tuplesAddedPerTransaction, Database database, boolean shouldAbort) {
			this.id = id;
			this.tuplesAddedPerTransaction = tuplesAddedPerTransaction;
			this.database = database;
			this.shouldAbort = shouldAbort;
			this.transactions = transactions;
		}

		public void run() {
			try {
				StoredRelation relation = database.getStoredRelationNamed("assets");

				for (int t = 0; t < this.transactions; t++) {
					this.transaction = database.newTransaction();
					// logger.logp(Level.SEVERE,
					// DatabaseMultithreadedTest.class.getName(), "run", "id " + id +
					// " counting");
					int tuplesBefore = new CountAggregate(relation, this.transaction).calculate();
					// logger.logp(Level.SEVERE,
					// DatabaseMultithreadedTest.class.getName(), "run", "id " + id +
					// " adding tuples");
					ArrayList<StoredTuple> tuples = CrappyDBMSTestCase.createRandomTuplesForAssetsRelation(this.tuplesAddedPerTransaction, relation
							.getSchema());
					int added = 0;
					for (StoredTuple tuple : tuples) {
						// System.out.println("id "+id+" transaction "+t+" adding tuple "+added);
						relation.addTuple(tuple, this.transaction);
						added++;
						// System.out.println("id "+id+" transaction "+t+" tuples left "+
						// (this.tuplesAddedPerTransaction-added) );
					}
					// logger.logp(Level.SEVERE,
					// DatabaseMultithreadedTest.class.getName(), "run", "id " + id +
					// " counting again");
					int tuplesAfter = new CountAggregate(relation, this.transaction).calculate();
					Assert.assertEquals("Id " + id + " Invalid number of tuples after adding", tuplesBefore + this.tuplesAddedPerTransaction,
							tuplesAfter);
					// logger.logp(Level.SEVERE,
					// DatabaseMultithreadedTest.class.getName(), "run", "id " + id +
					// " commiting");
					this.transaction.commit();
					Thread.sleep(200);
				}
			} catch (TransactionAbortedException e) {
				if (!shouldAbort) {
					Assert.fail("Id " + id + " should not abort" + e.getMessage());
				} else {
					// logger.logp(Level.SEVERE,
					// DatabaseMultithreadedTest.class.getName(), "run", "id " + id +
					// " aborted");
				}
			} catch (InvalidStoredRelationNameException e) {
				Assert.fail("Id" + id + " Assets StoredRelation should exists " + e.getMessage());
			} catch (InterruptedException e) {
				Assert.fail("Id " + id + " should not be interrupted" + e.getMessage());
			} finally {
				// System.out.println("id "+id+" finished");
				DatabaseMultithreadedAddTest.finished();
			}
		}
	}

}
