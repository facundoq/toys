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
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.Assert;

import org.crappydbms.datadictionary.DataDictionary;
import org.crappydbms.datadictionary.InvalidStoredRelationNameException;
import org.crappydbms.dbfiles.locking.DBFileLockManager;
import org.crappydbms.dbfiles.locking.PageLock;
import org.crappydbms.exceptions.CrappyDBMSException;
import org.crappydbms.logging.CrappyDBMSLogger;
import org.crappydbms.relations.RelationIterator;
import org.crappydbms.relations.StoredRelation;
import org.crappydbms.relations.schemas.RelationSchema;
import org.crappydbms.relations.tuples.StoredTuple;
import org.crappydbms.relations.tuples.Tuple;
import org.crappydbms.transactions.Transaction;
import org.crappydbms.transactions.TransactionAbortedException;

/**
 * @author Facundo Manuel Quiroga Oct 31, 2008
 */
public class DatabaseTest extends CrappyDBMSTestCase {

	protected Logger logger = CrappyDBMSLogger.getLogger(DatabaseTest.class.getName());
	Database database;

	File directory;
	Transaction transaction;

	public void setUp() throws Exception {
		// Create test file
		CrappyDBMSTestCase.deleteTestDirectory();
		directory = CrappyDBMSTestCase.createTestDirectory();
		DataDictionary dataDictionary = new DataDictionary(directory);
		database = new Database(dataDictionary);
		this.transaction = database.newTransaction();
	}

	public void tearDown() throws Exception {
		CrappyDBMSTestCase.checkDatabaseLocksAreFree(database);
		super.tearDown();
	}

	public void testAddStoredRelation() {
		try {
			CrappyDBMSTestCase.createTestingAssetsStoredRelationNamed(database, "assets");

			StoredRelation relation;
			try {
				relation = database.getStoredRelationNamed("assets");
				RelationIterator<Tuple> iterator = relation.iterator(this.transaction);
				Assert.assertFalse("A newly created stored relation should be empty", iterator.hasNext());
			} catch (CrappyDBMSException e) {
				Assert.fail(e.getMessage());
			}
			this.transaction.commit();
		} catch (TransactionAbortedException e) {
			Assert.fail("Transaction should not be aborted : " + e.getMessage());
		}
	}

	public void testAddTuple() throws IOException, CrappyDBMSException {
		CrappyDBMSTestCase.createTestingAssetsStoredRelationNamed(database, "assets");
		StoredRelation relation = database.getStoredRelationNamed("assets");

		Tuple tuple = CrappyDBMSTestCase.createRandomTuplesForAssetsRelation(1, relation.getSchema()).iterator().next();
		relation.addTuple(tuple, this.transaction);
		RelationIterator<Tuple> iterator = relation.iterator(this.transaction);
		Assert.assertTrue("The relation should have one tuple, has none", iterator.hasNext());
		iterator.next();
		Assert.assertFalse("The relation should have one tuple, has more than one", iterator.hasNext());
		this.transaction.commit();
	}

	public void testAddTuples() throws IOException, CrappyDBMSException {
		logger.logp(Level.FINE, DatabaseTest.class.getName(), "testAddTuples", "testing adding tuples and verifying if the iterator over the tuples works correctly ");
		CrappyDBMSTestCase.createTestingAssetsStoredRelationNamed(database, "assets");
		StoredRelation relation = database.getStoredRelationNamed("assets");
		int numberOfTuples = 300;
		ArrayList<StoredTuple> tuples = CrappyDBMSTestCase.createRandomTuplesForAssetsRelation(numberOfTuples, relation.getSchema());
		for (Tuple tuple : tuples) {
			relation.addTuple(tuple, this.transaction);
		}
		RelationIterator<Tuple> iterator = relation.iterator(this.transaction);
		for (Tuple tuple : tuples) {
			if (!iterator.hasNext()) {
				Assert.fail("Should have more tuples!");
			}
			Tuple nextTuple = iterator.next();
			Assert.assertNotNull("Tuples should NOT be null", nextTuple);
			Assert.assertTrue("Tuples should have the same class. expected: " + tuple.getClass() + " actual:" + nextTuple.getClass(), nextTuple.getClass().equals(tuple.getClass()));
			Assert.assertTrue("Tuples should be the same. expected: " + tuple + " actual:" + nextTuple, nextTuple.equals(tuple));
			Assert.assertEquals("equal tuples should have the same string representation", tuple.toString(), nextTuple.toString());
		}
		Assert.assertFalse("The relation should have " + numberOfTuples + " tuples, has more", iterator.hasNext());
		transaction.commit();
	}

	public void testAddTuplesMultipleRelations() throws IOException, CrappyDBMSException {
		logger.logp(Level.FINE, DatabaseTest.class.getName(), "testAddTuples", "testing adding tuples and verifying if the iterator over the tuples works correctly ");
		CrappyDBMSTestCase.createTestingAssetsStoredRelationNamed(database, "assets1");
		CrappyDBMSTestCase.createTestingAssetsStoredRelationNamed(database, "assets2");
		StoredRelation assets1 = database.getStoredRelationNamed("assets1");
		StoredRelation assets2 = database.getStoredRelationNamed("assets2");

		int numberOfTuples = 100;
		ArrayList<StoredTuple> tuples = CrappyDBMSTestCase.createRandomTuplesForAssetsRelation(numberOfTuples, assets1.getSchema());

		for (Tuple tuple : tuples) {
			assets1.addTuple(tuple, this.transaction);
			assets2.addTuple(tuple, this.transaction);
		}

		RelationIterator<Tuple> assets1Iterator = assets1.iterator(this.transaction);
		RelationIterator<Tuple> assets2Iterator = assets2.iterator(this.transaction);
		for (Tuple tuple : tuples) {
			if (!assets1Iterator.hasNext()) {
				Assert.fail(" Assets1 relation should have more tuples!");
			}
			if (!assets2Iterator.hasNext()) {
				Assert.fail(" Assets2 relation should have more tuples!");
			}
			Tuple assets1Tuple = assets1Iterator.next();
			Tuple assets2Tuple = assets2Iterator.next();
			Assert.assertNotNull("Tuples should NOT be null", assets1Tuple);
			Assert.assertTrue(" Assets1 tuples should be the same. expected: " + tuple + " actual:" + assets1Tuple, assets1Tuple.equals(tuple));
			Assert.assertTrue("Assets2 tuples should be the same. expected: " + tuple + " actual:" + assets2Tuple, assets2Tuple.equals(tuple));
		}
		Assert.assertFalse("The relation assets1 should have " + numberOfTuples + " tuples, has more", assets1Iterator.hasNext());
		Assert.assertFalse("The relation assets2 should have " + numberOfTuples + " tuples, has more", assets2Iterator.hasNext());
		this.transaction.commit();
	}

	public void testRemoveStoredRelation() throws IOException, CrappyDBMSException {

		RelationSchema assetsRelationSchema = CrappyDBMSTestCase.createAssetsRelationSchema();
		StoredRelation relation = this.database.addStoredRelation("assets", assetsRelationSchema, CrappyDBMSTestCase.createPrimaryKeyForAssetsRelationSchema(assetsRelationSchema));
		this.database.removeStoredRelation(relation);
		try {
			database.getStoredRelationNamed("customers");
			Assert.fail("The relation 'customers' should have been removed from the database");
		} catch (CrappyDBMSException e) {
		}
	}

	public void testPersistanceAfterShutdown() throws Exception {
		logger.logp(Level.FINE, DatabaseTest.class.getName(), "testAddTuples", "testing adding tuples and verifying if the iterator over the tuples works correctly ");
		CrappyDBMSTestCase.createTestingAssetsStoredRelationNamed(database, "assets1");
		StoredRelation assets1 = database.getStoredRelationNamed("assets1");

		int numberOfTuples = 100;
		ArrayList<StoredTuple> tuples = CrappyDBMSTestCase.createRandomTuplesForAssetsRelation(numberOfTuples, assets1.getSchema());

		for (Tuple tuple : tuples) {
			assets1.addTuple(tuple, this.transaction);
		}
		this.transaction.commit();
		database.shutDown();
		DataDictionary dataDictionary = new DataDictionary(directory);
		database = new Database(dataDictionary);
		this.transaction = database.newTransaction();
		//check everything is as it was before

		RelationIterator<Tuple> assets1Iterator = assets1.iterator(this.transaction);
		int tupleCount = 0;
		for (Tuple tuple : tuples) {
			tupleCount++;
			if (!assets1Iterator.hasNext()) {
				Assert.fail(" Assets1 relation should have more tuples!; tupleCount " + tupleCount);
			}
			Tuple assets1Tuple = assets1Iterator.next();
			Assert.assertNotNull("Tuples should NOT be null", assets1Tuple);
			Assert.assertTrue(" Assets1 tuples should be the same. expected: " + tuple + " actual:" + assets1Tuple, assets1Tuple.equals(tuple));
		}
		Assert.assertFalse("The relation assets1 should have " + numberOfTuples + " tuples, has more", assets1Iterator.hasNext());
		transaction.commit();
	}

	public void testPersistanceAfterShutdownWithMultipleRelations() throws Exception {
		logger.logp(Level.FINE, DatabaseTest.class.getName(), "testAddTuples", "testing adding tuples and verifying if the iterator over the tuples works correctly ");
		CrappyDBMSTestCase.createTestingAssetsStoredRelationNamed(database, "assets1");
		CrappyDBMSTestCase.createTestingAssetsStoredRelationNamed(database, "assets2");
		StoredRelation assets1 = database.getStoredRelationNamed("assets1");
		StoredRelation assets2 = database.getStoredRelationNamed("assets2");

		int numberOfTuples = 300;
		ArrayList<StoredTuple> tuples = CrappyDBMSTestCase.createRandomTuplesForAssetsRelation(numberOfTuples, assets1.getSchema());

		for (Tuple tuple : tuples) {
			assets1.addTuple(tuple, this.transaction);
			assets2.addTuple(tuple, this.transaction);
		}
		this.transaction.commit();
		database.shutDown();
		DataDictionary dataDictionary = new DataDictionary(directory);
		database = new Database(dataDictionary);
		this.transaction = database.newTransaction();
		//check everything is as it was before
		RelationIterator<Tuple> assets1Iterator = assets1.iterator(this.transaction);
		RelationIterator<Tuple> assets2Iterator = assets2.iterator(this.transaction);
		int tupleCount = 0;
		for (Tuple tuple : tuples) {
			if (!assets1Iterator.hasNext()) {
				Assert.fail(" Assets1 relation should have more tuples!; tupleCount " + tupleCount);
			}
			if (!assets2Iterator.hasNext()) {
				Assert.fail(" Assets2 relation should have more tuples!; tupleCount " + tupleCount);
			}
			tupleCount++;
			Tuple assets1Tuple = assets1Iterator.next();
			Tuple assets2Tuple = assets2Iterator.next();
			Assert.assertNotNull("Tuples should NOT be null", assets1Tuple);
			Assert.assertTrue(" Assets1 tuples should be the same. expected: " + tuple + " actual:" + assets1Tuple, assets1Tuple.equals(tuple));
			Assert.assertTrue("Assets2 tuples should be the same. expected: " + tuple + " actual:" + assets2Tuple, assets2Tuple.equals(tuple));
		}
		Assert.assertFalse("The relation assets1 should have " + numberOfTuples + " tuples, has more", assets1Iterator.hasNext());
		Assert.assertFalse("The relation assets2 should have " + numberOfTuples + " tuples, has more", assets2Iterator.hasNext());
		transaction.commit();
	}

	public void testPersistanceAfterCommit() throws InvalidStoredRelationNameException {
		logger.logp(Level.FINE, DatabaseTest.class.getName(), "testAddTuples", "testing adding tuples and verifying if the iterator over the tuples works correctly ");
		try {
			CrappyDBMSTestCase.createTestingAssetsStoredRelationNamed(database, "assets");
			StoredRelation relation;
			relation = database.getStoredRelationNamed("assets");
			int numberOfTuples = 19;
			ArrayList<StoredTuple> tuples = CrappyDBMSTestCase.createRandomTuplesForAssetsRelation(numberOfTuples, relation.getSchema());
			for (Tuple tuple : tuples) {
				relation.addTuple(tuple, this.transaction);
			}
			transaction.commit();
			this.transaction = this.database.newTransaction();
			RelationIterator<Tuple> iterator = relation.iterator(this.transaction);
			for (Tuple tuple : tuples) {
				if (!iterator.hasNext()) {
					Assert.fail("Should have more tuples!");
				}
				Tuple nextTuple = iterator.next();
				Assert.assertNotNull("Tuples should NOT be null", nextTuple);
				Assert.assertTrue("Tuples should have the same class. expected: " + tuple.getClass() + " actual:" + nextTuple.getClass(), nextTuple.getClass().equals(tuple.getClass()));
				Assert.assertTrue("Tuples should be the same. expected: " + tuple + " actual:" + nextTuple, nextTuple.equals(tuple));
				Assert.assertEquals("equal tuples should have the same string representation", tuple.toString(), nextTuple.toString());
			}
			Assert.assertFalse("The relation should have " + numberOfTuples + " tuples, has more", iterator.hasNext());
			transaction.commit();
		} catch (TransactionAbortedException e) {
			Assert.fail("Transaction should not be aborted!" + e.getMessage());
		}
	}

	public void testLocksAfterCommit() throws InvalidStoredRelationNameException {
		logger.logp(Level.FINE, DatabaseTest.class.getName(), "testAddTuples", "testing adding tuples and verifying if the iterator over the tuples works correctly ");
		StoredRelation relation;
		CrappyDBMSTestCase.createTestingAssetsStoredRelationNamed(database, "assets");
		relation = database.getStoredRelationNamed("assets");
		try {
			int numberOfTuples = 300;
			ArrayList<StoredTuple> tuples = CrappyDBMSTestCase.createRandomTuplesForAssetsRelation(numberOfTuples, relation.getSchema());
			for (Tuple tuple : tuples) {
				relation.addTuple(tuple, this.transaction);
			}
			transaction.commit();
		} catch (TransactionAbortedException e) {
			Assert.fail("Transaction should not be aborted!" + e.getMessage());
		}
		DBFileLockManager dbFileLockManager = relation.getDBFile().getDBFileLockManager();
		for (PageLock pageLock : dbFileLockManager.getPageLocks().values()) {
			Assert.assertEquals("Shouldn't be any writers", 0, pageLock.getWriters());
			Assert.assertEquals("Shouldn't be any readers", 0, pageLock.getReaders());
		}

	}

	public void testPersistanceAfterAbort() throws InvalidStoredRelationNameException {
		logger.logp(Level.FINE, DatabaseTest.class.getName(), "testAddTuples", "testing adding tuples and verifying if the iterator over the tuples works correctly ");
		CrappyDBMSTestCase.createTestingAssetsStoredRelationNamed(database, "assets");
		StoredRelation relation = database.getStoredRelationNamed("assets");
		try {
			int numberOfTuples = 19;
			ArrayList<StoredTuple> tuples = CrappyDBMSTestCase.createRandomTuplesForAssetsRelation(numberOfTuples, relation.getSchema());
			for (Tuple tuple : tuples) {
				relation.addTuple(tuple, this.transaction);
			}
			transaction.abort("testPersistanceAfterAbort");
			Assert.fail("Transaction should  be aborted!");
		} catch (TransactionAbortedException e) {
		}
		try {
			this.transaction = this.database.newTransaction();
			RelationIterator<Tuple> iterator = relation.iterator(this.transaction);
			if (iterator.hasNext()) {
				Assert.fail("Should not have any tuples!");
			}
			transaction.commit();
		} catch (TransactionAbortedException e) {
			Assert.fail("Transaction should not  be aborted!");
		}
		DBFileLockManager dbFileLockManager = relation.getDBFile().getDBFileLockManager();
		for (PageLock pageLock : dbFileLockManager.getPageLocks().values()) {
			Assert.assertEquals("Shouldn't be any writers", 0, pageLock.getWriters());
			Assert.assertEquals("Shouldn't be any readers", 0, pageLock.getReaders());
		}
	}

	public void testPersistanceAfterCommitAndAbort() throws InvalidStoredRelationNameException {
		CrappyDBMSTestCase.createTestingAssetsStoredRelationNamed(database, "assets");
		StoredRelation relation = database.getStoredRelationNamed("assets");
		int firstNumberOfTuples = 50;
		int secondNumberOfTuples = 50;
		ArrayList<StoredTuple> tuples = CrappyDBMSTestCase.createRandomTuplesForAssetsRelation(firstNumberOfTuples + secondNumberOfTuples, relation.getSchema());
		try {
			for (int i = 0; i < firstNumberOfTuples; i++) {
				relation.addTuple(tuples.get(i), this.transaction);
			}
			transaction.commit();
		} catch (TransactionAbortedException e) {
			Assert.fail("Transaction should not be aborted!" + e.getMessage());
		}

		this.transaction = this.database.newTransaction();
		try {
			for (int i = firstNumberOfTuples; i < secondNumberOfTuples; i++) {
				relation.addTuple(tuples.get(i), this.transaction);
			}
			transaction.abort();
			Assert.fail("Transaction should be aborted!");
		} catch (TransactionAbortedException e) {
		}

		this.transaction = this.database.newTransaction();

		try {
			RelationIterator<Tuple> iterator = relation.iterator(this.transaction);
			for (int i = 0; i < firstNumberOfTuples; i++) {
				Tuple tuple = tuples.get(i);
				if (!iterator.hasNext()) {
					Assert.fail("Should have more tuples!");
				}
				Tuple nextTuple = iterator.next();
				Assert.assertNotNull("Tuples should NOT be null", nextTuple);
				Assert.assertTrue("Tuples should have the same class. expected: " + tuple.getClass() + " actual:" + nextTuple.getClass(), nextTuple.getClass().equals(tuple.getClass()));
				Assert.assertTrue("Tuples should be the same. expected: " + tuple + " actual:" + nextTuple, nextTuple.equals(tuple));
				Assert.assertEquals("equal tuples should have the same string representation", tuple.toString(), nextTuple.toString());
			}
			Assert.assertFalse("The relation should have " + firstNumberOfTuples + " tuples, has more", iterator.hasNext());
			transaction.commit();
		} catch (TransactionAbortedException e) {
			Assert.fail("Transaction should not be aborted!" + e.getMessage());
		}
	}
}
