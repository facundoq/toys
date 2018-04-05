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
package org.crappydbms.queries.operations;

import java.util.ArrayList;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.crappydbms.exceptions.InvalidAttributeNameException;
import org.crappydbms.main.CrappyDBMSTestCase;
import org.crappydbms.main.CrappyDBMSTestingError;
import org.crappydbms.main.Database;
import org.crappydbms.queries.predicates.Predicate;
import org.crappydbms.queries.predicates.TruePredicate;
import org.crappydbms.queries.predicates.simple.SimplePredicate;
import org.crappydbms.queries.predicates.simple.factories.EqualsPredicateFactory;
import org.crappydbms.relations.RelationIterator;
import org.crappydbms.relations.StoredRelation;
import org.crappydbms.relations.fields.Field;
import org.crappydbms.relations.fields.StringField;
import org.crappydbms.relations.tuples.StoredTuple;
import org.crappydbms.relations.tuples.Tuple;
import org.crappydbms.transactions.Transaction;
import org.crappydbms.transactions.TransactionAbortedException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Facundo Manuel Quiroga Dec 19, 2008
 */
public class DeleteOperationTest extends TestCase {
	protected Database database;

	protected StoredRelation assets;

	protected int numberOfTuples = 320;

	Transaction transaction;

	@Before
	public void setUp() throws Exception {
		database = CrappyDBMSTestCase.setUpCommonTestingDatabase();
		assets = database.getStoredRelationNamed("assets");
		this.transaction = database.newTransaction();
		ArrayList<StoredTuple> tuples = CrappyDBMSTestCase.createRandomTuplesForAssetsRelation(this.numberOfTuples, assets.getSchema());
		for (Tuple tuple : tuples) {
			assets.addTuple(tuple, this.transaction);

		}
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		CrappyDBMSTestCase.checkDatabaseLocksAreFree(database);
		assets = null;
		database.removeSelf();
		database = null;
		CrappyDBMSTestCase.deleteTestDirectory();
	}

	/**
	 * Test method for {@link org.crappydbms.queries.operators.selection.SelectionOperator#iterator()} .
	 */
	@Test
	public void testRemoveOne() {
		try {
			SimplePredicate equalsPredicate = EqualsPredicateFactory.newPredicate("name", StringField.valueOf("juan1"));
			DeleteOperation deleteOperation = new DeleteOperation(this.assets, equalsPredicate);

			try {
				Field deleted = deleteOperation.perform(this.transaction);
				Assert.assertEquals("Should have deleted one tuple", 1, deleted.getValue());

				int count = 0;
				RelationIterator<Tuple> iterator = this.assets.iterator(this.transaction);
				while (iterator.hasNext()) {
					iterator.next();
					count++;
				}

				Assert.assertEquals("Should have deleted one tuple ", this.numberOfTuples - 1, count);

			} catch (InvalidAttributeNameException e) {
				throw new CrappyDBMSTestingError("Attribute name exists");
			}
			this.transaction.commit();
		} catch (TransactionAbortedException e) {
			Assert.fail("Transaction should not abort: " + e.getMessage());
		}
	}

	public void testRemoveAll() {

		try {
			Predicate equalsPredicate = new TruePredicate();
			DeleteOperation deleteOperation = new DeleteOperation(this.assets, equalsPredicate);

			try {
				Field deleted = deleteOperation.perform(this.transaction);
				int count = 0;
				RelationIterator<Tuple> iterator = this.assets.iterator(this.transaction);
				while (iterator.hasNext()) {
					iterator.next();
					count++;
				}
				Assert.assertEquals("Wrong remaining tuples ", 0, count);
				Assert.assertEquals("Wrong deleted tuples ", this.numberOfTuples, deleted.getValue());

				transaction.commit();
				// test again after making sure all changes were written to disk
				transaction = database.newTransaction();
				count = 0;
				iterator = this.assets.iterator(this.transaction);
				while (iterator.hasNext()) {
				  iterator.next();
					count++;
				}
				Assert.assertEquals("Wrong remaining tuples ", 0, count);

			} catch (InvalidAttributeNameException e) {
				throw new CrappyDBMSTestingError("Attribute name exists");
			}
			this.transaction.commit();
		} catch (TransactionAbortedException e) {
			Assert.fail("Transaction should not abort: " + e.getMessage());
		}
	}

	public void testRemoveNone() {
		try {

			Predicate predicate = new TruePredicate().not();
			DeleteOperation deleteOperation = new DeleteOperation(this.assets, predicate);

			try {
				Field deleted = deleteOperation.perform(this.transaction);
				int count = 0;
				RelationIterator<Tuple> iterator = this.assets.iterator(this.transaction);
				while (iterator.hasNext()) {
					iterator.next();
					count++;
				}
				Assert.assertEquals("Wrong remaining tuples ", this.numberOfTuples, count);
				Assert.assertEquals("Wrong deleted tuples ", 0, deleted.getValue());
				transaction.commit();
				// test again after making sure all changes were written to disk
				transaction = database.newTransaction();
				count = 0;

				iterator = this.assets.iterator(this.transaction);
				while (iterator.hasNext()) {
					iterator.next();
					count++;
				}
				Assert.assertEquals("Wrong remaining tuples ", this.numberOfTuples, count);

			} catch (InvalidAttributeNameException e) {
				throw new CrappyDBMSTestingError("Attribute name exists");
			}
			this.transaction.commit();

		} catch (TransactionAbortedException e) {
			Assert.fail("Transaction should not abort: " + e.getMessage());
		}
	}

}
