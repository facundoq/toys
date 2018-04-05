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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import junit.framework.Assert;

import org.crappydbms.main.CrappyDBMSTestCase;
import org.crappydbms.relations.schemas.attributes.StringAttributeType;

/**
 * @author Facundo Manuel Quiroga
 * Nov 23, 2008
 * 
 */
public class StringFieldTest extends CrappyDBMSTestCase {

	protected StringField stringField;
	String value = "asdads";
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		stringField = StringField.valueOf(value);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test method for {@link org.crappydbms.relations.fields.StringField#serializeTo(java.io.DataOutputStream)}.
	 * @throws IOException 
	 */
	public void testSerializeTo() throws IOException {
		int byteCount = StringAttributeType.getInstance().getSerializedSize();
		ByteArrayOutputStream pageBAOS = new ByteArrayOutputStream(byteCount);
		DataOutputStream pageStream = new DataOutputStream(pageBAOS);
		stringField.serializeTo(pageStream);
		byte[] bytes = pageBAOS.toByteArray();
		Assert.assertEquals("Wrong number of bytes written",byteCount, bytes.length);
		Assert.assertEquals("Wrong string size",value.length(), bytes[0]);
		for (int i = 0 ; i<value.length();i++){
			byte expected = (byte) value.charAt(i);
			Assert.assertEquals("Individual char/bytes should match",expected, bytes[i+1] );
		}
		
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		DataInputStream dis = new DataInputStream(bais);
		StringField stringField = (StringField) StringAttributeType.getInstance().read(dis);		
		Assert.assertEquals("Decoded string should match original string",value, stringField.getValue());
	}

}
