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
package org.crappydbms.queries.operators.join.cartesianproduct;

import junit.framework.Assert;

import org.crappydbms.exceptions.InvalidAttributeNameException;
import org.crappydbms.main.CrappyDBMSTestCase;
import org.crappydbms.main.Database;
import org.crappydbms.queries.operators.join.RelationsWithSameNameException;
import org.crappydbms.relations.RelationIterator;
import org.crappydbms.relations.StoredRelation;
import org.crappydbms.relations.fields.StringField;
import org.crappydbms.relations.schemas.StoredRelationSchema;
import org.crappydbms.relations.tuples.Tuple;
import org.crappydbms.transactions.Transaction;
import org.crappydbms.transactions.TransactionAbortedException;

/**
 * @author Facundo Manuel Quiroga Dec 11, 2008
 */
public class JoinOperatorTest extends CrappyDBMSTestCase {

	protected StoredRelationSchema assetsRelationSchema;
	protected StoredRelationSchema customersRelationSchema;

	protected Database database;
	protected StoredRelation assets;
	protected StoredRelation customers;
	Transaction transaction;
	protected int numberOfAssetTuples = 100;
	protected int numberOfCustomerTuples = 120;

	protected void setUp() throws Exception {

		database = CrappyDBMSTestCase.setUpCommonTestingDatabase();
		assets = database.getStoredRelationNamed("assets");
		this.transaction = database.newTransaction();
		assetsRelationSchema = assets.getSchema();
		CrappyDBMSTestCase.addRandomTuplesForAssetsRelation(numberOfAssetTuples, assets, transaction);

		customers = database.getStoredRelationNamed("customers");
		customersRelationSchema = customers.getSchema();
		CrappyDBMSTestCase.addRandomTuplesForCustomersRelation(numberOfCustomerTuples, customers, transaction);
	}

	public void testNew() {
		try {
			new JoinOperator(assets, customers);
		} catch (RelationsWithSameNameException e) {
			Assert.fail("Relations do not have the same name");
		}
	}

	public void testIterator() {
		try {
			JoinOperator joinOperator;
			try {
				joinOperator = new JoinOperator(assets, customers);
				int expectedTuples = numberOfAssetTuples * numberOfCustomerTuples;
				int tuples = 0;
				RelationIterator<Tuple> iterator = joinOperator.iterator(this.transaction);

				for (int i = 0; i < numberOfAssetTuples; i++) {
					for (int j = 0; j < numberOfCustomerTuples; j++) {
						Assert.assertTrue("should have " + expectedTuples + "tuples!, has: " + tuples, iterator.hasNext());
						tuples++;
						Tuple tuple = iterator.next();

						try {
							StringField expectedName = StringField.valueOf("juan" + i);
							Assert.assertEquals("Wrong atribute 'assets.name' ", expectedName, tuple.getFieldNamed("assets.name"));
						} catch (InvalidAttributeNameException e) {
							Assert.fail("Should have an attribute named 'assets.name' ");
						}

						try {
							StringField expectedCustomerName = StringField.valueOf("juan" + j);
							Assert.assertEquals("Wrong atribute 'customers.customerName' ", expectedCustomerName, tuple.getFieldNamed("customers.customerName"));
						} catch (InvalidAttributeNameException e) {
							Assert.fail("Should have an attribute named 'customers.customerName' ");
						}
					}
				}

				Assert.assertFalse("Should not have more tuples", iterator.hasNext());

			} catch (RelationsWithSameNameException e) {
				Assert.fail("Relations do not have the same name");
			}
		} catch (TransactionAbortedException e) {
			Assert.fail("Transaction should not abort: " + e.getMessage());
		}

	}

	protected void tearDown() throws Exception {
		super.tearDown();
		CrappyDBMSTestCase.checkDatabaseLocksAreFree(database);
	}

}
