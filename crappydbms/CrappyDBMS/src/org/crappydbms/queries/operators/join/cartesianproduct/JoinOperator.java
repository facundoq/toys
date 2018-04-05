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

import java.util.ArrayList;
import java.util.List;

import org.crappydbms.exceptions.CrappyDBMSError;
import org.crappydbms.queries.operators.Operator;
import org.crappydbms.queries.operators.join.CompositeOperator;
import org.crappydbms.queries.operators.join.RelationsWithSameNameException;
import org.crappydbms.queries.operators.rename.InvalidAttributeListsException;
import org.crappydbms.queries.operators.rename.RenameAttributesOperator;
import org.crappydbms.relations.Relation;
import org.crappydbms.relations.RelationIterator;
import org.crappydbms.relations.schemas.RelationSchema;
import org.crappydbms.relations.tuples.Tuple;
import org.crappydbms.transactions.Transaction;
import org.crappydbms.transactions.TransactionAbortedException;

/**
 * @author Facundo Manuel Quiroga Dec 9, 2008
 * 
 */
public class JoinOperator extends CompositeOperator {


	protected JoinRelationSchema joinRelationSchema;
	/**
	 * Creates a JoinOperator between the relations. Renames all attributes by adding the relation name as a prefix
	 * @param firstRelation
	 * @param secondRelation
	 * @throws RelationsWithSameNameException if both relations have the same name
	 */
	public JoinOperator(Relation firstRelation, Relation secondRelation) throws RelationsWithSameNameException {
		if (firstRelation.getName().equals(secondRelation.getName())){
			throw new RelationsWithSameNameException(firstRelation.getName()+" and "+secondRelation.getName());
		}
		this.setFirstRelation(this.appendRelationNameAsSuffixToAttributeNames(firstRelation));
		this.setSecondRelation(this.appendRelationNameAsSuffixToAttributeNames(secondRelation));
		this.setJoinRelationSchema(new JoinRelationSchema(this.getFirstRelation().getSchema(),this.getSecondRelation().getSchema()));
	}

	private RenameAttributesOperator appendRelationNameAsSuffixToAttributeNames(Relation relation) {
		List<String> names = relation.getSchema().getAttributeNames();
		String relationName = relation.getName();
		ArrayList<String> newNames =  new ArrayList<String>();
		for (String name : names){
			newNames.add(relationName+"."+name);
		}
		try {
			return new RenameAttributesOperator(relation,names,newNames);
		} catch (InvalidAttributeListsException e) {
			throw new CrappyDBMSError("Invalid attribute lists : message"+e.getMessage());
		}
	}

	@Override
	public String getName() {
		String firstRelationName = this.getFirstRelation().getName();
		String secondRelationName = this.getSecondRelation().getName();
		return "( " + firstRelationName + ") join (" + secondRelationName + " )";
	}

	@Override
	public RelationSchema getSchema() {
		return this.getJoinRelationSchema();
	}

	@Override
	public RelationIterator<Tuple> iterator(Transaction transaction) throws TransactionAbortedException {
		return new JoinOperatorIterator(this.getFirstRelation(),this.getSecondRelation(),this.getJoinRelationSchema(),transaction);
	}


	protected JoinRelationSchema getJoinRelationSchema() {
		return this.joinRelationSchema;
	}

	protected void setJoinRelationSchema(JoinRelationSchema joinRelationSchema) {
		this.joinRelationSchema = joinRelationSchema;
	}

}
