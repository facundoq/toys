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
package org.crappydbms.queries.operators.selection;

/**
 * @author Facundo Manuel Quiroga
 * Nov 26, 2008
 * 
 */

import java.util.NoSuchElementException;

import org.crappydbms.exceptions.InvalidAttributeNameException;
import org.crappydbms.queries.operators.SimpleOperatorIterator;
import org.crappydbms.queries.predicates.InvalidPredicateForRelationSchemaException;
import org.crappydbms.queries.predicates.Predicate;
import org.crappydbms.relations.RelationIterator;
import org.crappydbms.relations.schemas.RelationSchema;
import org.crappydbms.relations.tuples.Tuple;
import org.crappydbms.transactions.TransactionAbortedException;

public class SelectionOperatorIterator extends SimpleOperatorIterator {

	protected Predicate predicate;
	protected Tuple lastTuple;
	protected RelationSchema relationSchema;
	
	public SelectionOperatorIterator(RelationIterator<Tuple> iterator, Predicate predicate, RelationSchema relationSchema) {
		this.setIterator(iterator);
		this.setPredicate(predicate);
		// set lastTuple to null because no tuples have been searched for 
		this.setLastTuple(null);
		this.setRelationSchema(relationSchema);
	}

	
	@Override
	public boolean hasNext() throws TransactionAbortedException {
		// if there is a tuple in nextTuple, return true, because lastTuple has not been returned yet to iterator's client
		if (this.getLastTuple() != null){
			return true;
		}  
		// gets Tuples from iterator testing if each isTrueFor the predicate, and puts it into lastTuple if so
		Tuple tuple = null;
		boolean found = false;
		while (this.getIterator().hasNext() && !found){
			tuple = this.getIterator().next();
			try {
				found = this.getPredicate().isTrueFor(tuple);
			} catch (InvalidAttributeNameException e) {
				throw new InvalidPredicateForRelationSchemaException(e.getMessage());
			}
		}
		if (found){
			this.setLastTuple(tuple);
		}
		return found;
	}

	@Override
	public Tuple next() throws TransactionAbortedException {
		// call hasNext to see if there are more tuples, and also to set lastTuple if necessary
		if (!this.hasNext()){
			throw new NoSuchElementException("No more tuples!");
		}
		Tuple tuple = this.getLastTuple();
		// set lastTuple to null so next time hasNext is called it'll look for another tuple that matches the predicate
		this.setLastTuple(null);
		return tuple;
	}


	protected Predicate getPredicate() {
		return this.predicate;
	}

	protected void setPredicate(Predicate predicate) {
		this.predicate = predicate;
	}


	protected Tuple getLastTuple() {
		return this.lastTuple;
	}


	protected void setLastTuple(Tuple lastTuple) {
		this.lastTuple = lastTuple;
	}


	protected RelationSchema getRelationSchema() {
		return this.relationSchema;
	}


	protected void setRelationSchema(RelationSchema relationSchema) {
		this.relationSchema = relationSchema;
	}

}
