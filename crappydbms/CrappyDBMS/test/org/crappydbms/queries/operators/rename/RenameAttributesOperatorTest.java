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
package org.crappydbms.queries.operators.rename;

import java.util.ArrayList;

import junit.framework.Assert;

import org.crappydbms.exceptions.InvalidAttributeNameException;
import org.crappydbms.main.CrappyDBMSTestCase;
import org.crappydbms.main.Database;
import org.crappydbms.relations.RelationIterator;
import org.crappydbms.relations.StoredRelation;
import org.crappydbms.relations.schemas.StoredRelationSchema;
import org.crappydbms.relations.tuples.Tuple;
import org.crappydbms.transactions.Transaction;
import org.crappydbms.transactions.TransactionAbortedException;

public class RenameAttributesOperatorTest extends CrappyDBMSTestCase {

	protected Database database;

	protected StoredRelation assets;

	protected int numberOfTuples = 150;

	StoredRelationSchema innerSchema;

	protected ArrayList<String> oldAttributeNames;

	protected ArrayList<String> newAttributeNames;
	Transaction transaction;
	
	protected void setUp() throws Exception {
		super.setUp();

		database = CrappyDBMSTestCase.setUpCommonTestingDatabase();
		assets = database.getStoredRelationNamed("assets");
		transaction = database.newTransaction();
		innerSchema = assets.getSchema();
		CrappyDBMSTestCase.addRandomTuplesForAssetsRelation(numberOfTuples, assets,transaction);

		oldAttributeNames = new ArrayList<String>();
		oldAttributeNames.add("name");
		oldAttributeNames.add("value");
		newAttributeNames = new ArrayList<String>();
		newAttributeNames.add("assetName");
		newAttributeNames.add("assetValue");
	}
	
	public void testNew() {
		newAttributeNames.remove(1);
		try {
			 new RenameAttributesOperator(assets,oldAttributeNames,newAttributeNames);
			Assert.fail(" attributeName lists have different sizes; should have throw an exception");
		} catch (InvalidAttributeListsException e) {}
	}

	public void testRelationSchema() throws InvalidAttributeListsException {
	
		RenameAttributesOperator renameAttributesOperator = new RenameAttributesOperator(assets,oldAttributeNames,newAttributeNames);
		RenamedAttributesRelationSchema relationSchema = renameAttributesOperator.getRelationSchema();
		
		try {
			relationSchema.attributeNamed("date");
		} catch (InvalidAttributeNameException e) {
			Assert.fail("should have an attribute named date");
		}

		try {
			relationSchema.attributeNamed("assetName");
		} catch (InvalidAttributeNameException e) {
			Assert.fail("should have an attribute named assetName");
		}
		try {
			relationSchema.attributeNamed("assetValue");
		} catch (InvalidAttributeNameException e) {
			Assert.fail("should have an attribute named assetValue");
		}

		try {
			relationSchema.attributeNamed("name");
			Assert.fail("should NOT have an attribute named name");
		} catch (InvalidAttributeNameException e) {}
		try {
			relationSchema.attributeNamed("value");
			Assert.fail("should NOT have an attribute named value");
		} catch (InvalidAttributeNameException e) {}


	}
	

	
	public void testIterator() throws InvalidAttributeListsException{
		try{
		RenameAttributesOperator renameAttributesOperator = new RenameAttributesOperator(assets,oldAttributeNames,newAttributeNames);
		int tuples = 0;
		RelationIterator<Tuple> iterator = renameAttributesOperator.iterator(this.transaction);
		while (iterator.hasNext()){
			Tuple tuple = iterator.next();
			this.tupleTest(tuple,tuples);
			tuples++;
		}	
		Assert.assertEquals("Wrong number of total tuples",numberOfTuples,tuples);
	} catch (TransactionAbortedException e) {
		Assert.fail("Transaction should not abort: " + e.getMessage());
	}
	}
	
	/**
	 * @param tuple
	 */
	private void tupleTest(Tuple tuple,int tupleNumber) {
		try {
			Assert.assertEquals("names should match","juan"+tupleNumber, tuple.getFieldNamed("assetName").getValue());
			Assert.assertEquals("values should match",10, tuple.getFieldNamed("assetValue").getValue());
			Assert.assertEquals("dates should match","15/12/11", tuple.getFieldNamed("date").getValue());
			Assert.assertEquals("managers should match","pedrito", tuple.getFieldNamed("manager").getValue());
			
		} catch (InvalidAttributeNameException e) {
			Assert.fail("Attribute name is valid: "+e.getMessage());
		}
		
	}

	protected void tearDown() throws Exception {
		CrappyDBMSTestCase.checkDatabaseLocksAreFree(database);
		this.database.removeSelf();
		super.tearDown();
	}

}
