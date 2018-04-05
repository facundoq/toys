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
import java.util.Random;

import org.crappydbms.datadictionary.DataDictionary;
import org.crappydbms.datadictionary.InvalidStoredRelationNameException;
import org.crappydbms.main.test.RandomChooser;
import org.crappydbms.main.test.operations.TestAddOperation;
import org.crappydbms.main.test.operations.TestCountOperation;
import org.crappydbms.main.test.operations.TestJoinOperation;
import org.crappydbms.main.test.operations.TestModifyOperation;
import org.crappydbms.main.test.operations.TestOperation;
import org.crappydbms.main.test.operations.TestRemoveOperation;
import org.crappydbms.main.test.operations.TestSelectOperation;
import org.crappydbms.relations.StoredRelation;
import org.crappydbms.transactions.Transaction;
import org.crappydbms.transactions.TransactionAbortedException;

/**
 * @author Facundo Manuel Quiroga Jan 22, 2009
 */
public class DatabaseStressTest {

	public enum Operation {
		ADD, REMOVE, MODIFY, COUNT, SELECT, JOIN
	}

	static volatile boolean testIsRunning;
	static volatile int testersFinished;

	static synchronized void testerFinished() {
		testersFinished++;
	}

	static int testerSleepTime = 500;
	static volatile int finishedTransactions;

	static synchronized void transactionFinished() {
		finishedTransactions++;
	}

	static volatile int failedTransactions;

	static synchronized void transactionFailed() {
		failedTransactions++;
	}

	Database theDatabase;

	int numberOfStoredRelations;
	int initialNumberOfTuplesAddedPerStoredRelation;

	public void setUp() throws Exception {
		// Create test file
		CrappyDBMSTestCase.deleteTestDirectory();
		File directory = CrappyDBMSTestCase.createTestDirectory();
		DataDictionary dataDictionary = new DataDictionary(directory);
		theDatabase = new Database(dataDictionary);
		finishedTransactions = 0;
		failedTransactions = 0;
		testersFinished = 0;
		numberOfStoredRelations = 50;
		DatabaseStressTestUtils.addRandomStoredRelations(numberOfStoredRelations, theDatabase);
		initialNumberOfTuplesAddedPerStoredRelation = 200;
		this.addTuplesToStoredRelations(initialNumberOfTuplesAddedPerStoredRelation);

		testIsRunning = true;

	}

	protected void addTuplesToStoredRelations(int initialNumberOfTuplesAddedPerStoredRelation) {
		try {
			System.out.println("Adding initial tuples..");
			Transaction transaction = this.theDatabase.newTransaction();
			for (int i = 0; i < this.numberOfStoredRelations; i++) {
				StoredRelation storedRelation;
				try {
					storedRelation = this.theDatabase.getStoredRelationNamed(Integer.valueOf(i).toString());
				} catch (InvalidStoredRelationNameException e) {
					throw new CrappyDBMSTestingError("InvalidStoredRelationNameException " + e.getMessage());
				}
				this.addTuplesToStoredRelation(storedRelation, transaction, initialNumberOfTuplesAddedPerStoredRelation);

			}
			transaction.commit();
		} catch (TransactionAbortedException e) {
			throw new CrappyDBMSTestingError("TransactionAbortedException while adding initial tuples, reason: " + e.getMessage());
		}
		System.out.println("Finished adding initial tuples.");
	}

	/**
	 * @param storedRelation
	 * @param initialNumberOfTuplesAddedPerStoredRelation2
	 * @throws TransactionAbortedException 
	 */
	protected void addTuplesToStoredRelation(StoredRelation storedRelation, Transaction transaction,
			int initialNumberOfTuplesAddedPerStoredRelation) throws TransactionAbortedException {
		TestAddOperation testAddOperation = new TestAddOperation(storedRelation, transaction, initialNumberOfTuplesAddedPerStoredRelation);
		testAddOperation.perform();

	}

	public void tearDown() throws Exception {
		if (theDatabase != null) {
			theDatabase.shutDown();
		}
		theDatabase = null;
		CrappyDBMSTestCase.deleteTestDirectory();
	}

	public static void main(String[] args) throws Exception {
		DatabaseStressTest databaseStressTest = new DatabaseStressTest();
		databaseStressTest.test();
	}

	public void test() {

		try {
			try {
				setUp();
				// TODO improve reports 
				int numberOfTesters = 10;
				ArrayList<DatabaseTester> testers = new ArrayList<DatabaseTester>(numberOfTesters);
				for (int i = 0; i < numberOfTesters; i++) {
					DatabaseTester tester = new DatabaseTester(theDatabase, numberOfStoredRelations);
					testers.add(tester);
					new Thread(tester).start();
				}
				long testInitTime = System.currentTimeMillis();
				long timeElapsed;
				while (true) {
					timeElapsed = System.currentTimeMillis() - testInitTime;
					System.out.println("");
					CrappyDBMSTestCase.printActiveTransactions(theDatabase);
					System.out.println("");
					this.printTime(timeElapsed);
					System.out.println("Finished transactions: " + finishedTransactions);
					System.out.println("Failed transactions: " + failedTransactions);
					System.out.println(" Failed transactions % : " + ((failedTransactions * 100.0) / (finishedTransactions + 1)));
					System.out.println("Active testers: " + (numberOfTesters - testersFinished));
					this.printTransactionsPerMinute(timeElapsed, finishedTransactions);
					System.out.println("");
					Thread.sleep(3000);

				}

			} finally {
				tearDown();
			}
		} catch (Exception e) {
			System.out.println("Exit: exception " + e.getMessage());
		}

	}

	/**
	 * @param timeElapsed
	 */
	protected void printTime(long timeElapsed) {
		long seconds = timeElapsed / 1000;
		long minutes = seconds / 60;
		long remainingSeconds = seconds % 60;
		System.out.println("Time elapsed: " + minutes + ":" + remainingSeconds);
	}

	protected void printTransactionsPerMinute(long timeElapsed, int transactions) {
		long seconds = timeElapsed / 1000;

		double minutes = (seconds / 60.0);
		double transactionsPerMinute = 0;
		if (minutes != 0) {
			transactionsPerMinute = transactions / minutes;
		}

		System.out.println("Transactions per minute " + transactionsPerMinute);
	}

	public class DatabaseTester implements Runnable {

		protected Database database;
		protected int numberOfRelations;

		public DatabaseTester(Database database, int numberOfRelations) {
			this.setDatabase(database);
			this.setNumberOfRelations(numberOfRelations);
		}

		@Override
		public void run() {
			Database database = this.getDatabase();
			try {
				while (testIsRunning) {
					try {
						Transaction transaction = database.newTransaction();
						int numberOfRelationsOperatedUpon = this.numberOfRelationsOperatedUpon();
						int relationNumber = -1;
						for (int i = 0; i < numberOfRelationsOperatedUpon; i++) {
							//  chance to repeat the relation
							int useTheSameRelationChance = new Random().nextInt(100);
							boolean useTheSameRelation = (useTheSameRelationChance < 1) && (i != 0);
							if (!useTheSameRelation) {
								relationNumber = new Random().nextInt(this.getNumberOfRelations());
							}
							String relationName = Integer.valueOf(relationNumber).toString();
							this.operateUponRelationNamed(relationName, database, transaction);
						}
						transaction.commit();
					} catch (TransactionAbortedException e) {
						System.out.println("Transaction failed: " + e.getMessage());
						transactionFailed();
					} finally {
						transactionFinished();
					}
					try {
						Thread.sleep(testerSleepTime);
					} catch (InterruptedException e) {
						System.out.println("Interrupted ¿why? " + e.getMessage());
						e.printStackTrace();
					}
				}
			} finally {
				testerFinished();
			}

		}

		protected void operateUponRelationNamed(String relationName, Database database, Transaction transaction)
				throws TransactionAbortedException {
			try {
				StoredRelation storedRelation = database.getStoredRelationNamed(relationName);
				this.operateUponStoredRelation(storedRelation, transaction);
			} catch (InvalidStoredRelationNameException e) {
				throw new CrappyDBMSTestingError("InvalidStoredRelationNameException, reason" + e.getMessage());
			}
		}

		/**
		 * Execute some random operation upon the stored relation. 
		 * Operation - Chance 
		 * add - 30% remove - 15% modify - 10% count - 5% select - 35% join - 5%
		 */
		protected void operateUponStoredRelation(StoredRelation storedRelation, Transaction transaction) throws TransactionAbortedException {
			//add - 30% remove - 15% modify - 10% count - 5% select - 35% join - 5%
			//int[] operationProbabilities = { 30, 15, 10, 5, 35, 5 };

			int[] operationProbabilities = { 10, 5, 5, 25, 45, 10 };

			int tuplesToAdd = new Random().nextInt(10) + 1;
			int tuplesToRemove = new Random().nextInt(5) + 1;
			int tuplesToModify = new Random().nextInt(5) + 1;

			TestOperation[] operations = { new TestAddOperation(storedRelation, transaction, tuplesToAdd),
					new TestRemoveOperation(storedRelation, transaction, tuplesToRemove),
					new TestModifyOperation(storedRelation, transaction, tuplesToModify), new TestCountOperation(storedRelation, transaction),
					new TestSelectOperation(storedRelation, transaction), new TestJoinOperation(storedRelation, transaction), };
			RandomChooser<TestOperation> randomChooser = new RandomChooser<TestOperation>(operations, operationProbabilities);
			randomChooser.getObject().perform();
		}

		/**
		 * @return a random number of relations that will be affected by this transaction
		 *  1 relation = 40% 
		 *  2 relations = 35%
		 *   3 relations = 15%
		 *    4 relations = 5%
		 *     5 relations = 5 %
		 */
		protected int numberOfRelationsOperatedUpon() {
			Integer[] relations = { 1, 2, 3, 4, 5 };
			int[] probabilities = { 40, 35, 15, 5, 5 };
			RandomChooser<Integer> randomChooser = new RandomChooser<Integer>(relations, probabilities);
			return randomChooser.getObject();
		}

		protected Database getDatabase() {
			return this.database;
		}

		protected void setDatabase(Database database) {
			this.database = database;
		}

		protected int getNumberOfRelations() {
			return this.numberOfRelations;
		}

		protected void setNumberOfRelations(int numberOfRelations) {
			this.numberOfRelations = numberOfRelations;
		}

	}
}
