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
package org.crappydbms.queries.operators;

import java.util.ArrayList;

import org.crappydbms.relations.Relation;
import org.crappydbms.relations.RelationIterator;
import org.crappydbms.relations.schemas.RelationSchema;
import org.crappydbms.relations.tuples.Tuple;
import org.crappydbms.transactions.Transaction;
import org.crappydbms.transactions.TransactionAbortedException;

/**
 * @author Facundo Manuel Quiroga
 * Dec 4, 2008
 * 
 */
public abstract class SimpleOperator implements Operator {

	public SimpleOperator(Relation relation) {
		super();
		this.setRelation(relation);
	}

	protected Relation relation;

	protected Relation getRelation() {
		return this.relation;
	}

	protected void setRelation(Relation relation) {
		this.relation = relation;
	}
	public String getName(){
		return this.getRelation().getName();
	}
	public RelationSchema getSchema(){
		return this.getRelation().getSchema();
	}
	public ArrayList<Tuple> iterateAndReturnTuples(Transaction transaction) throws TransactionAbortedException{
		ArrayList<Tuple> tuples = new ArrayList<Tuple>();
		RelationIterator<Tuple> iterator = this.iterator(transaction);
		while (iterator.hasNext()){
			tuples.add(iterator.next());
		}
		return tuples;
	}

}
