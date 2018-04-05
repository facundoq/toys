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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.crappydbms.dbfiles.heapfile.HeapFile;
import org.crappydbms.exceptions.CrappyDBMSError;
import org.crappydbms.exceptions.CrappyDBMSException;
import org.crappydbms.main.Database;
import org.crappydbms.relations.StoredRelation;
import org.crappydbms.relations.schemas.StoredRelationSchema;
import org.crappydbms.relations.schemas.attributes.Attribute;

/**
 * Stores the information about tables, their attributes and primary key. Syntax
 * of file is: database <name> { relation <name> schema <attributes> primaryKey
 * <attributeNames> }* where attributes := (<name>,<type>) | attribute
 * (<name>,<type>) attributeNames := <name> | attributeNames , <name> name :=
 * any alphanumerical string type := int | string
 * 
 * @author Facundo Manuel Quiroga Oct 31, 2008
 * 
 */
public class DataDictionary {

	protected Map<String, StoredRelation> storedRelations;

	protected Database database;
	protected File dataDictionaryFile;

	protected File directory;

	/**
	 * @param file
	 *            from where to load the DataDictionary info
	 * @throws ParseException
	 * @throws IOException
	 * @throws CrappyDBMSException
	 * 
	 */
	public DataDictionary(File directory) throws IOException, ParseException, CrappyDBMSException {
		this.setDatabase(null);
		this.setStoredRelations(new HashMap<String, StoredRelation>());
		this.setDirectory(directory);
		if (!directory.isDirectory() || !directory.canWrite() || !directory.canRead()) {
			throw new CrappyDBMSError("Invalid directory file");
		}
		File dataDictionaryFile =new File(directory.getAbsolutePath() + "/dataDictionary.dat"); 
		this.setDataDictionaryFile(dataDictionaryFile);
		if (!dataDictionaryFile.exists()){
		    this.createEmptyDataDictionary();
		}
		this.loadStoredRelations();
	}

	/** Creates a data dictionary with no relations, and the database name "empty" 
	 * @throws IOException 
	 * 
	 */
	protected void createEmptyDataDictionary() throws IOException {
	    File dataDictionaryFile = this.getDataDictionaryFile();
	    dataDictionaryFile.createNewFile();
	    /*PrintWriter outputStream = new PrintWriter(new FileWriter(dataDictionaryFile));
	    outputStream.println("database empty");
	    outputStream.close();*/
	}

	/**
	 * Load all the stored relations described in the DataDictionary's file, and create
	 * the necessary StoredRelation objects
	 * 
	 * @throws IOException
	 * @throws ParseException
	 * @throws CrappyDBMSException
	 * 
	 */
	protected void loadStoredRelations() throws IOException, ParseException, CrappyDBMSException {
		BufferedReader inputStream = new BufferedReader(new FileReader(this.getDataDictionaryFile()));
		//inputStream.readLine();
		int currentLine = 0;
		DataDictionaryParser dataDictionaryParser = DataDictionaryParser.newDataDictionaryParser(this);
		
		//currentLine = 1;
		boolean ok = true;
		while (ok) {
			ok = dataDictionaryParser.parseStoredRelation(inputStream, currentLine);
			currentLine += 3;
		}
		inputStream.close();

	}

	

	/**
	 * @param name
	 * @param dataDictionary 
	 * @return
	 * @throws IOException 
	 */
	protected HeapFile createDBHeapFileNamed(String name, StoredRelationSchema relationSchema) throws IOException {
		File file = new File(this.getDirectory().getAbsolutePath() + "/" + name);
		return new HeapFile(file,relationSchema,this);
	}



	/**
	 * serialize the contents of the DataDictionary into a file.
	 * 
	 * @throws IOException
	 */
	public void save() throws IOException {
		new DataDictionarySaver().save(this.getDataDictionaryFile(), this.getDatabaseName(), this.getStoredRelations());
	}

	/**
	 * @param string
	 * @throws CrappyDBMSException
	 */
	public synchronized StoredRelation getStoredRelation(String name) throws InvalidStoredRelationNameException {
		StoredRelation storedRelation = this.getStoredRelations().get(name);
		if (storedRelation == null) {
			throw new InvalidStoredRelationNameException("could not find StoredRelation" + name);
		}
		return storedRelation;
	}

	/**
	 * @return a Map of the stored relations in the database
	 */
	public Map<String, StoredRelation> getStoredRelations() {
		return this.storedRelations;
	}
	/**
	 * @param database 
	 * @param storedRelation
	 *            to add to DataDictionary
	 * @throws IOException
	 * @throws CrappyDBMSException
	 */
	public synchronized StoredRelation addStoredRelation(String name, StoredRelationSchema schema,List<Attribute> primaryKey)throws  CrappyDBMSException {
		if (this.getStoredRelations().get(name) != null) {
			throw new CrappyDBMSException("A StoredRelation named " + name + " already exists");
		}
		HeapFile dbHeapFile;
		try {
			dbHeapFile = this.createDBHeapFileNamed(name+".dat",schema);
			StoredRelation storedRelation = new StoredRelation(name, schema, primaryKey, dbHeapFile);
			this.getStoredRelations().put(name, storedRelation);
			this.save();
		return storedRelation;
		} catch (IOException e) {
			throw new CrappyDBMSException("Could not create StoredRelation, IOException " +e.getMessage());
		}
	}
	
	public synchronized void removeStoredRelation(StoredRelation storedRelation) throws IOException {
		this.getStoredRelations().remove(storedRelation.getName());
		storedRelation.removeSelf();
		this.save();
	}
	
	public String getDatabaseName() {
		return this.getDirectory().getName();
	}

	protected void setStoredRelations(Map<String, StoredRelation> storedRelations) {
		this.storedRelations = storedRelations;
	}

	public File getDirectory() {
		return this.directory;
	}

	protected void setDirectory(File directory) {
		this.directory = directory;
	}

	public File getDataDictionaryFile() {
		return this.dataDictionaryFile;
	}

	protected void setDataDictionaryFile(File dataDictionaryFile) {
		this.dataDictionaryFile = dataDictionaryFile;
	}

	public Database getDatabase() {
		if (this.database == null){
			throw new CrappyDBMSError("Database was not set for the data dictionary");
		}
		return this.database;
	}

	public void setDatabase(Database database) {
		this.database = database;
	}

	public void shutDown() {
		// tell heapFiles to shut down?
	}

}
