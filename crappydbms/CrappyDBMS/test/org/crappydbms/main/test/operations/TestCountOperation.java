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

import org.crappydbms.queries.aggregates.CountAggregate;
import org.crappydbms.relations.StoredRelation;
import org.crappydbms.transactions.Transaction;
import org.crappydbms.transactions.TransactionAbortedException;

/**
 * @author Facundo Manuel Quiroga
 * Jan 22, 2009
 * 
 */
public class TestCountOperation extends TestOperation {

	/**
	 * @param storedRelation
	 * @param transaction
	 */
	public TestCountOperation(StoredRelation storedRelation, Transaction transaction) {
		super(storedRelation, transaction);
	}
	@Override
	public void perform() throws TransactionAbortedException {
		CountAggregate countAggregate = new CountAggregate(this.getStoredRelation(),this.getTransaction());
		countAggregate.calculate();
		
	}

}
