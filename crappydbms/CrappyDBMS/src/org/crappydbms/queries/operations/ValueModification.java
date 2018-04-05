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
package org.crappydbms.queries.operations;

import org.crappydbms.exceptions.InvalidAttributeNameException;
import org.crappydbms.relations.fields.Field;
import org.crappydbms.relations.tuples.InvalidAttributeTypeException;
import org.crappydbms.relations.tuples.StoredTuple;

/**
 * @author Facundo Manuel Quiroga
 * Dec 19, 2008
 * 
 */
public class ValueModification implements Modification {

	protected String attributeName;
	protected Field field;
	
	public ValueModification(String attributeName, Field field){
		this.setAttributeName(attributeName);
		this.setField(field);
	}
	@Override
	public void applyTo(StoredTuple tuple) throws InvalidAttributeNameException, InvalidAttributeTypeException {
		tuple.modifyColumn(this.getAttributeName(), this.getField());
	}
	
	
	protected String getAttributeName() {
		return this.attributeName;
	}
	protected void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	protected Field getField() {
		return this.field;
	}
	protected void setField(Field field) {
		this.field = field;
	}
	
	public String toString(){
		return "modifying attribute named"+this.getAttributeName()+" with value:"+this.getField().getValue();
	}
}
