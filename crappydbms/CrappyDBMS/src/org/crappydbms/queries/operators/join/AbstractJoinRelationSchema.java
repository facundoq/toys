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
package org.crappydbms.queries.operators.join;

import java.util.ArrayList;
import java.util.List;

import org.crappydbms.relations.schemas.AbstractRelationSchema;
import org.crappydbms.relations.schemas.RelationSchema;
import org.crappydbms.relations.schemas.attributes.Attribute;

/**
 * @author Facundo Manuel Quiroga
 * Dec 11, 2008
 * 
 */
public abstract class AbstractJoinRelationSchema extends AbstractRelationSchema {

	protected boolean relationSchemasHaveCommonAttributeNames(RelationSchema firstRelationSchema, RelationSchema secondRelationSchema) {
		return !this.getMatchingAttributesFrom(firstRelationSchema,secondRelationSchema).isEmpty();
	}

	/**
	 * @return a list of attributes, so that the name of the attribute is the same for both relations 
	 */
	protected List<Attribute> getMatchingAttributesFrom(RelationSchema firstRelationSchema, RelationSchema secondRelationSchema) {
		List<Attribute> commonAttributes = new ArrayList<Attribute>();
		for (Attribute attribute:firstRelationSchema.getAttributes()){
			for (Attribute secondAttribute:secondRelationSchema.getAttributes()){
				if (attribute.matches(secondAttribute)){
					commonAttributes.add(attribute);
				}
			}
		}
		return commonAttributes;
	}
	
	protected ArrayList<Attribute> getEqualAttributesFrom(RelationSchema firstRelationSchema, RelationSchema secondRelationSchema) {
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		for (Attribute attribute : firstRelationSchema.getAttributes()){
			for (Attribute secondAttribute:secondRelationSchema.getAttributes()){
				if (attribute.equals(secondAttribute)){
					attributes.add(attribute);
				}
			}
		}
		return attributes;
	}
}
