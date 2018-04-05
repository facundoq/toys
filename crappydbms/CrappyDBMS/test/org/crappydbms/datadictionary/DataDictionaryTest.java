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
package org.crappydbms.datadictionary;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.crappydbms.exceptions.CrappyDBMSException;
import org.crappydbms.main.CrappyDBMSTestingError;
import org.crappydbms.relations.StoredRelation;
import org.crappydbms.relations.schemas.RelationSchema;
import org.crappydbms.relations.schemas.RepeatedAttributeNameException;
import org.crappydbms.relations.schemas.StoredRelationSchema;
import org.crappydbms.relations.schemas.attributes.Attribute;
import org.crappydbms.relations.schemas.attributes.BaseAttribute;
import org.crappydbms.relations.schemas.attributes.IntegerAttributeType;
import org.crappydbms.relations.schemas.attributes.StringAttributeType;
import org.junit.After;
import org.junit.Before;

/**
 * @author Facundo Manuel Quiroga Oct 31, 2008
 */
public class DataDictionaryTest extends TestCase {
	protected DataDictionary dataDictionary;

	File directory;

	String[] lines = new String[] { "relation customer", "schema (name,string) (amount,int) (address,string) ", "primaryKey name ", "relation employee", "schema (name,string) (lastName,string) (address,string) (years,int)  ", "primaryKey name,lastName" };

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		// Create test file
		directory = this.createTestDirectory();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		for (File file : directory.listFiles()) {
			file.delete();
		}
		directory.delete();
	}

	public File createTestDirectory() throws IOException {

		File directory = new File("testDB/");
		directory.mkdir();
		File dataDictionaryFile = new File("testDB/dataDictionary.dat");
		PrintWriter outputStream = new PrintWriter(new FileWriter(dataDictionaryFile));
		for (String line : this.lines) {
			outputStream.println(line);
		}
		outputStream.close();
		return directory;
	}

	public DataDictionary createDataDictionary(File directory) throws CrappyDBMSException {
		try {
			DataDictionary dataDictionary = new DataDictionary(directory);
			return dataDictionary;
		} catch (IOException e1) {
			e1.printStackTrace();
			Assert.fail("should not throw a IOException");
		} catch (ParseException e1) {
			Assert.fail(e1.getMessage());
		} catch (CrappyDBMSException e) {
			Assert.fail(e.getMessage());
		}
		throw new CrappyDBMSException();
	}

	public void testStoredRelationAgainst(DataDictionary dataDictionary, String name, RelationSchema expectedRelationSchema, ArrayList<Attribute> primaryKey) {
		StoredRelation storedRelation;
		try {
			storedRelation = dataDictionary.getStoredRelation(name);
			Assert.assertTrue(expectedRelationSchema.equals(storedRelation.getSchema()));
			Assert.assertTrue(storedRelation.getPrimaryKey().equals(primaryKey));

		} catch (CrappyDBMSException e) {
			Assert.fail("Relation " + name + " should exist!");
		}
	}

	public void testNew() throws FileNotFoundException {
		try {
			dataDictionary = this.createDataDictionary(directory);
		} catch (CrappyDBMSException e1) {
			// should never get here
		}
		Assert.assertEquals("wrong database name", "testDB", dataDictionary.getDatabaseName());
		Assert.assertEquals("Should have two relations", 2, dataDictionary.getStoredRelations().size());

		this.doTestCustomerRelation(dataDictionary);

		this.doTestEmployeeRelation(dataDictionary);
	}

	protected void doTestCustomerRelation(DataDictionary dataDictionary) {
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		Attribute attribute;

		attribute = new BaseAttribute("name", StringAttributeType.getInstance());
		attributes.add(attribute);
		attribute = new BaseAttribute("amount", IntegerAttributeType.getInstance());
		attributes.add(attribute);
		attribute = new BaseAttribute("address", StringAttributeType.getInstance());
		attributes.add(attribute);
		RelationSchema customerSchema;
		try {
			customerSchema = new StoredRelationSchema(attributes);
		} catch (RepeatedAttributeNameException e) {
			throw new CrappyDBMSTestingError("Attribute list does not have repeated attributes!");
		}

		ArrayList<Attribute> primaryKey = new ArrayList<Attribute>();
		attribute = new BaseAttribute("name", StringAttributeType.getInstance());
		primaryKey.add(attribute);
		this.testStoredRelationAgainst(dataDictionary, "customer", customerSchema, primaryKey);
	}

	protected void doTestEmployeeRelation(DataDictionary dataDictionary) {
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		Attribute attribute;
		attribute = new BaseAttribute("name", StringAttributeType.getInstance());
		attributes.add(attribute);
		attribute = new BaseAttribute("lastName", StringAttributeType.getInstance());
		attributes.add(attribute);
		attribute = new BaseAttribute("address", StringAttributeType.getInstance());
		attributes.add(attribute);
		attribute = new BaseAttribute("years", IntegerAttributeType.getInstance());
		attributes.add(attribute);
		RelationSchema relationSchema;
		try {
			relationSchema = new StoredRelationSchema(attributes);
		} catch (RepeatedAttributeNameException e) {
			throw new CrappyDBMSTestingError("Attribute list does not have repeated attributes!");
		}

		ArrayList<Attribute> primaryKey = new ArrayList<Attribute>();
		attribute = new BaseAttribute("name", StringAttributeType.getInstance());
		primaryKey.add(attribute);
		attribute = new BaseAttribute("lastName", StringAttributeType.getInstance());
		primaryKey.add(attribute);
		this.testStoredRelationAgainst(dataDictionary, "employee", relationSchema, primaryKey);
	}

	public void testAddRelation() throws IOException, CrappyDBMSException {
		try {
			dataDictionary = this.createDataDictionary(directory);
		} catch (CrappyDBMSException e1) {
			// should never get here
		}
		try {
			DataDictionaryTest.createTestingStoredRelation(dataDictionary);
		} catch (CrappyDBMSException e1) {
			Assert.fail("StoredRelation 'assets' should not exist yet!");
		}
		this.doTestCustomerRelation(dataDictionary);
		this.doTestEmployeeRelation(dataDictionary);
		try {
			dataDictionary.getStoredRelation("assets");
			this.doTestAssetsRelation(dataDictionary);
		} catch (CrappyDBMSException e) {
			Assert.fail("StoredRelation 'assets' should exist!");
		}
		DataDictionary anotherDataDictionary = this.createDataDictionary(directory);
		StoredRelation assets = anotherDataDictionary.getStoredRelation("assets");

		RelationSchema assetsSchema = assets.getSchema();
		Assert.assertEquals("should have 4 attributes", 4, assetsSchema.getAttributes().size());
		this.doTestAssetsRelation(anotherDataDictionary);
		this.doTestCustomerRelation(anotherDataDictionary);
		this.doTestEmployeeRelation(anotherDataDictionary);

	}

	protected void doTestAssetsRelation(DataDictionary dataDictionary) {
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		ArrayList<Attribute> primaryKey = new ArrayList<Attribute>();
		Attribute attribute;
		attribute = new BaseAttribute("name", StringAttributeType.getInstance());
		attributes.add(attribute);
		primaryKey.add(attribute);
		attribute = new BaseAttribute("date", StringAttributeType.getInstance());
		attributes.add(attribute);
		attribute = new BaseAttribute("manager", StringAttributeType.getInstance());
		attributes.add(attribute);
		primaryKey.add(attribute);
		attribute = new BaseAttribute("value", IntegerAttributeType.getInstance());
		attributes.add(attribute);
		RelationSchema schema;
		try {
			schema = new StoredRelationSchema(attributes);
		} catch (RepeatedAttributeNameException e) {
			throw new CrappyDBMSTestingError("Attribute list does not have repeated attributes!");
		}

		this.testStoredRelationAgainst(dataDictionary, "assets", schema, primaryKey);

	}

	public static void createTestingStoredRelation(DataDictionary dataDictionary) throws IOException, CrappyDBMSException {
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		ArrayList<Attribute> primaryKey = new ArrayList<Attribute>();
		Attribute attribute;
		attribute = new BaseAttribute("name", StringAttributeType.getInstance());
		attributes.add(attribute);
		primaryKey.add(attribute);
		attribute = new BaseAttribute("date", StringAttributeType.getInstance());
		attributes.add(attribute);
		attribute = new BaseAttribute("manager", StringAttributeType.getInstance());
		attributes.add(attribute);
		primaryKey.add(attribute);
		attribute = new BaseAttribute("value", IntegerAttributeType.getInstance());
		attributes.add(attribute);
		try {
			StoredRelationSchema schema = new StoredRelationSchema(attributes);
			dataDictionary.addStoredRelation("assets", schema, primaryKey);
		} catch (RepeatedAttributeNameException e) {
			throw new CrappyDBMSTestingError("Attribute list does not have repeated attributes!");
		}

	}

}
