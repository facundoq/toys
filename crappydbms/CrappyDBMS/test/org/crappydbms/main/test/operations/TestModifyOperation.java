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





import org.crappydbms.exceptions.InvalidAttributeNameException;
import org.crappydbms.main.CrappyDBMSTestingError;
import org.crappydbms.queries.operations.Modification;
import org.crappydbms.queries.operations.ModifyOperation;
import org.crappydbms.queries.operations.ValueModification;
import org.crappydbms.queries.predicates.Predicate;
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
public class TestModifyOperation extends TestOperation {

	protected int numberOfTuples;
	/**
	 * @param storedRelation
	 * @param transaction
	 * @param numberOfTuples 
	 */
	public TestModifyOperation(StoredRelation storedRelation, Transaction transaction, int numberOfTuples) {
		super(storedRelation, transaction);
		this.setNumberOfTuples(numberOfTuples);
	}

	@Override
	public void perform() throws TransactionAbortedException {
		Predicate predicate = this.generateRandomPredicate(10,this.getNumberOfTuples());
		Modification modification = this.generateRandomModification(this.getStoredRelation());
		ModifyOperation modifyOperation = new ModifyOperation(this.getStoredRelation(),predicate,modification);
		try {
			modifyOperation.perform(this.getTransaction());
		} catch (InvalidAttributeNameException e) {
			throw new CrappyDBMSTestingError("InvalidAttributeNameException "+e.getMessage());
		}
		
	}

	/**
	 * @param storedRelation
	 * @return
	 */
	protected Modification generateRandomModification(StoredRelation storedRelation) {
		Attribute attribute = storedRelation.getSchema().getAttribute(0);
		AttributeType attributeType = attribute.getType();
		Field field;
		if (attributeType == StringAttributeType.getInstance()){
			field = StringField.valueOf( Integer.valueOf(10).toString() );
		} else if (attributeType == IntegerAttributeType.getInstance()){ 
			field = IntegerField.valueOf(10);
		}else{
			throw new CrappyDBMSTestingError("Invalid attribute type " + attributeType);
		}
		Modification modification = new ValueModification(attribute.getName(),field);
		return modification;
	}

	protected int getNumberOfTuples() {
		return this.numberOfTuples;
	}

	protected void setNumberOfTuples(int numberOfTuples) {
		this.numberOfTuples = numberOfTuples;
	}



}
