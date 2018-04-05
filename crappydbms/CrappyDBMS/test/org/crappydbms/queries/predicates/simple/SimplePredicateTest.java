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
package org.crappydbms.queries.predicates.simple;

import junit.framework.Assert;

import org.crappydbms.exceptions.CrappyDBMSError;
import org.crappydbms.exceptions.InvalidAttributeNameException;
import org.crappydbms.main.CrappyDBMSTestingError;
import org.crappydbms.queries.predicates.PredicateTest;
import org.crappydbms.queries.predicates.simple.factories.EqualsPredicateFactory;
import org.crappydbms.relations.fields.IntegerField;
import org.crappydbms.relations.fields.StringField;

/**
 * @author Facundo Manuel Quiroga
 * Dec 2, 2008
 * 
 */
public class SimplePredicateTest extends PredicateTest {
	
	protected SimplePredicate trueFieldFieldEqualsPredicate;
	protected SimplePredicate trueFieldAttributeEqualsPredicate;
	protected SimplePredicate trueAttributeFieldEqualsPredicate;
	protected SimplePredicate trueAttributeAttributeEqualsPredicate;
	
	protected SimplePredicate falseFieldFieldEqualsPredicate;
	protected SimplePredicate falseFieldAttributeEqualsPredicate;
	protected SimplePredicate falseAttributeFieldEqualsPredicate;
	protected SimplePredicate falseAttributeAttributeEqualsPredicate;
	
	protected SimplePredicate invalidFieldFieldEqualsPredicate;
	protected SimplePredicate invalidFieldAttributeEqualsPredicate;
	protected SimplePredicate invalidAttributeFieldEqualsPredicate;
	protected SimplePredicate invalidAttributeAttributeEqualsPredicate;
	
	protected void setUp() throws Exception {
		super.setUp();
		trueFieldFieldEqualsPredicate = EqualsPredicateFactory.newPredicate(IntegerField.valueOf(3), IntegerField.valueOf(3) );
		falseFieldFieldEqualsPredicate = EqualsPredicateFactory.newPredicate(IntegerField.valueOf(3), IntegerField.valueOf(5) );
		invalidFieldFieldEqualsPredicate = EqualsPredicateFactory.newPredicate(IntegerField.valueOf(3), StringField.valueOf("3") );
		
		trueFieldAttributeEqualsPredicate = EqualsPredicateFactory.newPredicate(IntegerField.valueOf(10), "value" );
		falseFieldAttributeEqualsPredicate = EqualsPredicateFactory.newPredicate(StringField.valueOf("pedrito2"), "manager" );
		invalidFieldAttributeEqualsPredicate = EqualsPredicateFactory.newPredicate(IntegerField.valueOf(3), "invalidAttributeName" );
		
		trueAttributeFieldEqualsPredicate = EqualsPredicateFactory.newPredicate("manager", StringField.valueOf("pedrito"));
		falseAttributeFieldEqualsPredicate = EqualsPredicateFactory.newPredicate("value", IntegerField.valueOf(5) );
		invalidAttributeFieldEqualsPredicate = EqualsPredicateFactory.newPredicate("invalidAttributeName", IntegerField.valueOf(3) );
		
		trueAttributeAttributeEqualsPredicate = EqualsPredicateFactory.newPredicate("name","name" );
		falseAttributeAttributeEqualsPredicate = EqualsPredicateFactory.newPredicate("name", "manager" );
		invalidAttributeAttributeEqualsPredicate = EqualsPredicateFactory.newPredicate("name", "invalidAttributeName");
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testIsTrueFor(){
		try{
		Assert.assertTrue(trueFieldFieldEqualsPredicate.isTrueFor(this.tuple));
		Assert.assertFalse(falseFieldFieldEqualsPredicate.isTrueFor(this.tuple));
		try{
			invalidFieldFieldEqualsPredicate.isTrueFor(this.tuple);
			Assert.fail("Invalid predicate for tuple, should throw an exception ");
		} catch (CrappyDBMSError e){
			
		}
		Assert.assertTrue(trueFieldAttributeEqualsPredicate.isTrueFor(this.tuple));
		Assert.assertFalse(falseFieldAttributeEqualsPredicate.isTrueFor(this.tuple));
		try{
			invalidFieldAttributeEqualsPredicate.isTrueFor(this.tuple);
			Assert.fail("Invalid predicate for tuple, should throw an exception ");
		} catch (CrappyDBMSError e){
			
		}
		Assert.assertTrue(trueAttributeFieldEqualsPredicate.isTrueFor(this.tuple));
		Assert.assertFalse(falseAttributeFieldEqualsPredicate.isTrueFor(this.tuple));
		try{
			invalidAttributeFieldEqualsPredicate.isTrueFor(this.tuple);
			Assert.fail("Invalid predicate for tuple, should throw an exception ");
		} catch (CrappyDBMSError e){
			
		}
		Assert.assertTrue(trueAttributeAttributeEqualsPredicate.isTrueFor(this.tuple));
		Assert.assertFalse(falseAttributeAttributeEqualsPredicate.isTrueFor(this.tuple));
		try{
			invalidAttributeAttributeEqualsPredicate.isTrueFor(this.tuple);
			Assert.fail("Invalid predicate for tuple, should throw an exception ");
		} catch (CrappyDBMSError e){
			
		}
		} catch(InvalidAttributeNameException e){
			throw new CrappyDBMSTestingError("Invalid attribute names");
		}
	}

}
