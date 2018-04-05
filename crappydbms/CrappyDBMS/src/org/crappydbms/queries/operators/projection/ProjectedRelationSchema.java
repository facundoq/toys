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

import java.util.ArrayList;
import java.util.List;

import org.crappydbms.exceptions.CrappyDBMSError;
import org.crappydbms.exceptions.InvalidAttributeNameException;
import org.crappydbms.relations.fields.Field;
import org.crappydbms.relations.schemas.AbstractRelationSchema;
import org.crappydbms.relations.schemas.RelationSchema;
import org.crappydbms.relations.schemas.RepeatedAttributeNameException;
import org.crappydbms.relations.schemas.StoredRelationSchema;
import org.crappydbms.relations.schemas.attributes.Attribute;
import org.crappydbms.relations.tuples.InvalidFieldPositionException;
import org.crappydbms.relations.tuples.Tuple;

/**
 * @author Facundo Manuel Quiroga
 * Dec 4, 2008
 * 
 */
public  class ProjectedRelationSchema extends AbstractRelationSchema {

	protected RelationSchema underlyingRelationSchema;
	protected ArrayList<Attribute> actualAttributes;
	protected ArrayList<Integer> properPositions;
	
	public ProjectedRelationSchema(RelationSchema underlyingRelationSchema,List<String> removedAttributeNames){
		this.setUnderlyingRelationSchema(underlyingRelationSchema);
		ArrayList<Integer> properPositions = new ArrayList<Integer>();
		ArrayList<Attribute> actualAttributes = new ArrayList<Attribute>();
		
		List<Attribute> attributes = underlyingRelationSchema.getAttributes();
		
		for (int i = 0; i<attributes.size();i++){
			Attribute currentAttribute = attributes.get(i);
			if (!removedAttributeNames.contains(currentAttribute.getName()) ){
				properPositions.add(i);
				actualAttributes.add(currentAttribute);
			}
		}
		this.setActualAttributes(actualAttributes);
		this.setProperPositions(properPositions);
	}
	
	@Override
	public boolean equals(RelationSchema relationSchema) {
		return this.getActualAttributes().equals(relationSchema.getAttributes());
	}

	@Override
	public List<Attribute> getAttributes() {
		return this.getActualAttributes();
	}

	@Override
	public StoredRelationSchema convertToStoredRelationSchema() {
		try {
			return new StoredRelationSchema(this.getAttributes());
		} catch (RepeatedAttributeNameException e) {
			throw new CrappyDBMSError("Creating a StoredRelationSchema should never throw a RepeatedAttributeNameException. Either this is not a valid Schema, or there is an error in the constructor");
		}
	}

	// TUPLE

	public boolean equals(ProjectedTuple firstTuple, ProjectedTuple secondTuple) {		
		return this.getFieldsFrom(firstTuple).equals(this.getFieldsFrom(secondTuple));
	}
	
	public ArrayList<Field> getFieldsFrom(ProjectedTuple tuple) {
		ArrayList<Field> fields = new ArrayList<Field>();
		for (int i = 0; i<this.getNumberOfAttributes();i++){
			try {
				fields.add(this.getFieldAtPosition(i, tuple));
			} catch (InvalidFieldPositionException e) {
				throw new CrappyDBMSError("Should never happen that a ProjectedRelationSchema of a ProjectedTuple asks for a field at a wrong position");
			}
		}
		
		return fields;
	}

	/**
	 * @param n
	 * @param projectedTuple
	 * @return
	 * @throws InvalidFieldPositionException 
	 */
	public Field getFieldAtPosition(int n, ProjectedTuple projectedTuple) throws InvalidFieldPositionException {
		
		int properPosition = this.getProperPositions().get(n);
		Tuple tuple = projectedTuple.getInnerTuple();
		return tuple.getFieldAtPosition(properPosition);
	}

	public Field getFieldNamedFrom(String attributeName, ProjectedTuple tuple) throws InvalidAttributeNameException {
		int position = this.getPositionOfAttributeNamed(attributeName);
		try {
			return this.getFieldAtPosition(position, tuple);
		} catch (InvalidFieldPositionException e) {
			throw new CrappyDBMSError("Should never happen that a ProjectedRelationSchema of a ProjectedTuple asks for a field at a wrong position");
		}
	}

	// PROTECTED

	protected ArrayList<Integer> getProperPositions() {
		return this.properPositions;
	}

	protected void setProperPositions(ArrayList<Integer> properPositions) {
		this.properPositions = properPositions;
	}

	protected RelationSchema getUnderlyingRelationSchema() {
		return this.underlyingRelationSchema;
	}

	protected void setUnderlyingRelationSchema(RelationSchema underlyingRelationSchema) {
		this.underlyingRelationSchema = underlyingRelationSchema;
	}

	protected ArrayList<Attribute> getActualAttributes() {
		return this.actualAttributes;
	}

	protected void setActualAttributes(ArrayList<Attribute> actualAttributes) {
		this.actualAttributes = actualAttributes;
	}

}
