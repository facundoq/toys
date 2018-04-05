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
package org.crappydbms.relations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.crappydbms.dbfiles.DBFile;
import org.crappydbms.exceptions.CrappyDBMSError;
import org.crappydbms.main.PageBufferFullException;
import org.crappydbms.relations.schemas.RepeatedAttributeNameException;
import org.crappydbms.relations.schemas.StoredRelationSchema;
import org.crappydbms.relations.schemas.attributes.Attribute;
import org.crappydbms.relations.tuples.StoredTuple;
import org.crappydbms.relations.tuples.Tuple;
import org.crappydbms.transactions.LockMode;
import org.crappydbms.transactions.Transaction;
import org.crappydbms.transactions.TransactionAbortedException;

/**
 * Represents a Relation that is stored in the hard drive.
 * @author Facundo Manuel Quiroga Oct 31, 2008
 * 
 */
public class StoredRelation implements Relation {

	protected String name;

	protected StoredRelationSchema schema;

	protected ArrayList<Attribute> primaryKey;

	protected DBFile DBFile;

	/**
	 * @param relationName
	 * @param relationSchema
	 * @param primaryKey
	 * @throws RepeatedAttributeNameException
	 *           if the primary key has repeated attribute names
	 */
	public StoredRelation(String name, StoredRelationSchema schema, List<Attribute> primaryKey, DBFile DBFile)
			throws RepeatedAttributeNameException {
		this.setName(name);
		this.setDBFile(DBFile);
		this.setSchema(schema);
		this.setPrimaryKey(new ArrayList<Attribute>());
		for (Attribute attribute : primaryKey) {
			if (this.primaryKeyhasAttributeNamed(attribute.getName())) {
				throw new RepeatedAttributeNameException("The attribute name " + attribute.getName() + "was repeated");
			}
			this.getPrimaryKey().add(attribute);
		}
	}

	public boolean primaryKeyhasAttributeNamed(String name) {
		for (Attribute attribute : this.getPrimaryKey()) {
			if (attribute.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public StoredRelationSchema getSchema() {
		return this.schema;
	}

	protected void setName(String name) {
		this.name = name;
	}

	protected void setSchema(StoredRelationSchema schema) {
		this.schema = schema;
	}

	public DBFile getDBFile() {
		return this.DBFile;
	}

	protected void setDBFile(DBFile file) {
		this.DBFile = file;
	}

	public void addTuple(Tuple tuple,Transaction transaction) throws  TransactionAbortedException {
		if (!this.getSchema().isCompatible(tuple)) {
			String message = "Tuple and StoredRelation relation schema do not match. Tuple: "+tuple.getRelationSchema()+" this:"+this.getSchema();
			throw new CrappyDBMSError(message);
		}
		StoredTuple storedTuple = tuple.convertToStoredTuple(this.getSchema());
		try {
			this.getDBFile().addTuple(storedTuple,transaction);
		} catch (PageBufferFullException e) {
			transaction.abort("Page buffer full");
		} catch (IOException e) {
			transaction.abort("Error adding tuple: IOException: "+e.getMessage());
		}
	}

	
	public StoredRelationIterator<Tuple> iterator(Transaction transaction, LockMode lockMode) throws TransactionAbortedException {
		return this.getDBFile().iterator(transaction,lockMode);
	}
	@Override
	public StoredRelationIterator<Tuple> iterator(Transaction transaction) throws TransactionAbortedException {
		return this.getDBFile().iterator(transaction,LockMode.shared());
	}

	public void removeSelf() {
		this.getDBFile().removeSelf();
	}

	public ArrayList<Attribute> getPrimaryKey() {
		return this.primaryKey;
	}

	protected void setPrimaryKey(ArrayList<Attribute> primaryKey) {
		this.primaryKey = primaryKey;
	}

	public ArrayList<Tuple> iterateAndReturnTuples(Transaction transaction) throws TransactionAbortedException{
		ArrayList<Tuple> tuples = new ArrayList<Tuple>();
		RelationIterator<Tuple> iterator = this.iterator(transaction);
		while (iterator.hasNext()){
			tuples.add(iterator.next());
		}
		return tuples;
	}

}
