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
package org.crappydbms.queries.operators.rename;

import org.crappydbms.relations.Relation;
import org.crappydbms.relations.RelationIterator;
import org.crappydbms.relations.schemas.RelationSchema;
import org.crappydbms.relations.tuples.Tuple;
import org.crappydbms.transactions.Transaction;
import org.crappydbms.transactions.TransactionAbortedException;

/**
 * @author Facundo Manuel Quiroga
 * Jan 23, 2009
 * 
 */
public class RenameRelationOperator implements Relation {

	protected Relation relation;
	protected  String newName;
	public RenameRelationOperator(Relation relation, String newName){
		 this.setRelation(relation);
		 this.setNewName(newName);
	}

	@Override
	public RelationIterator<Tuple> iterator(Transaction transaction) throws TransactionAbortedException {
		return this.getRelation().iterator(transaction);
	}

	@Override
	public String getName() {
		return this.getNewName();
	}

	@Override
	public RelationSchema getSchema() {
		return this.getRelation().getSchema();
	}

	protected Relation getRelation() {
		return this.relation;
	}

	protected void setRelation(Relation relation) {
		this.relation = relation;
	}

	protected String getNewName() {
		return this.newName;
	}

	protected void setNewName(String newName) {
		this.newName = newName;
	}

}
