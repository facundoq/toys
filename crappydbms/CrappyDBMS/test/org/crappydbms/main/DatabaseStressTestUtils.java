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
package org.crappydbms.main;

import java.util.ArrayList;
import java.util.Random;

import org.crappydbms.exceptions.CrappyDBMSException;
import org.crappydbms.relations.StoredRelation;
import org.crappydbms.relations.fields.Field;
import org.crappydbms.relations.fields.IntegerField;
import org.crappydbms.relations.fields.StringField;
import org.crappydbms.relations.schemas.RepeatedAttributeNameException;
import org.crappydbms.relations.schemas.StoredRelationSchema;
import org.crappydbms.relations.schemas.attributes.Attribute;
import org.crappydbms.relations.schemas.attributes.AttributeType;
import org.crappydbms.relations.schemas.attributes.BaseAttribute;
import org.crappydbms.relations.schemas.attributes.IntegerAttributeType;
import org.crappydbms.relations.schemas.attributes.StringAttributeType;
import org.crappydbms.relations.tuples.StoredTuple;

/**
 * @author Facundo Manuel Quiroga
 * Jan 22, 2009
 * 
 */
public class DatabaseStressTestUtils {

	
	public static void addRandomStoredRelations(int numberOfRelations, Database database){
		ArrayList<StoredRelationSchema> schemas = createRandomStoredRelationSchemas(numberOfRelations,2,7);
		for (int i = 0; i<numberOfRelations; i++){
			StoredRelationSchema schema = schemas.get(i);
			ArrayList<Attribute> primaryKey = new ArrayList<Attribute>();
			primaryKey.add(schema.getAttribute(0));
			String name = Integer.valueOf(i).toString() ;
			try {
				database.addStoredRelation(name,schema, primaryKey);
			} catch (CrappyDBMSException e) {
				throw new CrappyDBMSTestingError("Error creating StoredRelations,, message "+e.getMessage());
			}
		}
	}
	public static ArrayList<StoredRelationSchema> createRandomStoredRelationSchemas(int numberOfSchemas, int minAttributes, int maxAttributes){
		
		ArrayList<StoredRelationSchema> schemas = new ArrayList<StoredRelationSchema>(numberOfSchemas);
		for (int i = 0 ; i<numberOfSchemas ; i++){
			StoredRelationSchema schema = generateRandomStoredRelationSchemaNamed(minAttributes,maxAttributes);
			schemas.add(schema);
		}
		return schemas;
	}

	public static StoredRelationSchema generateRandomStoredRelationSchemaNamed( int minAttributes, int maxAttributes) {
		Random random = new Random();
		int numberOfAttributes = random.nextInt(maxAttributes+1)+minAttributes;
		ArrayList<Attribute> attributes = new ArrayList<Attribute>(numberOfAttributes);
		for (int i = 0; i< numberOfAttributes ; i++){
			AttributeType attributeType;
			// randomly add an integer or a string field
			if (i< random.nextInt(numberOfAttributes)){
				attributeType = StringAttributeType.getInstance(); 
			}else{
				attributeType = IntegerAttributeType.getInstance();
			}
			Attribute attribute = new BaseAttribute(Integer.valueOf(i).toString(),attributeType);
			attributes.add(attribute);
		}
		try {
			return new StoredRelationSchema(attributes);
		} catch (RepeatedAttributeNameException e) {
			throw new CrappyDBMSTestingError("Should never throw a RepeatedAttributNameException, message "+e.getMessage());
		}
	}
	/**
	 * @param storedRelation
	 * @param numberOfTuples
	 * @return
	 */
	public static ArrayList<StoredTuple> createRandomTuplesForRelation(StoredRelation storedRelation, int numberOfTuples) {
		ArrayList<StoredTuple> tuples = new ArrayList<StoredTuple>(numberOfTuples);
		for (int tupleNumber = 0 ; tupleNumber < numberOfTuples ; tupleNumber++){
			StoredTuple tuple = DatabaseStressTestUtils.createRandomTupleForRelation(storedRelation,tupleNumber);
			tuples.add(tuple);
		}
		return tuples;
	}
	/**
	 * @param storedRelation
	 * @param tupleNumber
	 * @return
	 */
	private static StoredTuple createRandomTupleForRelation(StoredRelation storedRelation, int tupleNumber) {	
		StoredRelationSchema storedRelationSchema = storedRelation.getSchema();
		ArrayList<Field> fields = new ArrayList<Field>();
		for (AttributeType attributeType: storedRelationSchema.getAttributeTypes()){
			if (attributeType == StringAttributeType.getInstance()){
				fields.add(StringField.valueOf(Integer.valueOf(tupleNumber).toString()));
			}else if (attributeType == IntegerAttributeType.getInstance()){
				fields.add(IntegerField.valueOf(tupleNumber));
			}else{
				throw new CrappyDBMSTestingError("Invalid attribute type: "+attributeType);
			}
		}
		
		return new StoredTuple(fields,storedRelationSchema);
	}
	
	
}
