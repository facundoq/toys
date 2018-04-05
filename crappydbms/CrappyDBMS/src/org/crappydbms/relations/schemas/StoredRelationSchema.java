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
package org.crappydbms.relations.schemas;

import java.util.ArrayList;
import java.util.List;

import org.crappydbms.relations.fields.Field;
import org.crappydbms.relations.schemas.attributes.Attribute;
import org.crappydbms.relations.schemas.attributes.AttributeType;

/**
 * @author Facundo Manuel Quiroga
 * Oct 31, 2008
 * 
 */
public class StoredRelationSchema extends AbstractRelationSchema {
	
	protected ArrayList<Attribute> attributes;

	
	public StoredRelationSchema(List<Attribute> attributes) throws RepeatedAttributeNameException{	
		this.setAttributes(new ArrayList<Attribute>());
		for (Attribute attribute : attributes){
			if (this.hasAttributeNamed(attribute.getName())){
				throw new RepeatedAttributeNameException("The attribute name "+attribute.getName()+"was repeated");
			}
			this.getAttributes().add(attribute);
		}

	}

	/* (non-Javadoc)
	 * @see org.crappydbms.main.RelationSchema#Equals(org.crappydbms.main.RelationSchema)
	 */
	@Override
	public boolean equals(RelationSchema relationSchema) {
	    
	    return this.getAttributes().equals(relationSchema.getAttributes());
	}
	
	
	
	
	protected void setAttributes(ArrayList<Attribute> attributes) {
		this.attributes = attributes;
	}
	public ArrayList<Attribute> getAttributes() {
		return this.attributes;
	}
	
	
	@Override
	public StoredRelationSchema convertToStoredRelationSchema() {
		return this;
	}

	/**
	 * @param fields
	 */
	public boolean areCompatible(ArrayList<Field> fields) {
		ArrayList<AttributeType> fieldsAttributeTypes = new ArrayList<AttributeType>();
		for (Field field : fields){
			fieldsAttributeTypes.add(field.getType());
		}
		return fieldsAttributeTypes.equals(this.getAttributeTypes());
		
	}

	/**
	 * Get the size a tuple of this relation schema would occupy after being serialized
	 */
	
	public int getTupleSize() {
		int size=0;
		for (Attribute attribute : this.getAttributes()){
			size += attribute.getType().getSerializedSize();
		}
		return size;
	}

}
