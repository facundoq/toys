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

import java.util.List;

import org.crappydbms.queries.operators.Operator;
import org.crappydbms.queries.operators.SimpleOperator;
import org.crappydbms.relations.Relation;
import org.crappydbms.relations.RelationIterator;
import org.crappydbms.relations.schemas.RelationSchema;
import org.crappydbms.relations.tuples.Tuple;
import org.crappydbms.transactions.Transaction;
import org.crappydbms.transactions.TransactionAbortedException;

/**
 * @author Facundo Manuel Quiroga
 * Dec 12, 2008
 * 
 */
public class RenameAttributesOperator extends SimpleOperator implements Operator {

	RenamedAttributesRelationSchema relationSchema;

	
	
	public RenameAttributesOperator(Relation relation,List<String> originalAttributeNames,List<String> newAttributeNames ) throws InvalidAttributeListsException {
		super(relation);
		RenamedAttributesRelationSchema relationSchema = new RenamedAttributesRelationSchema(relation.getSchema(),originalAttributeNames,newAttributeNames);
		this.setRelationSchema(relationSchema);
		
	}
	
	@Override
	public RelationSchema getSchema(){
		return this.getRelationSchema();
	}
	
	
	@Override
	public RelationIterator<Tuple> iterator(Transaction transaction) throws TransactionAbortedException {
		return new RenameAttributesOperatorIterator(this.getRelation().iterator( transaction),this.getRelationSchema());
	}

	protected RenamedAttributesRelationSchema getRelationSchema() {
		return this.relationSchema;
	}
	protected void setRelationSchema(RenamedAttributesRelationSchema relationSchema) {
		this.relationSchema = relationSchema;
	}

}
