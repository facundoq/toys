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
package org.crappydbms.queries.operators.join.natural;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.crappydbms.main.CrappyDBMSTestCase;
import org.crappydbms.main.Database;
import org.crappydbms.queries.operators.join.RelationsWithSameNameException;
import org.crappydbms.queries.operators.rename.RenameAttributesOperator;
import org.crappydbms.relations.RelationIterator;
import org.crappydbms.relations.StoredRelation;
import org.crappydbms.relations.schemas.StoredRelationSchema;
import org.crappydbms.relations.tuples.Tuple;
import org.crappydbms.transactions.Transaction;
import org.crappydbms.transactions.TransactionAbortedException;

/**
 * @author Facundo Manuel Quiroga
 * Dec 12, 2008
 * 
 */
public class NaturalJoinOperatorTest extends CrappyDBMSTestCase {
	
	protected StoredRelationSchema assetsRelationSchema;
	protected StoredRelationSchema customersRelationSchema;

	protected Database database;
	protected StoredRelation assets;
	protected StoredRelation customers;
	protected RenameAttributesOperator renamedCustomers;
	Transaction transaction;
	protected int numberOfAssetTuples=100;
	protected int numberOfCustomerTuples=120;
	
	protected void setUp() throws Exception {
		
		database = CrappyDBMSTestCase.setUpCommonTestingDatabase();
		assets = database.getStoredRelationNamed("assets");
		assetsRelationSchema = assets.getSchema();
		this.transaction = database.newTransaction();
		CrappyDBMSTestCase.addRandomTuplesForAssetsRelation(numberOfAssetTuples, assets,this.transaction);
		
		customers = database.getStoredRelationNamed("customers");
		customersRelationSchema = customers.getSchema();
		CrappyDBMSTestCase.addRandomTuplesForCustomersRelation(numberOfCustomerTuples, customers,this.transaction);
		List<String> oldAttributeNames = new ArrayList<String>();
		List<String> newAttributeNames = new ArrayList<String>();
		oldAttributeNames.add("customerName");
		newAttributeNames.add("name");
		this.renamedCustomers = new RenameAttributesOperator(customers,oldAttributeNames,newAttributeNames);
	}
	
	public void testNew(){
		try {
			new NaturalJoinOperator(assets,renamedCustomers);
		} catch (RelationsWithSameNameException e) {
			Assert.fail("Relations do not have the same name");
		} catch (MatchingNonEqualAttributesException e) {
			Assert.fail("Relations do not have attributes with the same name but different type");
		}
	}
	
	public void testIterator() throws MatchingNonEqualAttributesException, RelationsWithSameNameException{
		try{
			NaturalJoinOperator naturalJoinOperator = new NaturalJoinOperator(assets,renamedCustomers);
			int tupleCount = 0;
			RelationIterator<Tuple> iterator = naturalJoinOperator.iterator(this.transaction);
			while (iterator.hasNext()){
				Tuple tuple = iterator.next();
				tupleCount++;
				Assert.assertEquals("Wrong number of fields",6,tuple.getFields().size());
			}
			Assert.assertEquals("Operator should return as many tuples as ",this.numberOfAssetTuples, tupleCount);
		} catch (TransactionAbortedException e) {
			Assert.fail("Transaction should not abort: " + e.getMessage());
		}
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		CrappyDBMSTestCase.checkDatabaseLocksAreFree(database);
	}
	

}
