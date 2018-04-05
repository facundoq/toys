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
package org.crappydbms.queries.operators.projection;

import java.util.ArrayList;

import junit.framework.Assert;

import org.crappydbms.exceptions.InvalidAttributeNameException;
import org.crappydbms.main.CrappyDBMSTestCase;
import org.crappydbms.relations.fields.IntegerField;
import org.crappydbms.relations.fields.StringField;
import org.crappydbms.relations.schemas.StoredRelationSchema;
import org.crappydbms.relations.tuples.Tuple;

/**
 * @author Facundo Manuel Quiroga
 * Dec 8, 2008
 * 
 */
public class ProjectedRelationSchemaTest extends CrappyDBMSTestCase {

	Tuple innerTuple;
	StoredRelationSchema innerSchema;
	protected ArrayList<String> removedAttributeNamesName;
	protected ArrayList<String> removedAttributeNamesValue;
	
	protected void setUp() throws Exception {
		innerSchema = CrappyDBMSTestCase.createAssetsRelationSchema();
		innerTuple = CrappyDBMSTestCase.createRandomTuplesForAssetsRelation(1, innerSchema).get(0);
		
		removedAttributeNamesName = new ArrayList<String>();
		removedAttributeNamesName.add("name");
		removedAttributeNamesValue = new ArrayList<String>();
		removedAttributeNamesValue.add("value");
	}

	protected void tearDown() throws Exception {
		innerSchema = null;
		innerTuple = null;
		removedAttributeNamesName = null;
		removedAttributeNamesValue = null;
	}
	public void testNew(){
		ProjectedRelationSchema projectedRelationSchema = new ProjectedRelationSchema(innerSchema,removedAttributeNamesName);
		Assert.assertFalse("Should not contain name attribute",projectedRelationSchema.getAttributeNames().contains("name"));
		Assert.assertEquals("Should have just 3 attributes",3, projectedRelationSchema.getNumberOfAttributes());
		Assert.assertTrue("Should contain value attribute",projectedRelationSchema.getAttributeNames().contains("value"));
		
		projectedRelationSchema = new ProjectedRelationSchema(innerSchema,removedAttributeNamesValue);
		Assert.assertFalse("Should not contain value attribute",projectedRelationSchema.getAttributeNames().contains("value"));
		Assert.assertEquals("Should have just 3 attributes",3, projectedRelationSchema.getNumberOfAttributes());
		Assert.assertTrue("Should contain name attribute",projectedRelationSchema.getAttributeNames().contains("name"));
	}
	
	public void testTuple(){
		ProjectedRelationSchema projectedRelationSchema = new ProjectedRelationSchema(innerSchema,removedAttributeNamesName);
		ProjectedTuple projectedTuple = new ProjectedTuple(projectedRelationSchema,innerTuple);
		try {
			projectedTuple.getFieldNamed("name");
			Assert.fail("name is an invalid field!");
		} catch (InvalidAttributeNameException e) {
		}
		try {
			 IntegerField value = (IntegerField) projectedTuple.getFieldNamed("value");
			 Assert.assertEquals("value should be 10", 10 ,value.getValue().intValue());
		} catch (InvalidAttributeNameException e) {
			Assert.fail("value, date and manager attributes are valid for projectedRelationTuple");
		}
		try {
			 StringField date = (StringField) projectedTuple.getFieldNamed("date");
			 Assert.assertEquals("Dates should be 15/12/11","15/12/11",date.getValue() );
		} catch (InvalidAttributeNameException e) {
			Assert.fail("value, date and manager attributes are valid for projectedRelationTuple");
		}
		try {
			StringField manager = (StringField) projectedTuple.getFieldNamed("manager");
			 Assert.assertEquals("Manager should be pedrito","pedrito",manager.getValue() );
		} catch (InvalidAttributeNameException e) {
			Assert.fail("value, date and manager attributes are valid for projectedRelationTuple");
		}
		Assert.assertEquals("projectedTuple should have 3 fields", 3, projectedTuple.numberOfFields());
		projectedTuple.getFields();
		
	}

	
}
