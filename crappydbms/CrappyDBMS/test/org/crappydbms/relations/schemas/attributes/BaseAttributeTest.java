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

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * @author Facundo Manuel Quiroga Nov 5, 2008
 * 
 */
public class BaseAttributeTest extends TestCase {
	Attribute nameString;

	Attribute nameString2;

	Attribute nameInt;

	Attribute addressString;

	public void setUp() throws Exception {
		nameString = new BaseAttribute("name", StringAttributeType.getInstance());
		nameString2 = new BaseAttribute("name", StringAttributeType.getInstance());
		nameInt = new BaseAttribute("name", IntegerAttributeType.getInstance());
		addressString = new BaseAttribute("adress", StringAttributeType.getInstance());
	}

	public void testEquals() {
		Assert.assertNotSame("Two attributes with different types should not be equal", nameString, nameInt);
		Assert.assertNotSame("Two attributes with different names should not be equal", nameString, addressString);
		Assert.assertNotSame("Two attributes with different names and types should not be equal", nameInt, addressString);
		Assert.assertEquals("Two attributes with the same name and type should be equal", nameString, nameString2);

	}

	public void testHashCode() {
		// not equal attributes can have the same hashCode, so we only test with
		// equals
		Assert.assertEquals("Equal attributes should have the same hashcode", nameString.hashCode(), nameString2.hashCode());
	}

	public void testMatch() {
		Assert.assertTrue("Attributes with the same name and type should match", nameString.matches(nameString2));
		Assert.assertTrue("Attributes with the same name should match", nameString.matches(nameInt));
		Assert.assertFalse("Attributes with different names should not match", addressString.matches(nameInt));
	}
	
	public void testIsCompatibleWith() {
		Assert.assertTrue("Attributes with the same name and type should be compatible", nameString.isCompatibleWith(nameString2));
		Assert.assertTrue("Attributes with the same type should be compatible", nameString.isCompatibleWith(addressString));
		Assert.assertFalse("Attributes with different types should not be compatible", addressString.isCompatibleWith(nameInt));
	}

	public void tearDown() throws Exception {
	}

}
