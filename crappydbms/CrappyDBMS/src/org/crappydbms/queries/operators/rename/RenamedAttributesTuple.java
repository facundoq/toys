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

import java.util.ArrayList;

import org.crappydbms.exceptions.InvalidAttributeNameException;
import org.crappydbms.relations.fields.Field;
import org.crappydbms.relations.schemas.InvalidTupleForRelationSchemaException;
import org.crappydbms.relations.tuples.AbstractTuple;
import org.crappydbms.relations.tuples.InvalidFieldPositionException;
import org.crappydbms.relations.tuples.Tuple;

/**
 * @author Facundo Manuel Quiroga
 * Dec 12, 2008
 * 
 */
public class RenamedAttributesTuple extends AbstractTuple implements Tuple {
	
	protected Tuple tuple;
	protected RenamedAttributesRelationSchema relationSchema; 
	/**
	 * @param tuple
	 * @param relationSchema
	 */
	public RenamedAttributesTuple(Tuple tuple, RenamedAttributesRelationSchema relationSchema) {	
		this.setTuple(tuple);
		this.setRelationSchema(relationSchema);
	}


	@Override
	public Field getFieldAtPosition(int n) throws InvalidFieldPositionException {
		return this.getFields().get(n);
	}

	@Override
	public Field getFieldNamed(String attributeName) throws InvalidAttributeNameException {
		int position = this.getRelationSchema().getPositionOfAttributeNamed(attributeName); 
		try {
			return this.getFieldAtPosition(position);
		} catch (InvalidFieldPositionException e) {
			throw new InvalidTupleForRelationSchemaException("Attribute name"+attributeName+" has a non-existant position");
		}
	}

	@Override
	public ArrayList<Field> getFields() {
		return this.getTuple().getFields();
	}

	@Override
	public RenamedAttributesRelationSchema getRelationSchema() {
		
		return this.relationSchema;
	}



	protected Tuple getTuple() {
		return this.tuple;
	}

	protected void setTuple(Tuple tuple) {
		this.tuple = tuple;
	}

	protected void setRelationSchema(RenamedAttributesRelationSchema relationSchema) {
		this.relationSchema = relationSchema;
	}

}
