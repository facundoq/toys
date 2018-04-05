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

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.crappydbms.datadictionary.DataDictionary;
import org.crappydbms.datadictionary.InvalidStoredRelationNameException;
import org.crappydbms.exceptions.CrappyDBMSException;
import org.crappydbms.relations.StoredRelation;
import org.crappydbms.relations.schemas.RelationSchema;
import org.crappydbms.relations.schemas.StoredRelationSchema;
import org.crappydbms.relations.schemas.attributes.Attribute;
import org.crappydbms.transactions.Transaction;

/**
 * @author Facundo Manuel Quiroga Oct 31, 2008
 */
public class Database {
	
	protected static int maximumConcurrentTransactions = 100;
	
	protected PageManager pageManager;
	protected DataDictionary dataDictionary;
	protected TransactionManager transactionManager;
	protected StalledTransactionChecker stalledTransactionChecker;
	
	public Database(DataDictionary dataDictionary) {
		this.setDataDictionary(dataDictionary);
		dataDictionary.setDatabase(this);
		this.setPageManager(new PageManager(this));
		this.setTransactionManager(new TransactionManager(this,maximumConcurrentTransactions));
		this.setStalledTransactionChecker(new StalledTransactionChecker(this));
		this.getStalledTransactionChecker().start();
		this.getStalledTransactionChecker().startChecking();
	}

	public DataDictionary getDataDictionary() {
		return this.dataDictionary;
	}

	public void setDataDictionary(DataDictionary dataDictionary) {
		this.dataDictionary = dataDictionary;
	}

	/**
	 * @param relationSchema
	 * @param name
	 * @throws CrappyDBMSException
	 * @throws IOException
	 */
	public StoredRelation addStoredRelation(String name, StoredRelationSchema relationSchema, List<Attribute> primaryKey) throws  CrappyDBMSException {
		return this.getDataDictionary().addStoredRelation(name, relationSchema, primaryKey);
	}

	public StoredRelation addStoredRelation(String name, RelationSchema relationSchema, List<Attribute> primaryKey) throws  CrappyDBMSException {
		return this.addStoredRelation(name, relationSchema.convertToStoredRelationSchema(), primaryKey);
	}

	/**
	 * @param string
	 * @return
	 */
	public StoredRelation getStoredRelationNamed(String name) throws InvalidStoredRelationNameException {
		return this.getDataDictionary().getStoredRelation(name);
	}

	/**
	 * @param relation
	 * @throws IOException
	 */
	public void removeStoredRelation(StoredRelation storedRelation) throws IOException {
		this.getDataDictionary().removeStoredRelation(storedRelation);
	}

	/**
	 * @returns a new transaction
	 */
	public Transaction newTransaction() {
		return this.getTransactionManager().newTransaction();
	}
	
	/**
	 * This method SHOULD ONLY BE CALLED if no transactions are active
	 */
	public void removeSelf() throws IOException {
		//TODO Check if transactions are active. If so abort them.
		while (this.getDataDictionary().getStoredRelations().size() > 0) {
			Iterator<StoredRelation> iterator = this.getDataDictionary().getStoredRelations().values().iterator();
			StoredRelation storedRelation = iterator.next();
			this.removeStoredRelation(storedRelation);
		}
	}

	public PageManager getPageManager() {
		return this.pageManager;
	}

	protected void setPageManager(PageManager pageManager) {
		this.pageManager = pageManager;
	}
	public void shutDown() throws CrappyDBMSException{
		//todo abort transactions or wait till they're finished
		this.getDataDictionary().shutDown();
		try {
			this.getPageManager().shutDown();
			this.getStalledTransactionChecker().stopChecking();
			this.getStalledTransactionChecker().finishChecking();
			while (!this.getStalledTransactionChecker().finished()){
				Thread.sleep(200);
			}
		} catch (IOException e) {
			throw new CrappyDBMSException("Error shutting down, "+e.getMessage());
		} catch (InterruptedException e) {
			throw new CrappyDBMSException("Interrupted while waiting StalledTransactionChecker to finish at shutdown"+e.getMessage());
		}
	}

	public TransactionManager getTransactionManager() {
		return this.transactionManager;
	}

	protected void setTransactionManager(TransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	protected StalledTransactionChecker getStalledTransactionChecker() {
		return this.stalledTransactionChecker;
	}

	protected void setStalledTransactionChecker(StalledTransactionChecker stalledTransactionChecker) {
		this.stalledTransactionChecker = stalledTransactionChecker;
	}
}
