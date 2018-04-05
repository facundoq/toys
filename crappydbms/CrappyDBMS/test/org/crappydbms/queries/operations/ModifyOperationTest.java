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
public class ModifyOperationTest extends TestCase {

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
	public void testModifyOne() {
		try {
			SimplePredicate equalsPredicate = EqualsPredicateFactory.newPredicate("name", StringField.valueOf("juan1"));
			Modification modification = new ValueModification("manager", StringField.valueOf("pepito"));
			ModifyOperation modifyOperation = new ModifyOperation(this.assets, equalsPredicate, modification);

			try {
				Field modified;
				modified = modifyOperation.perform(this.transaction);

				Assert.assertEquals("Should have modified one tuple", 1, modified.getValue());

				RelationIterator<Tuple> iterator = this.assets.iterator(this.transaction);
				while (iterator.hasNext()) {
					Tuple tuple = iterator.next();
					Assert.assertEquals("Should have 4 fields", 4, tuple.getFields().size());
					if (tuple.getFieldNamed("name").getValue().equals("juan1")) {
						Assert.assertEquals("Manager of modified tuple should be pepito", "pepito", tuple.getFieldNamed("manager").getValue());
					} else {
						Assert.assertEquals("Manager of non-modified tuple should still be pedrito", "pedrito", tuple.getFieldNamed("manager").getValue());
					}
				}

				transaction.commit();
				// test again after making sure all changes were written to disk
				transaction = database.newTransaction();
				
				iterator = this.assets.iterator(this.transaction);
				while (iterator.hasNext()) {
					Tuple tuple = iterator.next();
					Assert.assertEquals("Should have 4 fields", 4, tuple.getFields().size());
					if (tuple.getFieldNamed("name").getValue().equals("juan1")) {
						Assert.assertEquals("Manager of modified tuple should be pepito", "pepito", tuple.getFieldNamed("manager").getValue());
					} else {
						Assert.assertEquals("Manager of non-modified tuple should still be pedrito", "pedrito", tuple.getFieldNamed("manager").getValue());
					}
				}
			} catch (InvalidAttributeNameException e) {
				throw new CrappyDBMSTestingError("Attribute name exists");
			} catch (TransactionAbortedException e) {
				throw new CrappyDBMSTestingError("Transaction error: " + e.getMessage());
			}
			this.transaction.commit();
		} catch (TransactionAbortedException e) {
			Assert.fail("Transaction should not abort: " + e.getMessage());
		}
	}

	public void testModifyAll() {
		try {
			Predicate truePredicate = new TruePredicate();
			Modification modification = new ValueModification("manager", StringField.valueOf("pepito"));
			ModifyOperation modifyOperation = new ModifyOperation(this.assets, truePredicate, modification);

			try {
				Field modified = modifyOperation.perform(this.transaction);
				Assert.assertEquals("Should have modified all tuples", this.numberOfTuples, modified.getValue());
				RelationIterator<Tuple> iterator = this.assets.iterator(this.transaction);
				while (iterator.hasNext()) {
					Tuple tuple = iterator.next();
					Assert.assertEquals("Should have 4 fields", 4, tuple.getFields().size());
					Assert.assertEquals("Manager of modified tuple should be pepito", "pepito", tuple.getFieldNamed("manager").getValue());
				}

				transaction.commit();
				// test again after making sure all changes were written to disk
				transaction = database.newTransaction();
				iterator = this.assets.iterator(this.transaction);
				while (iterator.hasNext()) {
					Tuple tuple = iterator.next();
					Assert.assertEquals("Should have 4 fields", 4, tuple.getFields().size());
					Assert.assertEquals("Manager of modified tuple should be pepito", "pepito", tuple.getFieldNamed("manager").getValue());
				}

			} catch (InvalidAttributeNameException e) {
				throw new CrappyDBMSTestingError("Attribute name exists");
			} catch (TransactionAbortedException e) {
				throw new CrappyDBMSTestingError("Transaction error: " + e.getMessage());
			}
			this.transaction.commit();
		} catch (TransactionAbortedException e) {
			Assert.fail("Transaction should not abort: " + e.getMessage());
		}
	}

	public void testModifyNone() {
		try {
			Predicate predicate = new TruePredicate().not();
			Modification modification = new ValueModification("manager", StringField.valueOf("pepito"));
			ModifyOperation modifyOperation = new ModifyOperation(this.assets, predicate, modification);

			try {
				Field modified = modifyOperation.perform(this.transaction);
				Assert.assertEquals("Should have modified no tuples", 0, modified.getValue());
				RelationIterator<Tuple> iterator = this.assets.iterator(this.transaction);
				while (iterator.hasNext()) {
					Tuple tuple = iterator.next();
					Assert.assertEquals("Should have 4 fields", 4, tuple.getFields().size());
					Assert.assertEquals("Manager of non-modified tuple should still be pedrito", "pedrito", tuple.getFieldNamed("manager").getValue());
				}

				transaction.commit();
				// test again after making sure all changes were written to disk
				transaction = database.newTransaction();
				iterator = this.assets.iterator(this.transaction);
				while (iterator.hasNext()) {
					Tuple tuple = iterator.next();
					Assert.assertEquals("Should have 4 fields", 4, tuple.getFields().size());
					Assert.assertEquals("Manager of non-modified tuple should still be pedrito", "pedrito", tuple.getFieldNamed("manager").getValue());
				}

			} catch (InvalidAttributeNameException e) {
				throw new CrappyDBMSTestingError("Attribute name exists");
			} catch (TransactionAbortedException e) {
				throw new CrappyDBMSTestingError("Transaction error: " + e.getMessage());
			}
			this.transaction.commit();
		} catch (TransactionAbortedException e) {
			Assert.fail("Transaction should not abort: " + e.getMessage());
		}

	}

}
