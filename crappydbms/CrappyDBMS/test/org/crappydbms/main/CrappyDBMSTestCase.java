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
import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.crappydbms.datadictionary.DataDictionary;
import org.crappydbms.dbfiles.FilePageID;
import org.crappydbms.dbfiles.heapfile.HeapFile;
import org.crappydbms.dbfiles.locking.DBFileLockManager;
import org.crappydbms.dbfiles.locking.PageLock;
import org.crappydbms.exceptions.CrappyDBMSException;
import org.crappydbms.relations.StoredRelation;
import org.crappydbms.relations.fields.Field;
import org.crappydbms.relations.fields.IntegerField;
import org.crappydbms.relations.fields.StringField;
import org.crappydbms.relations.schemas.RelationSchema;
import org.crappydbms.relations.schemas.RepeatedAttributeNameException;
import org.crappydbms.relations.schemas.StoredRelationSchema;
import org.crappydbms.relations.schemas.attributes.Attribute;
import org.crappydbms.relations.schemas.attributes.BaseAttribute;
import org.crappydbms.relations.schemas.attributes.IntegerAttributeType;
import org.crappydbms.relations.schemas.attributes.StringAttributeType;
import org.crappydbms.relations.tuples.StoredTuple;
import org.crappydbms.relations.tuples.Tuple;
import org.crappydbms.transactions.Transaction;
import org.crappydbms.transactions.TransactionAbortedException;
import org.junit.After;

/**
 * A collection of useful Static methods for all test cases
 * and default tearDown method, that clears the buffer pool and deletes the test directory
 * @author Facundo Manuel Quiroga Nov 22, 2008
 * 
 */
public abstract class CrappyDBMSTestCase extends TestCase {

	public static void checkDatabaseLocksAreFree(Database database) {
		for (Transaction aTransaction : database.getTransactionManager().getTransactions()) {
			Assert.assertEquals(aTransaction+" should not hold exclusive locks",0, aTransaction.getExclusiveLocks().size());
			Assert.assertEquals(aTransaction+" should not hold shared locks", 0,aTransaction.getSharedLocks().size());
		}
		
		for (StoredRelation storedRelation : database.getDataDictionary().getStoredRelations().values()) {
			DBFileLockManager dbFileLockManager = storedRelation.getDBFile().getDBFileLockManager();
			for (FilePageID filePageId : dbFileLockManager.getPageLocks().keySet()) {
				String errorMessage = "Error: PageLock for " + filePageId + " still in use - ";
				PageLock pl = dbFileLockManager.getPageLocks().get(filePageId);
				Assert.assertEquals(errorMessage + "writers", 0, pl.getWriters());
				Assert.assertEquals(errorMessage + "readers", 0, pl.getReaders());
				Assert.assertEquals(errorMessage + "writersWaiting", 0, pl.getWritersWaitingCount());
				Assert.assertEquals(errorMessage + "readersWaiting", 0, pl.getReadersWaitingCount());
				Assert.assertEquals(errorMessage + "upgradesWaiting", 0, pl.getUpgradersWaitingCount());
			}
		}
	
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	protected void tearDown() throws Exception {
		CrappyDBMSTestCase.deleteTestDirectory();
	}

	public static Database setUpCommonTestingDatabase() {
		Database database;
		CrappyDBMSTestCase.deleteTestDirectory();
		File directory = CrappyDBMSTestCase.createTestDirectory();
		DataDictionary dataDictionary;
		try {
			dataDictionary = new DataDictionary(directory);
		} catch (Exception e) {
			throw new CrappyDBMSTestingError("Could not create Datadictionary for testing, message: " + e.getMessage());
		}
		database = new Database(dataDictionary);

		try {
			CrappyDBMSTestCase.createTestingAssetsStoredRelationNamed(database, "assets");
			CrappyDBMSTestCase.createTestingAssetsStoredRelationNamed(database, "assets2");
			CrappyDBMSTestCase.createTestingCustomersStoredRelationNamed(database, "customers");
		} catch (Exception e) {
			throw new CrappyDBMSTestingError("Could not create stored relations for testing, message: " + e.getMessage());
		}
		return database;
	}

	public static File createTestDirectory() {
		File directory = new File("testDB/");
		directory.mkdir();
		return directory;
	}

	public static void deleteTestDirectory() {
		File directory = new File("testDB/");
		if (directory.exists()) {
			if (directory.isDirectory()) {
				for (File file : directory.listFiles()) {
					if (file.canWrite()) {
						if (!file.delete()) {
							throw new CrappyDBMSTestingError("Could not delete testing directory");
						}
					} else {
						throw new CrappyDBMSTestingError("Could not write in testing directory");
					}

				}
			}
			directory.delete();
		}
	}

	// ASSETS RELATION
	/**
	 * name : juan + i
	 * date: "15/12/11"
	 * manager: "pedrito"
	 * value : 10
	 */
	public static ArrayList<StoredTuple> createRandomTuplesForAssetsRelation(int n, StoredRelationSchema relationSchema) {
		ArrayList<StoredTuple> tuples = new ArrayList<StoredTuple>();
		for (int i = 0; i < n; i++) {
			ArrayList<Field> fields = new ArrayList<Field>();
			fields.add(StringField.valueOf("juan" + i));
			fields.add(StringField.valueOf("15/12/11"));
			fields.add(StringField.valueOf("pedrito"));
			fields.add(IntegerField.valueOf(10));
			StoredTuple tuple = new StoredTuple(fields, relationSchema);
			tuples.add(tuple);
		}
		return tuples;
	}

	public static Tuple createRandomTupleForAssetsRelation(StoredRelationSchema relationSchema) {
		return CrappyDBMSTestCase.createRandomTuplesForAssetsRelation(1, relationSchema).iterator().next();
	}

	/**
	 * name : juan + i
	 * date: "15/12/11"
	 * manager: "pedrito"
	 * value : 10
	 * @throws TransactionAbortedException 
	 */
	public static void addRandomTuplesForAssetsRelation(int n, StoredRelation assets, Transaction transaction)
			throws TransactionAbortedException {
		ArrayList<StoredTuple> tuples = CrappyDBMSTestCase.createRandomTuplesForAssetsRelation(n, assets.getSchema());
		for (Tuple tuple : tuples) {
			assets.addTuple(tuple, transaction);
		}
	}

	public static void createTestingAssetsStoredRelationNamed(Database database, String name) {
		RelationSchema schema = CrappyDBMSTestCase.createAssetsRelationSchema();
		List<Attribute> primaryKey = CrappyDBMSTestCase.createPrimaryKeyForAssetsRelationSchema(schema);
		try {
			database.addStoredRelation(name, schema, primaryKey);
		} catch (CrappyDBMSException e) {
			throw new CrappyDBMSTestingError(e.getMessage());
		}
	}

	/**
	 * Create a StoredRelationSchema with attributes: name, date, manager, value and types string, string, integer, string
	 * @return
	 */
	public static StoredRelationSchema createAssetsRelationSchema() {
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		Attribute attribute;
		attribute = new BaseAttribute("name", StringAttributeType.getInstance());
		attributes.add(attribute);
		attribute = new BaseAttribute("date", StringAttributeType.getInstance());
		attributes.add(attribute);
		attribute = new BaseAttribute("manager", StringAttributeType.getInstance());
		attributes.add(attribute);
		attribute = new BaseAttribute("value", IntegerAttributeType.getInstance());
		attributes.add(attribute);
		try {
			return new StoredRelationSchema(attributes);
		} catch (RepeatedAttributeNameException e) {
			throw new CrappyDBMSTestingError("Attribute list does not have repeated attributes!");
		}
	}

	/** 
	 * @param assetsRelationSchema from which the primary key attributes are retrieved
	 * @return a primary key composed of name and manager attributes
	 */
	public static List<Attribute> createPrimaryKeyForAssetsRelationSchema(RelationSchema assetsRelationSchema) {
		ArrayList<Attribute> primaryKey = new ArrayList<Attribute>();
		Iterator<Attribute> iterator = assetsRelationSchema.getAttributes().iterator();
		primaryKey.add(iterator.next());
		iterator.next();
		primaryKey.add(iterator.next());
		return primaryKey;
	}

	protected static HeapFile newAssetsHeapFile() {
		File assetsFile;
		try {
			assetsFile = File.createTempFile("assets", "dat");
			assetsFile.deleteOnExit();
		} catch (IOException e) {
			throw new RuntimeException("could not create temp file");
		}
		StoredRelationSchema schema = CrappyDBMSTestCase.createAssetsRelationSchema();
		HeapFile heapFile;
		try {
			heapFile = new HeapFile(assetsFile, schema, null);
		} catch (IOException e) {
			Assert.fail("Should not throw an IOException ");
			throw new RuntimeException("This should never happen");
		}
		return heapFile;
	}

	// CUSTOMERS RELATIOn
	/**
	 * Attributes: customerName (string), amount (integer), address (string)
	 */
	public static StoredRelationSchema createCustomersRelationSchema() {
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		Attribute attribute;
		attribute = new BaseAttribute("customerName", StringAttributeType.getInstance());
		attributes.add(attribute);
		attribute = new BaseAttribute("amount", IntegerAttributeType.getInstance());
		attributes.add(attribute);
		attribute = new BaseAttribute("address", StringAttributeType.getInstance());
		attributes.add(attribute);
		try {
			return new StoredRelationSchema(attributes);
		} catch (RepeatedAttributeNameException e) {
			throw new CrappyDBMSTestingError("Attribute list does not have repeated attributes!");
		}
	}

	/**
	 * @param customersRelationSchema
	 * @return
	 */
	public static Tuple createRandomTupleForCustomersRelation(StoredRelationSchema relationSchema) {
		return CrappyDBMSTestCase.createRandomTuplesForCustomersRelation(1, relationSchema).iterator().next();
	}

	public static ArrayList<Tuple> createRandomTuplesForCustomersRelation(int n, StoredRelationSchema relationSchema) {
		ArrayList<Tuple> tuples = new ArrayList<Tuple>();
		for (int i = 0; i < n; i++) {
			ArrayList<Field> fields = new ArrayList<Field>();
			fields.add(StringField.valueOf("juan" + i));
			fields.add(IntegerField.valueOf(10));
			fields.add(StringField.valueOf("la plata"));
			Tuple tuple = new StoredTuple(fields, relationSchema);
			tuples.add(tuple);
		}
		return tuples;
	}

	private static void createTestingCustomersStoredRelationNamed(Database database, String name) {
		RelationSchema schema = CrappyDBMSTestCase.createCustomersRelationSchema();
		List<Attribute> primaryKey;
		try {
			primaryKey = CrappyDBMSTestCase.createPrimaryKeyForCustomersRelationSchema(schema);
			database.addStoredRelation(name, schema, primaryKey);
		} catch (Exception e) {
			throw new CrappyDBMSTestingError(e.getMessage());
		}
	}

	/** 
	 * @param customersRelationSchema from which the primary key attributes are retrieved
	 * @return a primary key composed of customerName attribute
	 */
	public static List<Attribute> createPrimaryKeyForCustomersRelationSchema(RelationSchema assetsRelationSchema) throws IOException,
			CrappyDBMSException {
		ArrayList<Attribute> primaryKey = new ArrayList<Attribute>();
		Iterator<Attribute> iterator = assetsRelationSchema.getAttributes().iterator();
		primaryKey.add(iterator.next());
		return primaryKey;
	}

	public static void addRandomTuplesForCustomersRelation(int n, StoredRelation customers, Transaction transaction)
			throws TransactionAbortedException {
		ArrayList<Tuple> tuples = CrappyDBMSTestCase.createRandomTuplesForCustomersRelation(n, customers.getSchema());
		for (Tuple tuple : tuples) {
			customers.addTuple(tuple, transaction);
		}
	}

	public static void printActiveTransactions(Database database) {
		System.out.println("Active transactions:");
		for (Transaction transaction : database.getTransactionManager().getActiveTransactionsCopy()) {
			System.out.println(transaction.toString() + " s " + transaction.getStatus() + " ls " + transaction.getLockingStatus()+" last action "+ transaction.getLastAction().getDescription()); 
		}
	}
	
}
