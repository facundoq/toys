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

import java.util.ArrayList;

import org.crappydbms.main.DatabaseStressTestUtils;
import org.crappydbms.relations.StoredRelation;
import org.crappydbms.relations.tuples.StoredTuple;
import org.crappydbms.transactions.Transaction;
import org.crappydbms.transactions.TransactionAbortedException;

/**
 * @author Facundo Manuel Quiroga
 * Jan 22, 2009
 * 
 */
public class TestAddOperation extends TestOperation {

	 protected int numberOfTuples;
	
	public TestAddOperation(StoredRelation storedRelation, Transaction transaction, int numberOfTuples) {
		super(storedRelation, transaction);
		this.setNumberOfTuples(numberOfTuples);
	}

	@Override
	public void perform() throws TransactionAbortedException {
		ArrayList<StoredTuple> tuples = DatabaseStressTestUtils.createRandomTuplesForRelation(this.getStoredRelation(),numberOfTuples);
			for (StoredTuple tuple : tuples){
				storedRelation.addTuple(tuple, this.getTransaction());
			}
	}

	protected int getNumberOfTuples() {
		return this.numberOfTuples;
	}

	protected void setNumberOfTuples(int numberOfTuples) {
		this.numberOfTuples = numberOfTuples;
	}



}
