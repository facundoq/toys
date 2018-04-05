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

import java.util.List;

import org.crappydbms.relations.schemas.attributes.Attribute;
import org.crappydbms.relations.schemas.attributes.AttributeType;
import org.crappydbms.relations.tuples.Tuple;


/**
 * @author Facundo Manuel Quiroga
 * Oct 31, 2008
 * 
 */
public interface RelationSchema {	
	
	public List<Attribute> getAttributes();
	
	public List<AttributeType> getAttributeTypes();
	public List<String> getAttributeNames();
	
	/**
	 * @return true if both relation schemas have the same attributes in the same order  
	 */
	public boolean equals (RelationSchema relationSchema);

	
	/**
	 * @param relationSchema
	 * @return true if both relation schemas have the same number of attributes and if for each pair of attributes in position i in both schemas, the type of attribute matches 
	 */
	public boolean isCompatibleWith(RelationSchema relationSchema);
	/**
	 * @param relationSchema
	 * @return true if the tuple is compatible with the relation schema 
	 */
	public boolean isCompatible(Tuple tuple);
	/**
	 * @return
	 */
	public StoredRelationSchema convertToStoredRelationSchema();
	
	
	
}
