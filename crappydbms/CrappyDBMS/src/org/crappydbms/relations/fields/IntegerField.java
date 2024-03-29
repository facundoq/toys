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
package org.crappydbms.relations.fields;

import java.io.DataOutputStream;
import java.io.IOException;

import org.crappydbms.relations.schemas.attributes.AttributeType;
import org.crappydbms.relations.schemas.attributes.IntegerAttributeType;

/**
 * @author Facundo Manuel Quiroga Nov 11, 2008
 */
public class IntegerField implements Field {
	static AttributeType type = IntegerAttributeType.getInstance();

	public static IntegerField valueOf(Integer value) {
		return new IntegerField(value);
	}

	protected Integer value;

	private IntegerField(int value) {
		this.setValue(Integer.valueOf(value));
	}

	public Integer getValue() {
		return this.value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	@Override
	public void serializeTo(DataOutputStream pageStream) throws IOException {
		pageStream.writeInt(this.getValue());
	}

	public String toString() {
		return this.getValue().toString();
	}

	@Override
	public boolean equals(Field field) {
		if (!field.getType().equals(this.getType())){
			return false;
		}
		IntegerField otherField = (IntegerField) field;
		return this.getValue().equals(otherField.getValue());
	}

	public boolean equals(Object field) {
		try {
			return this.equals( (Field) field);
		} catch (ClassCastException e) {
			return false;
		}
	}
	public int hashCode(){
		return this.getType().hashCode()+this.getValue().hashCode();
	}

	@Override
	public int compareTo(Field field) {
		try {
			IntegerField integerField = (IntegerField) field;
			return this.getValue().compareTo(integerField.getValue());
		} catch (ClassCastException e) {
			throw new FieldTypeException("Cannot compare fields of a different class: received " + field.getClass().getName() + ", and this is: " + this.getClass().getName());
		}
	}

	public AttributeType getType() {
		return type;
	}

}
