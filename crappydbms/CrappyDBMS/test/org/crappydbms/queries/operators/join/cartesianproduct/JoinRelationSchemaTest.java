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
package org.crappydbms.queries.operators.join.cartesianproduct;

import java.util.ArrayList;

import junit.framework.Assert;

import org.crappydbms.exceptions.InvalidAttributeNameException;
import org.crappydbms.main.CrappyDBMSTestCase;
import org.crappydbms.main.CrappyDBMSTestingError;
import org.crappydbms.queries.operators.join.RelationSchemasHaveCommonAttributesException;
import org.crappydbms.relations.schemas.RelationSchema;
import org.crappydbms.relations.schemas.RepeatedAttributeNameException;
import org.crappydbms.relations.schemas.StoredRelationSchema;
import org.crappydbms.relations.schemas.attributes.Attribute;
import org.crappydbms.relations.schemas.attributes.BaseAttribute;
import org.crappydbms.relations.schemas.attributes.IntegerAttributeType;
import org.crappydbms.relations.schemas.attributes.StringAttributeType;

/**
 * @author Facundo Manuel Quiroga
 * Dec 11, 2008
 * 
 */
public class JoinRelationSchemaTest extends CrappyDBMSTestCase {

	RelationSchema assetsRelationSchema;
	RelationSchema customersRelationSchema;
	
	protected void setUp() throws Exception {
		assetsRelationSchema = CrappyDBMSTestCase.createAssetsRelationSchema();
		customersRelationSchema = CrappyDBMSTestCase.createCustomersRelationSchema(); 
		
	}
	
	public void testNewWithCommonAttributes(){
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		Attribute attribute;

		attribute = new BaseAttribute("name", StringAttributeType.getInstance());
		attributes.add(attribute);
		attribute = new BaseAttribute("house", IntegerAttributeType.getInstance());
		attributes.add(attribute);
		attribute = new BaseAttribute("telephone", StringAttributeType.getInstance());
		attributes.add(attribute);
		RelationSchema mockSchema;
		try {
			mockSchema = new StoredRelationSchema(attributes);
		} catch (RepeatedAttributeNameException e1) {
			throw new CrappyDBMSTestingError("There should not be any repeated attributes");
		}	
		try {
			new JoinRelationSchema(assetsRelationSchema,mockSchema);
			Assert.fail("Schemas do have common attributes");
		} catch (RelationSchemasHaveCommonAttributesException e) {}
		
		try {
			new JoinRelationSchema(mockSchema,assetsRelationSchema);
			Assert.fail("Schemas do have common attributes");
		} catch (RelationSchemasHaveCommonAttributesException e) {}
		
		try {
			new JoinRelationSchema(assetsRelationSchema,assetsRelationSchema);
			Assert.fail("Schemas do have common attributes");
		} catch (RelationSchemasHaveCommonAttributesException e) {}

	}
	
	public void testNew(){
		try {
			new JoinRelationSchema(assetsRelationSchema,customersRelationSchema);
		} catch (RelationSchemasHaveCommonAttributesException e) {
			Assert.fail("Schemas do not have common attributes");
		}
		
		try {
			new JoinRelationSchema(customersRelationSchema,assetsRelationSchema);
		} catch (RelationSchemasHaveCommonAttributesException e) {
			Assert.fail("Schemas do not have common attributes");
		}
	}
	
	public void testOperations() throws Exception {
		JoinRelationSchema joinRelationSchema = new JoinRelationSchema(assetsRelationSchema,customersRelationSchema);
		int expectedNumberOfAttributes = assetsRelationSchema.getAttributes().size()+customersRelationSchema.getAttributes().size();
		Assert.assertEquals( "Resulting number of attributes does not match", expectedNumberOfAttributes, joinRelationSchema.getNumberOfAttributes());
		
		Assert.assertEquals(0,joinRelationSchema.getPositionOfAttributeNamed("name"));
		Assert.assertEquals(1,joinRelationSchema.getPositionOfAttributeNamed("date"));
		Assert.assertEquals(2,joinRelationSchema.getPositionOfAttributeNamed("manager"));
		Assert.assertEquals(3,joinRelationSchema.getPositionOfAttributeNamed("value"));
		Assert.assertEquals(4,joinRelationSchema.getPositionOfAttributeNamed("customerName"));
		Assert.assertEquals(5,joinRelationSchema.getPositionOfAttributeNamed("amount"));
		Assert.assertEquals(6,joinRelationSchema.getPositionOfAttributeNamed("address"));
		try{
			joinRelationSchema.getPositionOfAttributeNamed("wrongAttribute");
			Assert.fail("'wrongAttribute' is not a valid attribute");
		}catch (InvalidAttributeNameException e){}
		
	}
	protected void tearDown() throws Exception {

	}

}
