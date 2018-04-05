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
/**Opens a DataDictionary file and loads the relations from it.
 * 
 * @author Facundo Manuel Quiroga
 * Nov 6, 2008
 * 
 */
package org.crappydbms.datadictionary;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import org.crappydbms.exceptions.CrappyDBMSException;
import org.crappydbms.exceptions.InvalidAttributeNameException;
import org.crappydbms.relations.schemas.AbstractRelationSchema;
import org.crappydbms.relations.schemas.StoredRelationSchema;
import org.crappydbms.relations.schemas.attributes.Attribute;
import org.crappydbms.relations.schemas.attributes.AttributeType;
import org.crappydbms.relations.schemas.attributes.BaseAttribute;
import org.crappydbms.relations.schemas.attributes.IntegerAttributeType;
import org.crappydbms.relations.schemas.attributes.StringAttributeType;

public class DataDictionaryParser {
    
    protected File file;
    protected DataDictionary dataDictionary;
    public static DataDictionaryParser newDataDictionaryParser(DataDictionary dataDictionary){
	return new DataDictionaryParser(dataDictionary);
    }
    private DataDictionaryParser(DataDictionary dataDictionary){
    	this.setDataDictionary(dataDictionary);
    }

    /**
	 * @param inputStream from where to read lines 
	 * @param currentLine 
	 * @return a newly created StoredRelation, null if file ends
	 * @throws IOException, ParseException if the file contents are invalid-
	 * @throws ParseException 
     * @throws CrappyDBMSException 
	 */
	protected boolean parseStoredRelation( BufferedReader inputStream, int currentLine) throws IOException, ParseException, CrappyDBMSException {
		
		String name = this.parseStoredRelationName(inputStream,currentLine);
		if (name == null){
			return false;
		}
		ArrayList<Attribute> attributes = this.parseStoredRelationAttributes(inputStream,currentLine);
		StoredRelationSchema schema = new StoredRelationSchema(attributes);
		
		String[] primaryKeyAttributeNames = this.parseStoredRelationPrimaryKey(inputStream,currentLine);
		if (primaryKeyAttributeNames.length== 0){
			throw new ParseException("Must define a set of attributes as primary key!",currentLine);
		}
		ArrayList<Attribute> primaryKey = this.attributeNamesToPrimaryKey(primaryKeyAttributeNames,schema,currentLine);
		this.getDataDictionary().addStoredRelation(name, schema, primaryKey);
		return true;
	}
	/**
	 * @param primaryKeyAttributeNames 
	 * @param relationSchema
	 * @param currentLine
	 * @return a LinkedHashSet of the attributes that compose the primary key
	 * @throws ParseException if there is an attribute name of the primary key that is non-existent in the list of attributes of the schema 
	 */
	protected ArrayList<Attribute> attributeNamesToPrimaryKey(String[] primaryKeyAttributeNames, AbstractRelationSchema relationSchema,
			int currentLine) throws ParseException {
		ArrayList<Attribute> primaryKey = new ArrayList<Attribute>();
		for (String attributeName : primaryKeyAttributeNames){
			try {
				Attribute attribute = relationSchema.attributeNamed(attributeName);
				primaryKey.add(attribute);
			} catch (InvalidAttributeNameException e) {
				throw new ParseException("Invalid primary key attribute name: "+attributeName,currentLine);
			}
		}
		return primaryKey;
	}
	/**
	 * @param inputStream
	 * @param currentLine
	 * @return a LinkedHashSet of attributes that compose the PK
	 * @throws IOException 
	 * @throws ParseException 
	 */
	protected String[] parseStoredRelationPrimaryKey(BufferedReader inputStream, int currentLine) throws IOException, ParseException {

		String primaryKeyLine = inputStream.readLine().trim();
		String expectedToken = new String("primaryKey");
		String relationToken = primaryKeyLine.substring(0,expectedToken.length());		
		if (!relationToken.equals(expectedToken)){
			throw new ParseException("Wrong relation syntax: should be 'primaryKey <attribute list>', got "+relationToken+"instead of primaryKey",currentLine);
		}
		String attributeTokens = primaryKeyLine.substring(expectedToken.length()+1).trim();  
		String[] attributeNames = attributeTokens.split(",") ;
		return attributeNames;
	}
	/**
	 * @param inputStream
	 * @param currentLine 
	 * @return 
	 * @return LinkedHashSet of attributes
	 * @throws IOException 
	 * @throws ParseException 
	 */
	protected ArrayList<Attribute> parseStoredRelationAttributes(BufferedReader inputStream, int currentLine) throws IOException, ParseException {
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		String relationNameLine = inputStream.readLine().trim();
		String expectedToken = new String("schema");
		String relationToken = relationNameLine.substring(0,expectedToken.length());		
		if (!relationToken.equals(expectedToken)){
			throw new ParseException("Wrong relation syntax: should be 'schema <attribute list>', got "+relationToken+"instead of schema",currentLine);
		}
		relationNameLine = relationNameLine.substring(7).trim();
		relationNameLine = relationNameLine.replaceAll("[ ]*", "");
		String[] attributeTuples = relationNameLine.split("[()]+");
		// dont use the first tuple which is always empty cos split just works like that
		for (int i=1; i<attributeTuples.length;i++){
			String tuple = attributeTuples[i];
			String[] pairNameValue = tuple.split(",");
			if (pairNameValue.length !=2){
				throw new ParseException("Wrong attribute definition",currentLine);
			}
			if (pairNameValue[0] == null || pairNameValue[1] == null || pairNameValue[0].length()<1 || pairNameValue[1].length()<1){
				throw new ParseException("Wrong attribute;  null or empty name/values",currentLine);				
			}
			AttributeType attributeType;
			if (pairNameValue[1].equals("string")){
				attributeType = StringAttributeType.getInstance();
			}else if (pairNameValue[1].equals("int")){
				attributeType = IntegerAttributeType.getInstance();
			}else {
				throw new ParseException("Invalid type "+pairNameValue[1],currentLine);
			}
			attributes.add(new BaseAttribute(pairNameValue[0],attributeType));
		}
		
		return attributes;
	}
	/**
	 * @param inputStream
	 * @return the name of the relation, null if file ends
	 * @throws ParseException 
	 * @throws IOException 
	 */
	private String parseStoredRelationName(BufferedReader inputStream,int currentLine) throws ParseException, IOException {
		
		String relationNameLine = inputStream.readLine();
		if (relationNameLine == null || relationNameLine.length() < 1) {
			return null;
		}
		String[] relationNameSplit = relationNameLine.trim().split("[ ]+");
		if (relationNameSplit.length != 2 || relationNameSplit[0]== null || relationNameSplit[1] == null ){
			throw new ParseException("Invalid relation name line: wrong syntax, should be 'relation <name>' , line "+currentLine, currentLine);					
		}
		if ( ! relationNameSplit[0].equals("relation") ){
			throw new ParseException("Invalid relation name line: wrong syntax, first word should be 'relation', syntax = 'relation <name> ', line "+currentLine, currentLine);
		}
		if ( relationNameSplit[1].length() < 3 ){
			throw new ParseException("Invalid relation name line: name must be 3 chars or more, line "+currentLine,currentLine);
		}
		return relationNameSplit[1];	
	}

	
	public DataDictionary getDataDictionary() {
		return this.dataDictionary;
	}
	public void setDataDictionary(DataDictionary dataDictionary) {
		this.dataDictionary = dataDictionary;
	}
}
