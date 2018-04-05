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

import org.crappydbms.exceptions.CrappyDBMSError;
import org.crappydbms.relations.schemas.attributes.AttributeType;
import org.crappydbms.relations.schemas.attributes.StringAttributeType;

/**
 * @author Facundo Manuel Quiroga Nov 11, 2008
 * 
 */
public class StringField implements Field {
	static AttributeType type = StringAttributeType.getInstance();

	public static int maximumLength() {
		return StringAttributeType.getInstance().getMaximumLength();
	}

	public static StringField valueOf(String value) {
		return new StringField(value);
	}

	String value;

	private StringField(String value) {
		this.setValue(value);
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		if (value.length() > StringField.maximumLength()) {
			throw new CrappyDBMSError("Strings cant be more than 49-char length. Value: " + value);
		}
		this.value = value;
	}

	@Override
	public void serializeTo(DataOutputStream pageStream) throws IOException {
		byte stringLength = new Integer(this.getValue().length()).byteValue();
		int sl = this.getValue().length();
		int extraBytes = StringField.maximumLength() - stringLength;
		pageStream.write(sl);
		pageStream.writeBytes(this.getValue());
		// fill remaining space
		byte[] fillingBytes = new byte[extraBytes];
		for (int i = 0; i<fillingBytes.length;i++){
			fillingBytes[i]=0;
		}
		pageStream.write(fillingBytes);
		/*
		while (extraBytes > 0) {
			pageStream.writeByte(0);
			extraBytes--;
		}*/

	}

	public String toString() {
		return this.getValue();
	}

	@Override
	public boolean equals(Field field) {
		if (!field.getType().equals(this.getType())){
			return false;
		}
		StringField otherField = (StringField) field;
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
			StringField stringField = (StringField) field;
			return this.getValue().compareTo(stringField.getValue());
		} catch (ClassCastException e) {
			throw new FieldTypeException("Cannot compare fields of a different class: received " + field.getClass().getName() + ", and this is: "
					+ this.getClass().getName());
		}
	}

	@Override
	public AttributeType getType() {
		return type;
	}

}
