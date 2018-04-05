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
package org.crappydbms.queries.aggregates;

import org.crappydbms.relations.Relation;
import org.crappydbms.relations.RelationIterator;
import org.crappydbms.relations.tuples.Tuple;
import org.crappydbms.transactions.Transaction;
import org.crappydbms.transactions.TransactionAbortedException;

/**
 * @author Facundo Manuel Quiroga
 * Jan 9, 2009
 * 
 */
public class CountAggregate implements Aggregate {

	protected Relation relation;
	protected Transaction transaction;
	
	public CountAggregate(Relation relation, Transaction transaction){
		this.setRelation(relation);
		this.setTransaction(transaction);
	}
	@Override
	public int calculate() throws TransactionAbortedException {
		RelationIterator<Tuple> iterator = this.getRelation().iterator(this.getTransaction());
		int count = 0;
		while (iterator.hasNext()){
			iterator.next();
			count++;
		}
		
		return count;
	}
	protected Relation getRelation() {
		return this.relation;
	}
	protected void setRelation(Relation relation) {
		this.relation = relation;
	}
	protected Transaction getTransaction() {
		return this.transaction;
	}
	protected void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}
	
}
