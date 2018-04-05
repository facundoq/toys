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
package org.crappydbms.queries.operators.join.cartesianproduct;

import java.util.NoSuchElementException;

import org.crappydbms.queries.operators.join.TuplesAndSchemasMatchException;
import org.crappydbms.relations.IteratingException;
import org.crappydbms.relations.Relation;
import org.crappydbms.relations.RelationIterator;
import org.crappydbms.relations.tuples.Tuple;
import org.crappydbms.transactions.Transaction;
import org.crappydbms.transactions.TransactionAbortedException;

/**
 * @author Facundo Manuel Quiroga Dec 9, 2008
 * 
 */
public class JoinOperatorIterator implements RelationIterator<Tuple> {

	protected Relation firstRelation;

	protected Relation secondRelation;

	protected JoinRelationSchema joinRelationSchema;

	protected RelationIterator<Tuple> firstRelationIterator;

	protected RelationIterator<Tuple> secondRelationIterator;

	protected boolean iteratingOverSecondRelation;

	protected boolean noMoreTuples;

	protected Tuple firstRelationTuple;
	
	protected Transaction transaction;
	/**
	 * @param firstRelation
	 * @param secondRelation
	 * @param joinRelationSchema
	 * @throws TransactionAbortedException 
	 */
	public JoinOperatorIterator(Relation firstRelation, Relation secondRelation, JoinRelationSchema joinRelationSchema,Transaction transaction) throws TransactionAbortedException {
		this.setTransaction(transaction);
		this.setFirstRelation(firstRelation);
		this.setSecondRelation(secondRelation);
		this.setJoinRelationSchema(joinRelationSchema);
		this.setFirstRelationIterator(firstRelation.iterator(transaction));
		this.firstRelationTuple = null;
		this.iteratingOverSecondRelation = false;
		this.noMoreTuples = ! this.getFirstRelationIterator().hasNext() || !secondRelation.iterator(transaction).hasNext();
	}

	@Override
	public boolean hasNext() throws TransactionAbortedException {
		if (this.noMoreTuples) {
			return false;
		} else {
			if (this.iteratingOverSecondRelation) {
				if (this.secondRelationIterator.hasNext()) {
					return true;
				} else { // finished with current firstRelation tuple, have to move to next
					this.iteratingOverSecondRelation = false;
					return this.hasNext();
				}
			} else { // not iterating over second relation, advance one with the first
				if (this.firstRelationIterator.hasNext()) {
					this.iteratingOverSecondRelation = true;
					this.firstRelationTuple = firstRelationIterator.next();
					this.secondRelationIterator = this.getSecondRelation().iterator(this.getTransaction());
					return this.hasNext();
				} else { //first relation has no more tuples, iteration finished
					this.noMoreTuples = true;
					return false;
				}
			}
		}
	}

	@Override
	public Tuple next() throws TransactionAbortedException {
		if (this.hasNext()){
			try {
				Tuple secondRelationTuple = this.secondRelationIterator.next();
				return new JoinTuple(this.firstRelationTuple,secondRelationTuple,this.getJoinRelationSchema());
			} catch (TuplesAndSchemasMatchException e) {
				throw new IteratingException("Error creating the JoinTuple");
			}
		}
		throw new NoSuchElementException();
	}


	protected Relation getFirstRelation() {
		return this.firstRelation;
	}

	protected void setFirstRelation(Relation firstRelation) {
		this.firstRelation = firstRelation;
	}

	protected Relation getSecondRelation() {
		return this.secondRelation;
	}

	protected void setSecondRelation(Relation secondRelation) {
		this.secondRelation = secondRelation;
	}

	protected JoinRelationSchema getJoinRelationSchema() {
		return this.joinRelationSchema;
	}

	protected void setJoinRelationSchema(JoinRelationSchema joinRelationSchema) {
		this.joinRelationSchema = joinRelationSchema;
	}

	protected RelationIterator<Tuple> getFirstRelationIterator() {
		return this.firstRelationIterator;
	}

	protected void setFirstRelationIterator(RelationIterator<Tuple> firstRelationIterator) {
		this.firstRelationIterator = firstRelationIterator;
	}

	protected RelationIterator<Tuple> getSecondRelationIterator() {
		return this.secondRelationIterator;
	}

	protected void setSecondRelationIterator(RelationIterator<Tuple> secondRelationIterator) {
		this.secondRelationIterator = secondRelationIterator;
	}

	protected Transaction getTransaction() {
		return this.transaction;
	}

	protected void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

}
