/* CrappyDBMS, a top-down 2d action-rpg game written in Java.
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

package org.crappydbms.queries.operators.selection;

import java.util.ArrayList;

import junit.framework.Assert;

import org.crappydbms.exceptions.InvalidAttributeNameException;
import org.crappydbms.main.CrappyDBMSTestCase;
import org.crappydbms.main.CrappyDBMSTestingError;
import org.crappydbms.main.Database;
import org.crappydbms.queries.predicates.simple.SimplePredicate;
import org.crappydbms.queries.predicates.simple.factories.EqualsPredicateFactory;
import org.crappydbms.relations.RelationIterator;
import org.crappydbms.relations.StoredRelation;
import org.crappydbms.relations.fields.IntegerField;
import org.crappydbms.relations.fields.StringField;
import org.crappydbms.relations.tuples.StoredTuple;
import org.crappydbms.relations.tuples.Tuple;
import org.crappydbms.transactions.Transaction;
import org.crappydbms.transactions.TransactionAbortedException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Facundo Quiroga Creation date: Nov 26, 2008 7:59:19 PM
 */
public class SelectionOperatorTest extends CrappyDBMSTestCase {

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
	 * Test method for {@link org.crappydbms.queries.operators.selection.SelectionOperator#getSchema()}.
	 */
	@Test
	public void testGetSchema() {
		SimplePredicate equalsPredicate = EqualsPredicateFactory.newPredicate("name", StringField.valueOf("juan1"));
		SelectionOperator selectionOperator = new SelectionOperator(this.assets, equalsPredicate);
		Assert.assertTrue("Relation schemas should match ", selectionOperator.getSchema().equals(assets.getSchema()));
	}

	/**
	 * Test method for {@link org.crappydbms.queries.operators.selection.SelectionOperator#iterator()}.
	 */
	@Test
	public void testIteratorOneMatch() {
		try {
			SimplePredicate equalsPredicate = EqualsPredicateFactory.newPredicate("name", StringField.valueOf("juan1"));
			SelectionOperator selectionOperator = new SelectionOperator(this.assets, equalsPredicate);
			RelationIterator<Tuple> tupleIterator = selectionOperator.iterator(this.transaction);
			Assert.assertTrue("Selection should not have 0 tuples", tupleIterator.hasNext());
			Tuple tuple = tupleIterator.next();
			Assert.assertFalse("Selection should not have more than 1 tuples", tupleIterator.hasNext());
			StringField expected = StringField.valueOf("juan1");
			StringField actual;
			try {
				actual = (StringField) tuple.getFieldNamed("name");
			} catch (InvalidAttributeNameException e) {
				throw new CrappyDBMSTestingError("There should be aan attribute named 'name' ");
			}
			Assert.assertTrue("Should have name = juan1", expected.equals(actual));
		} catch (TransactionAbortedException e) {
			Assert.fail("Transaction should not abort: " + e.getMessage());
		}
	}

	@Test
	public void testIteratorNoMatches() {
		try {
			SimplePredicate equalsPredicate = EqualsPredicateFactory.newPredicate("name", StringField.valueOf("invalidName"));
			SelectionOperator selectionOperator = new SelectionOperator(this.assets, equalsPredicate);
			RelationIterator<Tuple> tupleIterator = selectionOperator.iterator(this.transaction);
			Assert.assertFalse("Selection should not have tuples", tupleIterator.hasNext());
		} catch (TransactionAbortedException e) {
			Assert.fail("Transaction should not abort: " + e.getMessage());
		}
	}

	public void testIteratorAllMatches() {
		try {
			SimplePredicate equalsPredicate = EqualsPredicateFactory.newPredicate("value", IntegerField.valueOf(10));
			RelationIterator<Tuple> originalIterator = this.assets.iterator(this.transaction);
			SelectionOperator selectionOperator = new SelectionOperator(this.assets, equalsPredicate);
			int tuples = 0;

			RelationIterator<Tuple> iterator = selectionOperator.iterator(this.transaction);
			while (iterator.hasNext()) {
				Tuple tuple = iterator.next();
				tuples++;
				Assert.assertTrue("Tuples should match", tuple.equals(originalIterator.next()));
			}
			Assert.assertEquals("Selection should have " + this.numberOfTuples + " tuples", this.numberOfTuples, tuples);
		} catch (TransactionAbortedException e) {
			Assert.fail("Transaction should not abort: " + e.getMessage());
		}
	}

	public void testIteratorComposite() {
		try {
			SimplePredicate firstEqualsPredicate = EqualsPredicateFactory.newPredicate("value", IntegerField.valueOf(10));
			SimplePredicate secondEqualsPredicate = EqualsPredicateFactory.newPredicate("manager", StringField.valueOf("pedrito"));
			RelationIterator<Tuple> originalIterator = this.assets.iterator(this.transaction);
			SelectionOperator firstSelectionOperator = new SelectionOperator(this.assets, firstEqualsPredicate);
			SelectionOperator secondSelectionOperator = new SelectionOperator(firstSelectionOperator, secondEqualsPredicate);
			int tuples = 0;
			RelationIterator<Tuple> iterator = secondSelectionOperator.iterator(this.transaction);
			while (iterator.hasNext()) {
				Tuple tuple = iterator.next();
				tuples++;
				Assert.assertTrue("Tuples should match", tuple.equals(originalIterator.next()));
			}
			Assert.assertEquals("Selection should have " + this.numberOfTuples + " tuples", this.numberOfTuples, tuples);
		} catch (TransactionAbortedException e) {
			Assert.fail("Transaction should not abort: " + e.getMessage());
		}
	}

}
