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

import org.crappydbms.queries.operators.join.AbstractJoinRelationSchema;
import org.crappydbms.queries.operators.join.RelationSchemasHaveCommonAttributesException;
import org.crappydbms.relations.schemas.RelationSchema;
import org.crappydbms.relations.schemas.attributes.Attribute;

/**
 * @author Facundo Manuel Quiroga
 * Dec 9, 2008
 * 
 */
public class JoinRelationSchema extends AbstractJoinRelationSchema {
	protected RelationSchema firstRelationSchema;
	protected RelationSchema secondRelationSchema;
	protected ArrayList<Attribute> attributes;
	
	public JoinRelationSchema(RelationSchema firstRelationSchema, RelationSchema secondRelationSchema) {

		if (this.relationSchemasHaveCommonAttributeNames(firstRelationSchema,secondRelationSchema)){
			throw new RelationSchemasHaveCommonAttributesException("");
		}
		this.setFirstRelationSchema(firstRelationSchema);
		this.setSecondRelationSchema(secondRelationSchema);
		this.setAttributes(new ArrayList<Attribute>());
		this.getAttributes().addAll(firstRelationSchema.getAttributes());
		this.getAttributes().addAll(secondRelationSchema.getAttributes());
	}

	@Override
	public boolean equals(RelationSchema relationSchema) {
		return this.getAttributes().equals(relationSchema.getAttributes());
	}

	@Override
	public List<Attribute> getAttributes() {
		return this.attributes ;
	}
	
	protected RelationSchema getFirstRelationSchema() {
		return this.firstRelationSchema;
	}
	protected void setFirstRelationSchema(RelationSchema firstRelationSchema) {
		this.firstRelationSchema = firstRelationSchema;
	}
	protected RelationSchema getSecondRelationSchema() {
		return this.secondRelationSchema;
	}
	protected void setSecondRelationSchema(RelationSchema secondRelationSchema) {
		this.secondRelationSchema = secondRelationSchema;
	}
	protected void setAttributes(ArrayList<Attribute> attributes) {
		this.attributes = attributes;
	}

}
