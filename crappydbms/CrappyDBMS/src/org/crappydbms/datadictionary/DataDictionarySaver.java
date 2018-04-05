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
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import org.crappydbms.relations.StoredRelation;
import org.crappydbms.relations.schemas.attributes.Attribute;

/**
 * @author Facundo Manuel Quiroga
 * Nov 6, 2008
 * 
 */
public class DataDictionarySaver {

    /**
     * @param file 
     * @param databaseName
     * @param storedRelations
     * @throws IOException 
     */
    public void save(File file, String databaseName, Map<String, StoredRelation> storedRelations) throws IOException {
	PrintWriter outputStream = new PrintWriter(new FileWriter(file));
	for (StoredRelation storedRelation : storedRelations.values()){
	    outputStream.println("relation "+ storedRelation.getName());
	    outputStream.println(this.serializeStoredRelationSchema(storedRelation));
	    outputStream.println(this.serializeStoredRelationPrimaryKey(storedRelation));
	}
	outputStream.close();
    }

    /**
     * @param storedRelation
     * @return
     */
    protected String serializeStoredRelationPrimaryKey(StoredRelation storedRelation) {
	String result = "primaryKey ";
	for (Attribute attribute : storedRelation.getPrimaryKey() ){
	    result = result + attribute.getName()+",";
	}
	result = result.substring(0, result.length()-1);
	return result;
    }

    /**
     * @param storedRelation
     * @return
     */
    protected String serializeStoredRelationSchema(StoredRelation storedRelation) {
	String result = "schema ";
	for (Attribute attribute : storedRelation.getSchema().getAttributes() ){
	    result = result +"("+ attribute.getName()+","+attribute.getType().toString()+") ";
	}
	result = result.substring(0, result.length()-1);
	return result;
    }

}
