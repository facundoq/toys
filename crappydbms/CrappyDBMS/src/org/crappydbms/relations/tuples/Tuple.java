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

import java.util.ArrayList;

import org.crappydbms.exceptions.InvalidAttributeNameException;
import org.crappydbms.relations.fields.Field;
import org.crappydbms.relations.schemas.RelationSchema;
import org.crappydbms.relations.schemas.StoredRelationSchema;

/**
 * @author Facundo Manuel Quiroga
 * Oct 31, 2008
 * 
 */
public interface Tuple {
		//public void serializeTo(DataOutputStream pageStream) throws IOException;
    public RelationSchema getRelationSchema();
    public String toString();
    
    public boolean equals(Tuple tuple);
    public int hashCode();

  	/**
  	 * Field are numbered from 0 to fields.size-1
  	 * @throws InvalidFieldPositionException 
  	 */
    public Field getFieldAtPosition(int n) throws InvalidFieldPositionException;
    public Field getFieldNamed(String attributeName) throws InvalidAttributeNameException;
    public ArrayList<Field> getFields();
    public int numberOfFields();
		/**
		 * @param schema
		 * @return
		 */
		public StoredTuple convertToStoredTuple(StoredRelationSchema schema);
}
