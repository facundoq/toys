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
package org.crappydbms.relations.schemas.attributes;

import java.io.DataInputStream;
import java.io.IOException;

import org.crappydbms.exceptions.CrappyDBMSError;
import org.crappydbms.relations.fields.Field;
import org.crappydbms.relations.fields.IntegerField;

/**
 * @author Facundo Manuel Quiroga
 * Nov 4, 2008
 * 
 */
public class IntegerAttributeType implements AttributeType {
	
	protected static IntegerAttributeType instance; 
	public static IntegerAttributeType getInstance(){
		if (IntegerAttributeType.instance == null){
			IntegerAttributeType.instance = new IntegerAttributeType();
		}
		return IntegerAttributeType.instance;
	}
	public String toString(){
		return "int";
	}
	private IntegerAttributeType(){
		
	}

	/* (non-Javadoc)
	 * @see org.crappydbms.main.AttributeType#getSize()
	 */
	@Override
	public int getSerializedSize() {
		return Integer.SIZE / 8;
	}
	@Override
	public Field read(DataInputStream input) {
		try {
			return IntegerField.valueOf(input.readInt());
		} catch (IOException e) {
			throw new CrappyDBMSError("Cant read from in-memory byte array");
		}
	}


	@Override
	public boolean isTypeOf(Field field) {
		return field.getType().equals(this);
	}

}
