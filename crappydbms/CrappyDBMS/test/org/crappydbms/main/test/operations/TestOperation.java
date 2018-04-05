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
package org.crappydbms.main.test.operations;

import java.util.Random;

import org.crappydbms.main.CrappyDBMSTestingError;
import org.crappydbms.queries.predicates.Predicate;
import org.crappydbms.queries.predicates.TruePredicate;
import org.crappydbms.queries.predicates.simple.factories.EqualsPredicateFactory;
import org.crappydbms.relations.StoredRelation;
import org.crappydbms.relations.fields.Field;
import org.crappydbms.relations.fields.IntegerField;
import org.crappydbms.relations.fields.StringField;
import org.crappydbms.relations.schemas.attributes.Attribute;
import org.crappydbms.relations.schemas.attributes.AttributeType;
import org.crappydbms.relations.schemas.attributes.IntegerAttributeType;
import org.crappydbms.relations.schemas.attributes.StringAttributeType;
import org.crappydbms.transactions.Transaction;
import org.crappydbms.transactions.TransactionAbortedException;

/**
 * @author Facundo Manuel Quiroga
 * Jan 22, 2009
 * 
 */
public abstract class TestOperation {
	
	// TODO finish implementing subclasses 
	
	
	protected StoredRelation storedRelation;
	protected Transaction transaction;
	public TestOperation(StoredRelation storedRelation, Transaction transaction){
		this.setStoredRelation(storedRelation);
		this.setTransaction(transaction);
	}
	
	public abstract void perform() throws TransactionAbortedException;

	protected StoredRelation getStoredRelation() {
		return this.storedRelation;
	}

	protected void setStoredRelation(StoredRelation storedRelation) {
		this.storedRelation = storedRelation;
	}

	protected Transaction getTransaction() {
		return this.transaction;
	}

	protected void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	protected Predicate generateRandomPredicate(int maxTupleNumber, int numberOfTuples) {
		Attribute  attribute = storedRelation.getSchema().getAttribute(0);
		AttributeType attributeType = attribute.getType();
		int tupleNumber = new Random().nextInt(maxTupleNumber+1);
		Predicate predicate = new TruePredicate();
		for (int i = 0 ; i<numberOfTuples ; i++){
			Field field;
			if (attributeType == StringAttributeType.getInstance()){
				field = StringField.valueOf( Integer.valueOf(tupleNumber).toString() );
			} else if (attributeType == IntegerAttributeType.getInstance()){ 
				field = IntegerField.valueOf(tupleNumber);
			}else{
				throw new CrappyDBMSTestingError("Invalid attribute type " + attributeType);
			}
			predicate = predicate.or( EqualsPredicateFactory.newPredicate(field, attribute.getName()));
			tupleNumber++;
		}
		return predicate;
	}
	

}

