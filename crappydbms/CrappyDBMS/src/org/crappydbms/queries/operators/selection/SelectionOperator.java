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

import org.crappydbms.queries.operators.SimpleOperator;
import org.crappydbms.queries.predicates.Predicate;
import org.crappydbms.relations.Relation;
import org.crappydbms.relations.RelationIterator;
import org.crappydbms.relations.schemas.RelationSchema;
import org.crappydbms.relations.tuples.Tuple;
import org.crappydbms.transactions.Transaction;
import org.crappydbms.transactions.TransactionAbortedException;

/**
 * @author Facundo Manuel Quiroga
 * Nov 3, 2008
 * 
 */
public class SelectionOperator extends SimpleOperator {
	
	protected Predicate predicate;
	/**
	 * @param relation
	 * @param predicate
	 */
	public SelectionOperator(Relation relation, Predicate predicate) {
		super(relation);
		this.setPredicate(predicate);
	}

	@Override
	public RelationSchema getSchema() {
		return this.getRelation().getSchema();
	}

	@Override
	public RelationIterator<Tuple> iterator(Transaction transaction) throws TransactionAbortedException {
		return new SelectionOperatorIterator(this.getRelation().iterator(transaction),this.getPredicate(),this.getSchema());
	}

	protected Predicate getPredicate() {
		return this.predicate;
	}

	protected void setPredicate(Predicate predicate) {
		this.predicate = predicate;
	}



}
