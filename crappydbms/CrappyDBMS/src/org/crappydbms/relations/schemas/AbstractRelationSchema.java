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
import java.util.Iterator;
import java.util.List;

import org.crappydbms.exceptions.CrappyDBMSError;
import org.crappydbms.exceptions.InvalidAttributeNameException;
import org.crappydbms.relations.schemas.attributes.Attribute;
import org.crappydbms.relations.schemas.attributes.AttributeType;
import org.crappydbms.relations.tuples.Tuple;

/**
 * @author Facundo Manuel Quiroga
 * Dec 8, 2008
 * 
 */
public abstract class AbstractRelationSchema implements RelationSchema {

	/**
	 * 
	 */
	public AbstractRelationSchema() {
		super();
	}

	public boolean hasAttributeNamed(String name) {
		for (Attribute attribute : this.getAttributes()){
			if (attribute.getName().equals(name)){
				return true;
			}
		}
		return false;
	}

	/**
	 * @param attributeName
	 * @return the attribute named AttributeName
	 * @throws InvalidAttributeNameException if no attribute exists named AttributeName
	 */
	public Attribute attributeNamed(String attributeName) throws InvalidAttributeNameException {
		for (Attribute attribute : this.getAttributes()){
			if (attribute.getName().equals(attributeName)){
				return attribute;
			}
			
		}
		throw new InvalidAttributeNameException(attributeName);
	}

	public ArrayList<AttributeType> getAttributeTypes() {
		ArrayList<AttributeType> types = new ArrayList<AttributeType>();
		for (Attribute attribute : this.getAttributes()){
			types.add(attribute.getType());
		}
		return types;
	}

	public ArrayList<String> getAttributeNames() {
		ArrayList<String> names = new ArrayList<String>();
		for (Attribute attribute : this.getAttributes()){
			names.add(attribute.getName());
		}
		return names;
	}

	

	@Override
	public boolean isCompatible(Tuple tuple) {
		return this.isCompatibleWith(tuple.getRelationSchema());
	}

	
	public boolean isCompatibleWith(RelationSchema relationSchema) {
		List<AttributeType> thisAttributeTypes = this.getAttributeTypes();
		List<AttributeType> otherAttributeTypes = relationSchema.getAttributeTypes();
		return thisAttributeTypes.equals(otherAttributeTypes);
		
	}
	public Attribute getAttribute(int i){
		return this.getAttributes().get(i);
	}
	
	public int getPositionOfAttributeNamed(String attributeName) throws InvalidAttributeNameException{
		int position = -1;
		Iterator<Attribute> iterator = this.getAttributes().iterator();
		boolean found = false;
		while ( !found && iterator.hasNext()){
			Attribute attribute = iterator.next();
			position++;
			found = attribute.getName().equals(attributeName);
		}
		if (!found){
			throw new InvalidAttributeNameException(attributeName);
		}
		return position;
	}
	public int getNumberOfAttributes() {
		return this.getAttributes().size();
	}
	
	@Override
	public boolean equals(RelationSchema relationSchema) {
		
		return this.getAttributes().equals(relationSchema.getAttributes());
	}

	public String toString(){
		String result = new String();
		for (Attribute attribute : this.getAttributes()){
			result = result + attribute +  " ";
		}
		return result.substring(0, result.length()-1);
	}
	@Override
	public StoredRelationSchema convertToStoredRelationSchema() {
		try {
			return new StoredRelationSchema(this.getAttributes());
		} catch (RepeatedAttributeNameException e) {
			throw new CrappyDBMSError("Creating a StoredRelationSchema should never throw a RepeatedAttributeNameException. Either this is not a valid Schema, or there is an error in the constructor");
		}
	}
}