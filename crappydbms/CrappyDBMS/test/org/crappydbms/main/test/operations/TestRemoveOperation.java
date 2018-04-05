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
import org.crappydbms.queries.operations.DeleteOperation;
import org.crappydbms.queries.predicates.Predicate;
import org.crappydbms.relations.StoredRelation;
import org.crappydbms.transactions.Transaction;
import org.crappydbms.transactions.TransactionAbortedException;

/**
 * @author Facundo Manuel Quiroga
 * Jan 22, 2009
 * 
 */
public class TestRemoveOperation extends TestOperation {

	protected int numberOfTuples;
	/**
	 * @param storedRelation
	 * @param transaction
	 * @param numberOfTuples
	 */
	public TestRemoveOperation(StoredRelation storedRelation, Transaction transaction, int numberOfTuples) {
		super(storedRelation,transaction);
		this.setNumberOfTuples(numberOfTuples);
	}

	@Override
	public void perform() throws TransactionAbortedException {
		Predicate predicate = this.generateRandomPredicate(10,this.getNumberOfTuples());
		DeleteOperation deleteOperation = new DeleteOperation(storedRelation,predicate);
		try {
			deleteOperation.perform(this.getTransaction());
		} catch (InvalidAttributeNameException e) {
			throw new CrappyDBMSTestingError("InvalidAttributeNameException, message"+e.getMessage());
		}
		
	}

	protected int getNumberOfTuples() {
		return this.numberOfTuples;
	}

	protected void setNumberOfTuples(int numberOfTuples) {
		this.numberOfTuples = numberOfTuples;
	}


}
