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

package org.crappydbms.relations.tuples;

import java.io.DataOutputStream;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;

import org.crappydbms.exceptions.InvalidAttributeNameException;
import org.crappydbms.relations.fields.Field;
import org.crappydbms.relations.schemas.InvalidTupleForRelationSchemaException;
import org.crappydbms.relations.schemas.StoredRelationSchema;
import org.crappydbms.relations.schemas.attributes.Attribute;

/**
 * A Stored tuple is one that has been retrieved directly from or can be stored to disk as is
 * It is the only  kind of tuple that can be stored
 * @author Facundo Manuel Quiroga Nov 11, 2008
 * 
 */

public class StoredTuple extends AbstractTuple {

	protected StoredRelationSchema relationSchema;

	protected ArrayList<Field> fields;

	public StoredTuple(ArrayList<Field> fields, StoredRelationSchema relationSchema) {
		if (fields.isEmpty()) {
			throw new InvalidParameterException("empty 'fields' collection");
		}
		if (!relationSchema.areCompatible(fields)){
			throw new InvalidParameterException("fields list is not compatible with relation schema"); 
		}
		this.setRelationSchema(relationSchema);
		this.setFields(new ArrayList<Field>());
		this.getFields().addAll(fields);
	}

	public StoredRelationSchema getRelationSchema() {
		return this.relationSchema;
	}

	protected void setRelationSchema(StoredRelationSchema relationSchema) {
		this.relationSchema = relationSchema;
	}


	public void serializeTo(DataOutputStream pageStream) throws IOException {
		for (Field field : this.getFields()) {
			field.serializeTo(pageStream);
		}
	}
	
	@Override
	public ArrayList<Field> getFields() {
		return this.fields;
	}

	protected void setFields(ArrayList<Field> fields) {
		this.fields = fields;
	}

	@Override
	public Field getFieldNamed(String attributeName) {
		int position;
		try {
			position = this.getRelationSchema().getPositionOfAttributeNamed(attributeName);
		} catch (InvalidAttributeNameException e) {
			throw new InvalidTupleForRelationSchemaException("Cannot find attribute named: "+ attributeName);
		}
		try{
			return this.getFieldAtPosition(position);
		} catch (InvalidFieldPositionException e) {
			throw new InvalidTupleForRelationSchemaException("Invalid position for tuple, position = "+ position);
		}
	}

	@Override
	public Field getFieldAtPosition(int n) throws InvalidFieldPositionException {
		ArrayList<Field> fields = this.getFields();
		if (n >= fields.size() || n < 0) {
			throw new InvalidFieldPositionException("Invalid number of field " + n + ". Tuple has " + fields.size() + " fields.");
		}
		return fields.get(n);
	}
	
	

	@Override
	public StoredTuple convertToStoredTuple(StoredRelationSchema schema) {
		return new StoredTuple(this.getFields(),schema);
	}

	/**
	 * @param columnName
	 * @param field
	 * @throws InvalidAttributeNameException 
	 * @throws InvalidAttributeTypeException 
	 */
	public void modifyColumn(String attributeName, Field field) throws InvalidAttributeNameException, InvalidAttributeTypeException {

			Attribute attribute = this.getRelationSchema().attributeNamed(attributeName);
			//if the field is not of the same type as the attribute raise an exception
			if ( ! attribute.getType().isTypeOf(field)){
				throw new InvalidAttributeTypeException("Attribute "+attribute+" does not have the same type as field "+field);
			}
			int attributePosition = this.getRelationSchema().getPositionOfAttributeNamed(attributeName);
			this.getFields().set(attributePosition, field);
	}
	

}
