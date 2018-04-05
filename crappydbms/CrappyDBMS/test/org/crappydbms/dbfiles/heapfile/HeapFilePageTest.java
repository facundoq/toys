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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

import junit.framework.Assert;

import org.crappydbms.exceptions.CrappyDBMSError;
import org.crappydbms.logging.CrappyDBMSLogger;
import org.crappydbms.main.CrappyDBMSTestCase;
import org.crappydbms.main.PageManager;
import org.crappydbms.relations.schemas.StoredRelationSchema;
import org.crappydbms.relations.tuples.StoredTuple;
import org.crappydbms.relations.tuples.Tuple;

/**
 * @author Facundo Manuel Quiroga
 * Nov 23, 2008
 * 
 */
public class HeapFilePageTest extends CrappyDBMSTestCase {
	
	protected Logger logger = CrappyDBMSLogger.getLogger(HeapFilePageTest.class.getName());
	protected HeapFile heapFile;
	protected File assetsFile;
	protected HeapFilePage heapFilePage;
	protected HeapFilePageID heapFilePageID;
	protected int heapPageSize;
	
	protected void setUp() throws Exception {
		super.setUp();
		this.heapFile = CrappyDBMSTestCase.newAssetsHeapFile();
		heapFilePageID = new HeapFilePageID(heapFile,0);
		heapPageSize = PageManager.getDefaultPageSize();
		byte[] bytes = new byte[heapPageSize] ;
		bytes[0] = 0;
		heapFilePage = new HeapFilePage(bytes, heapFilePageID);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		this.heapFile.removeSelf();
		this.heapFile = null;
	}
	
	public void testNew(){
		Assert.assertEquals("must have 0 tuples!", 0, heapFilePage.getNumberOfTuples() );
		StoredRelationSchema relationSchema =CrappyDBMSTestCase.createAssetsRelationSchema(); 
		int tupleSize = relationSchema.getTupleSize();
		int maximumTuples = (heapPageSize-1) / tupleSize;
		Assert.assertEquals("",maximumTuples, heapFilePage.getNumberOfTupleSlots());
		
		
	}
	public void testAddtuples(){
		StoredRelationSchema relationSchema =CrappyDBMSTestCase.createAssetsRelationSchema(); 
		int numberOfTuples = heapFilePage.getNumberOfTupleSlots()+1;
		ArrayList<StoredTuple> tuples = CrappyDBMSTestCase.createRandomTuplesForAssetsRelation(numberOfTuples,relationSchema );
		Iterator<StoredTuple> iterator = tuples.iterator();
		for (int i = 0; i<numberOfTuples-1;i++){
			Tuple tuple = iterator.next();
			heapFilePage.addTuple((StoredTuple) tuple);
		}
		try{
			Tuple tuple = iterator.next();
			heapFilePage.addTuple((StoredTuple) tuple);
			Assert.fail("Should have thrown a CrappyDBMSError because the page is full");
		} catch (CrappyDBMSError e){
			
		}
	}
	public void testSerializationAndDeserialization(){
		StoredRelationSchema relationSchema =CrappyDBMSTestCase.createAssetsRelationSchema(); 
		int numberOfTuples = heapFilePage.getNumberOfTupleSlots();
		ArrayList<StoredTuple> tuples = CrappyDBMSTestCase.createRandomTuplesForAssetsRelation(numberOfTuples,relationSchema );
		Iterator<StoredTuple> iterator = tuples.iterator();
		for (int i = 0; i<numberOfTuples;i++){
			Tuple tuple = iterator.next();
			heapFilePage.addTuple((StoredTuple) tuple);
		}
		byte[] serializedPage = heapFilePage.serialize();

		HeapFilePage newHeapFile = new HeapFilePage(serializedPage,heapFilePageID);
		Assert.assertEquals("number of tuples should match!",numberOfTuples, newHeapFile.getTuples().size());
		Iterator<StoredTuple> newTuplesIterator = newHeapFile.iterator();
		for (Tuple oldTuple : tuples){
			Tuple newTuple = newTuplesIterator.next();
			Assert.assertTrue("Tuples should match! oldTuple:"+oldTuple+" newTuple: "+newTuple,newTuple.equals(oldTuple));
		}
	}

}
