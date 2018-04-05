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
import org.crappydbms.relations.fields.StringField;


/**
 * @author Facundo Manuel Quiroga
 * Nov 4, 2008
 * 
 */
public class StringAttributeType implements AttributeType {
	
	protected static StringAttributeType instance; 
	public static StringAttributeType getInstance(){
		if (StringAttributeType.instance == null){
			StringAttributeType.instance = new StringAttributeType();
		}
		return StringAttributeType.instance;
	}
	
	private StringAttributeType(){
		
	}

	public String toString(){
		return "string";
	}
	
	@Override
	public int getSerializedSize() {
		return 50;
	}
	public int getMaximumLength(){
		return 49;
	}
	@Override
	public Field read(DataInputStream input) throws IOException {
		int size = this.getSerializedSize(); 
		byte[] bytes = new byte[size];
		if ( input.read(bytes, 0, size) != size ){
			throw new CrappyDBMSError("Cant read from in-memory byte array");
		}else{
			byte stringLength = bytes[0];
			if (stringLength>this.getMaximumLength()){
				throw new CrappyDBMSError("String length ("+stringLength+") surpasses maximum length ("+this.getMaximumLength()+")");
			}
			byte[] string = new byte[stringLength];
			for (int i = 0; i<stringLength; i++){
				string[i] = bytes[i+1];
			}
			return StringField.valueOf(new String(string));
		}
	}
	
	@Override
	public boolean isTypeOf(Field field) {
		return StringField.class.isInstance(field);
	}


}
