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
package org.crappydbms.dbfiles.heapfile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.Assert;

import org.crappydbms.dbfiles.FilePage;
import org.crappydbms.dbfiles.FilePageIterator;
import org.crappydbms.exceptions.CrappyDBMSException;
import org.crappydbms.logging.CrappyDBMSLogger;
import org.crappydbms.main.CrappyDBMSTestCase;
import org.crappydbms.main.Database;
import org.crappydbms.main.PageBufferFullException;
import org.crappydbms.relations.RelationIterator;
import org.crappydbms.relations.StoredRelationIterator;
import org.crappydbms.relations.fields.Field;
import org.crappydbms.relations.schemas.StoredRelationSchema;
import org.crappydbms.relations.tuples.StoredTuple;
import org.crappydbms.relations.tuples.Tuple;
import org.crappydbms.transactions.LockMode;
import org.crappydbms.transactions.Transaction;
import org.crappydbms.transactions.TransactionAbortedException;

/**
 * @author Facundo Manuel Quiroga Nov 22, 2008
 * 
 */
public class HeapFileTest extends CrappyDBMSTestCase {
	protected Logger logger = CrappyDBMSLogger.getLogger(HeapFileTest.class.getName());
	protected HeapFile heapFile;

	protected File assetsFile;

	protected Transaction transaction;
	protected Database database;
	
	protected void setUp() throws Exception {
		super.setUp();
		database = CrappyDBMSTestCase.setUpCommonTestingDatabase();
		this.heapFile =  (HeapFile) database.getStoredRelationNamed("assets").getDBFile();
		this.transaction = database.newTransaction();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	
	public void testNewHeapFile() throws TransactionAbortedException, IOException{
		logger.logp(Level.FINE, HeapFileTest.class.getName(), "testNewHeapFile", "testing testNewHeapFile");
		Assert.assertEquals("Should have 0 pages", 0,heapFile.calculateNumberOfPages() );
		try {
			heapFile.searchForNotFullPage(transaction);
			Assert.fail("Should not have any not-full page");
		} catch (NoEmptyPagesException e) {
			
		} catch (PageBufferFullException e) {
			Assert.fail("buffer should not be full; there aren't any pages");
		}
		try {
			
			heapFile.getPage(0);
			Assert.fail("Should throw an InvalidPageNumberError");
		} catch (IOException e) {
			Assert.fail(" Page number is invalid, should not throw an IOException: "+e.getMessage());
		} catch (InvalidPageNumberError e){
			
		} catch (PageBufferFullException e) {
			Assert.fail("buffer should not be full; there aren't any pages  ");
		}
		this.transaction.commit();
	}

	public void testHeapFilePagesIterator() throws IOException, CrappyDBMSException {
		logger.logp(Level.FINE, HeapFileTest.class.getName(), "testHeapFilePagesIterator", "testing testHeapFilePagesIterator");
		FilePageIterator<FilePage> heapFilePageIterator = heapFile.getHeapFilePagesIterator(transaction,LockMode.exclusive());
		Assert.assertFalse("The file should not have any pages",heapFilePageIterator.hasNext());		
		this.transaction.commit();
	}

	/**
	 * Test method for {@link org.crappydbms.dbfiles.heapfile.HeapFile#readPage(long)}.
	 */
	public void testRead() {

	}

	/**
	 * Test method for
	 * {@link org.crappydbms.dbfiles.heapfile.HeapFile#addTuple(org.crappydbms.relations.tuples.Tuple)}
	 * .
	 */
	public void testAddTuples() throws IOException, CrappyDBMSException {
		
		logger.logp(Level.FINE, HeapFileTest.class.getName(), "testAddTuples", "testing addTuple");
		StoredRelationSchema relationSchema = heapFile.getRelationSchema();
		int numberOfTuples = 300;
		ArrayList<StoredTuple> tuples = CrappyDBMSTestCase.createRandomTuplesForAssetsRelation(numberOfTuples, relationSchema);
		int insertedTuples = 0;
		for (StoredTuple tuple : tuples){
			heapFile.addTuple( tuple,transaction);
			insertedTuples++;
		}
		transaction.commit();
		
		transaction = database.newTransaction();
		RelationIterator<Tuple> iterator = heapFile.iterator(transaction,LockMode.shared());
		for (Tuple tuple : tuples) {
			if (!iterator.hasNext()) {
				Assert.fail("Should have more tuples!");
			}
			Tuple nextTuple = iterator.next();
			Assert.assertNotNull("Tuples should NOT be null", nextTuple);
			Assert.assertTrue("Tuples should have the same class. expected: "+tuple.getClass()+" actual:"+nextTuple.getClass(), nextTuple.getClass().equals(tuple.getClass()));		
			Assert.assertTrue("Tuples should be the same. expected: "+tuple+" actual:"+nextTuple, nextTuple.equals(tuple));
			Assert.assertEquals("equal tuples should have the same string representation", tuple.toString(), nextTuple.toString());
		}
		Assert.assertFalse("The relation should have " + numberOfTuples + " tuples, has more", iterator.hasNext());
		this.transaction.commit();
	}

	/**
	 * Test method for
	 * {@link org.crappydbms.dbfiles.heapfile.HeapFile#removeTuple(org.crappydbms.relations.tuples.Tuple)}
	 * .
	 */
	public void testRemoveTuples() throws Exception{
		logger.logp(Level.FINE, HeapFileTest.class.getName(), "testAddTuples", "testing addTuple");
		StoredRelationSchema relationSchema = heapFile.getRelationSchema();
		int numberOfTuples = 300;
		ArrayList<StoredTuple> tuples = CrappyDBMSTestCase.createRandomTuplesForAssetsRelation(numberOfTuples, relationSchema);

		int insertedTuples = 0;
		for (StoredTuple tuple : tuples){
			heapFile.addTuple( tuple,transaction);
			insertedTuples++;
		}

		StoredRelationIterator<Tuple> iterator = heapFile.iterator(transaction,LockMode.exclusive());
		while (iterator.hasNext()){
			iterator.next();
			iterator.remove();
		}
		
		
		int tupleCount = 0;
		iterator = heapFile.iterator(transaction, LockMode.shared());
		while (iterator.hasNext()){
			iterator.next();
			tupleCount++;			
		}
		 Assert.assertEquals("Should not have any tuples",0,tupleCount);
		 this.transaction.commit();
	}

	public void testRemoveTuple() throws Exception{
		// ADD TUPLES
		logger.logp(Level.FINE, HeapFileTest.class.getName(), "testAddTuples", "testing addTuple");
		StoredRelationSchema relationSchema = heapFile.getRelationSchema();
		int numberOfTuples = 300;
		ArrayList<StoredTuple> tuples = CrappyDBMSTestCase.createRandomTuplesForAssetsRelation(numberOfTuples, relationSchema);

		int insertedTuples = 0;
		for (StoredTuple tuple : tuples){
			heapFile.addTuple( tuple,transaction);
			insertedTuples++;
		}
		// REMOVE ONE TUPLE
		StoredRelationIterator<Tuple> iterator = heapFile.iterator(transaction,LockMode.exclusive());
		Tuple removedTuple = iterator.next();
		Field removedTupleField = removedTuple.getFieldNamed("name");
		iterator.remove();

		int tupleCount = 0;
		while (iterator.hasNext()){
			Tuple tuple = iterator.next();
			tupleCount++;
			Assert.assertFalse("Should not have any tuples",tuple.getFieldNamed("name").equals(removedTupleField));
		}
		
		Assert.assertEquals("Did not remove 1 tuple",299, tupleCount);
		iterator = heapFile.iterator(transaction,LockMode.shared());
		// CHECK ALL TUPLES MATCH AFTER REMOVING THE FIRST
		tuples.remove(0);
		for (Tuple tuple : tuples) {
			if (!iterator.hasNext()) {
				Assert.fail("Should have more tuples!");
			}
			Tuple nextTuple = iterator.next();
			Assert.assertNotNull("Tuples should NOT be null", nextTuple);
			Assert.assertTrue("Tuples should have the same class. expected: "+tuple.getClass()+" actual:"+nextTuple.getClass(), nextTuple.getClass().equals(tuple.getClass()));		
			Assert.assertTrue("Tuples should be the same. expected: "+tuple+" actual:"+nextTuple, nextTuple.equals(tuple));
			Assert.assertEquals("equal tuples should have the same string representation", tuple.toString(), nextTuple.toString());
		}
		this.transaction.commit();
	}



}
