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

import org.crappydbms.relations.fields.Field;
import org.crappydbms.relations.schemas.StoredRelationSchema;

/**
 * @author Facundo Manuel Quiroga
 * Dec 4, 2008
 * 
 */
public abstract class AbstractTuple implements Tuple {

	/**
	 * 
	 */
	public AbstractTuple() {
		super();
	}

	public String toString() {
	    String result = new String();
	    for (Field field : this.getFields()){
	    	result += field.toString()+" ";
	    }
	    return result.substring(0, result.length()-1);
	}

	public boolean equals(Tuple tuple) {
		return this.toString().equals(tuple.toString());
	}

	public boolean equals(Object tuple) {
		return this.equals( (Tuple) tuple);
	}

	public int hashCode() {
		return this.toString().hashCode();
	}
	
	public StoredTuple convertToStoredTuple(StoredRelationSchema schema) {
		
		return new StoredTuple(this.getFields(),this.getRelationSchema().convertToStoredRelationSchema());
	}
	public int numberOfFields(){
		return this.getFields().size();
	}
}