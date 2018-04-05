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

import org.crappydbms.main.CrappyDBMSTestingError;
import org.crappydbms.queries.operators.join.RelationsWithSameNameException;
import org.crappydbms.queries.operators.join.natural.MatchingNonEqualAttributesException;
import org.crappydbms.queries.operators.join.natural.NaturalJoinOperator;
import org.crappydbms.queries.operators.rename.RenameRelationOperator;
import org.crappydbms.relations.RelationIterator;
import org.crappydbms.relations.StoredRelation;
import org.crappydbms.relations.tuples.Tuple;
import org.crappydbms.transactions.Transaction;
import org.crappydbms.transactions.TransactionAbortedException;

/**
 * @author Facundo Manuel Quiroga
 * Jan 22, 2009
 * 
 */
public class TestJoinOperation extends TestOperation {

	/**
	 * @param storedRelation
	 * @param transaction
	 */
	public TestJoinOperation(StoredRelation storedRelation, Transaction transaction) {
		super(storedRelation, transaction);
	}

	@Override
	public void perform() throws TransactionAbortedException {
		RenameRelationOperator renamedRelation = new RenameRelationOperator(this.getStoredRelation(),"newFakeName");
		try {
			NaturalJoinOperator naturalJoinOperator = new NaturalJoinOperator(this.getStoredRelation(),renamedRelation);
			RelationIterator<Tuple> iterator = naturalJoinOperator.iterator(transaction);
			while (iterator.hasNext()){
				iterator.next();
			}
		} catch (MatchingNonEqualAttributesException e) {
			throw new CrappyDBMSTestingError("MatchingNonEqualAttributesException "+e.getMessage());
		} catch (RelationsWithSameNameException e) {
			throw new CrappyDBMSTestingError("RelationsWithSameNameException "+e.getMessage());
		}
		 
	}

}
