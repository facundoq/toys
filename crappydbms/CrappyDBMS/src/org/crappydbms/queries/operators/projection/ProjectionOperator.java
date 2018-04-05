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
package org.crappydbms.queries.operators.projection;

import java.util.List;

import org.crappydbms.queries.operators.SimpleOperator;
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
public class ProjectionOperator extends SimpleOperator {

	protected ProjectedRelationSchema projectedRelationSchema;

	public ProjectionOperator(Relation relation, List<String> removedAttributeNames) {		
		super(relation);
		this.setProjectedRelationSchema(new ProjectedRelationSchema(relation.getSchema(),removedAttributeNames));
	}

	@Override
	public RelationSchema getSchema() {
		return this.getProjectedRelationSchema();
	}

	@Override
	public RelationIterator<Tuple> iterator(Transaction transaction) throws TransactionAbortedException {
		return new ProjectedOperatorIterator(this.getRelation().iterator(transaction),this.getProjectedRelationSchema());
	}

	protected ProjectedRelationSchema getProjectedRelationSchema() {
		return this.projectedRelationSchema;
	}

	protected void setProjectedRelationSchema(ProjectedRelationSchema projectedRelationSchema) {
		this.projectedRelationSchema = projectedRelationSchema;
	}

}
