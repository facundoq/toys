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
package org.crappydbms.queries.operators.projection;

import java.util.ArrayList;

import junit.framework.Assert;

import org.crappydbms.exceptions.InvalidAttributeNameException;
import org.crappydbms.main.CrappyDBMSTestCase;
import org.crappydbms.main.Database;
import org.crappydbms.relations.RelationIterator;
import org.crappydbms.relations.StoredRelation;
import org.crappydbms.relations.fields.IntegerField;
import org.crappydbms.relations.fields.StringField;
import org.crappydbms.relations.schemas.StoredRelationSchema;
import org.crappydbms.relations.tuples.Tuple;
import org.crappydbms.transactions.Transaction;
import org.crappydbms.transactions.TransactionAbortedException;

/**
 * @author Facundo Manuel Quiroga
 * Dec 8, 2008
 * 
 */
public class ProjectionOperatorTest extends CrappyDBMSTestCase {
	
	protected Database database;
	
	protected StoredRelation assets;
	protected int numberOfTuples = 300;
	
	StoredRelationSchema innerSchema;
	protected ArrayList<String> removedAttributeNamesName;
	protected ArrayList<String> removedAttributeNamesValue;
	Transaction transaction;
	protected void setUp() throws Exception {
		super.setUp();
		
		database = CrappyDBMSTestCase.setUpCommonTestingDatabase();
		assets = database.getStoredRelationNamed("assets");
		this.transaction = database.newTransaction();
		innerSchema = assets.getSchema();	
		CrappyDBMSTestCase.addRandomTuplesForAssetsRelation(numberOfTuples, assets,this.transaction);
		
		removedAttributeNamesName = new ArrayList<String>();
		removedAttributeNamesName.add("name");

	}
	
	public void testIterator(){
		try{
		ProjectedRelationSchema projectedRelationSchema = new ProjectedRelationSchema(innerSchema,removedAttributeNamesName);

		ProjectionOperator projectionOperator = new ProjectionOperator(assets,removedAttributeNamesName);
		int tupleCount = 0;
		RelationIterator<Tuple> iterator = projectionOperator.iterator(this.transaction);
		while (iterator.hasNext()){
			Tuple tuple = iterator.next();
			tupleCount++;
			Assert.assertTrue("Relation schemas should be equal",tuple.getRelationSchema().equals(projectedRelationSchema));
			this.doTestTuple(tuple);
		}
		
		Assert.assertEquals("Should have equal number of tuples",numberOfTuples,tupleCount);
	} catch (TransactionAbortedException e) {
		Assert.fail("Transaction should not abort: " + e.getMessage());
	}
	}

	/**
	 * @param tuple
	 */
	private void doTestTuple(Tuple projectedTuple) {
		Assert.assertTrue("Tuples should be instances of projectedTuple",ProjectedTuple.class.isInstance(projectedTuple));		
		try {
			projectedTuple.getFieldNamed("name");
			Assert.fail("name is an invalid field!");
		} catch (InvalidAttributeNameException e) {
		}
		try {
			 IntegerField value = (IntegerField) projectedTuple.getFieldNamed("value");
			 Assert.assertEquals("value should be 10", 10 ,value.getValue().intValue());
		} catch (InvalidAttributeNameException e) {
			Assert.fail("value, date and manager attributes are valid for projectedRelationTuple");
		}
		try {
			 StringField date = (StringField) projectedTuple.getFieldNamed("date");
			 Assert.assertEquals("Dates should be 15/12/11","15/12/11",date.getValue() );
		} catch (InvalidAttributeNameException e) {
			Assert.fail("value, date and manager attributes are valid for projectedRelationTuple");
		}
		try {
			StringField manager = (StringField) projectedTuple.getFieldNamed("manager");
			 Assert.assertEquals("Manager should be pedrito","pedrito",manager.getValue() );
		} catch (InvalidAttributeNameException e) {
			Assert.fail("value, date and manager attributes are valid for projectedRelationTuple");
		}
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		CrappyDBMSTestCase.checkDatabaseLocksAreFree(database);
	}

}
