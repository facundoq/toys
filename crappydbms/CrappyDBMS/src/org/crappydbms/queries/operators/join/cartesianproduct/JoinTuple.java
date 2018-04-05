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

import org.crappydbms.exceptions.CrappyDBMSError;
import org.crappydbms.exceptions.InvalidAttributeNameException;
import org.crappydbms.queries.operators.join.TuplesAndSchemasMatchException;
import org.crappydbms.relations.fields.Field;
import org.crappydbms.relations.schemas.RelationSchema;
import org.crappydbms.relations.schemas.StoredRelationSchema;
import org.crappydbms.relations.tuples.AbstractTuple;
import org.crappydbms.relations.tuples.InvalidFieldPositionException;
import org.crappydbms.relations.tuples.StoredTuple;
import org.crappydbms.relations.tuples.Tuple;

/**
 * @author Facundo Manuel Quiroga
 * Dec 9, 2008
 * 
 */
public class JoinTuple extends AbstractTuple {
	ArrayList<Field> fields;
	protected Tuple firstTuple;
	protected Tuple secondTuple;
	protected JoinRelationSchema joinRelationSchema;	
	
	/**
	 * @param firstTuple of the resulting JoinTuple
	 * @param secondTuple of the resulting JoinTuple
	 * @param joinRelationSchema of the resulting JoinTuple
	 * @throws TuplesAndSchemasMatchException if the tuples are not valid for the JoinRelationSchema
	 */
	public JoinTuple(Tuple firstTuple, Tuple secondTuple, JoinRelationSchema joinRelationSchema) throws TuplesAndSchemasMatchException {
		if  (! this.tuplesAreValidFor(firstTuple,secondTuple,joinRelationSchema)){
			throw new TuplesAndSchemasMatchException(firstTuple.getRelationSchema()+" joined with "+secondTuple.getRelationSchema()+" do not match: "+ joinRelationSchema);
		}
		this.setFirstTuple(firstTuple);
		this.setSecondTuple(secondTuple);
		this.setJoinRelationSchema(joinRelationSchema);
		this.setFields(new ArrayList<Field>());
		this.getFields().addAll(firstTuple.getFields());
		this.getFields().addAll(secondTuple.getFields());
	}

	/**
	 * Return true if tuples' relation schemas are in accord with the JoinRelationSchema
	 * firstTuple's relation schema must be the equal to joinRelationSchema's first schema,
	 * and the same for the secondTuple.
	 */
	private boolean tuplesAreValidFor(Tuple firstTuple, Tuple secondTuple, JoinRelationSchema joinRelationSchema) {
		if ( ! firstTuple.getRelationSchema().equals(joinRelationSchema.getFirstRelationSchema())){
			return false;
		}
		if ( ! secondTuple.getRelationSchema().equals(joinRelationSchema.getSecondRelationSchema())){
			return false;
		}
		return true;
	}

	@Override
	public StoredTuple convertToStoredTuple(StoredRelationSchema schema) {
		return new StoredTuple(this.getFields(),schema);
	}

	@Override
	public Field getFieldAtPosition(int n) throws InvalidFieldPositionException {
		if (n<0 || n > this.numberOfFields()){
			throw new InvalidFieldPositionException("position:"+n);
		}
		return this.getFields().get(n);
	}

	@Override
	public Field getFieldNamed(String attributeName) throws InvalidAttributeNameException {
		int position = this.getJoinRelationSchema().getPositionOfAttributeNamed(attributeName); 
		try {
			return this.getFieldAtPosition(position);
		} catch (InvalidFieldPositionException e) {
			throw new CrappyDBMSError("Invalid position"+position+"; Should never happen, since position was obtained from the RelationSchema.");
		}
	}

	@Override
	public ArrayList<Field> getFields() {
		return this.fields;
	}

	@Override
	public RelationSchema getRelationSchema() {
		return this.getJoinRelationSchema();
	}

	@Override
	public int numberOfFields() {
		return this.getFields().size();
	}


	protected Tuple getFirstTuple() {
		return this.firstTuple;
	}

	protected void setFirstTuple(Tuple firstTuple) {
		this.firstTuple = firstTuple;
	}

	protected Tuple getSecondTuple() {
		return this.secondTuple;
	}

	protected void setSecondTuple(Tuple secondTuple) {
		this.secondTuple = secondTuple;
	}

	protected JoinRelationSchema getJoinRelationSchema() {
		return this.joinRelationSchema;
	}

	protected void setJoinRelationSchema(JoinRelationSchema joinRelationSchema) {
		this.joinRelationSchema = joinRelationSchema;
	}

	protected void setFields(ArrayList<Field> fields) {
		this.fields = fields;
	}

}
