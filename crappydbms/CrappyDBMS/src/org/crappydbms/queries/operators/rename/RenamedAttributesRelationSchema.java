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
import java.util.List;

import org.crappydbms.relations.schemas.AbstractRelationSchema;
import org.crappydbms.relations.schemas.RelationSchema;
import org.crappydbms.relations.schemas.attributes.Attribute;
import org.crappydbms.relations.schemas.attributes.BaseAttribute;

/**
 * @author Facundo Manuel Quiroga Dec 12, 2008
 * 
 */
public class RenamedAttributesRelationSchema extends AbstractRelationSchema {

	List<Attribute> attributes;

	public RenamedAttributesRelationSchema(RelationSchema schema, List<String> originalAttributeNames, List<String> newAttributeNames) throws InvalidAttributeListsException {
		if (originalAttributeNames.size() != newAttributeNames.size()) {
			throw new InvalidAttributeListsException("Original attributes count: " + originalAttributeNames.size() + ". New attributes count: "+ newAttributeNames.size());
		}

		this.setAttributes(new ArrayList<Attribute>());
		for (Attribute attribute : schema.getAttributes()) {
			if (originalAttributeNames.contains(attribute.getName())) {
				int positionOfNewName = originalAttributeNames.indexOf(attribute.getName());
				String newName = newAttributeNames.get(positionOfNewName);
				this.getAttributes().add(new BaseAttribute(newName, attribute.getType()));
			} else {
				this.getAttributes().add(attribute);
			}
		}
	}

	protected void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}

	@Override
	public List<Attribute> getAttributes() {
		return this.attributes;
	}

}
