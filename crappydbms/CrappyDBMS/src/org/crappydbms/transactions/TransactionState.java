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
package org.crappydbms.transactions;

import java.util.ArrayList;

import org.crappydbms.dbfiles.DBFile;
import org.crappydbms.dbfiles.FilePage;
import org.crappydbms.dbfiles.FilePageID;
import org.crappydbms.dbfiles.heapfile.HeapFile;
import org.crappydbms.relations.tuples.StoredTuple;

/**
 * @author Facundo Manuel Quiroga
 * Jan 14, 2009
 * 
 */
public abstract class TransactionState {
	
	
	protected BaseTransaction baseTransaction;
	
	public TransactionState(BaseTransaction baseTransaction){
		this.setBaseTransaction(baseTransaction);
	}
	public abstract String toString();
	protected void setTransactionState(TransactionState transactionState){
		this.getBaseTransaction().setTransactionState(transactionState);
	}
	protected TransactionLockingManager getTransactionLockingManager() {
		return this.getBaseTransaction().getTransactionLockingManager();
	}
	protected TransactionPagesManager getTransactionPageAddingManager() {
		return this.getBaseTransaction().getTransactionPagesManager();
	}
	
	public abstract void abort(String message) throws TransactionAbortedException;
	public abstract void commit() throws TransactionAbortedException;
	public abstract boolean isFinished();

	public abstract FilePage getPage(FilePageID filePageID,LockMode lockMode) throws TransactionAbortedException ;
	
	public abstract void lockShared(FilePageID filePageID) throws TransactionAbortedException;
	public abstract void lockExclusive(FilePageID filePageID) throws TransactionAbortedException ;
	public abstract boolean doTryUpgradeLock(FilePageID filePageID);
	public abstract boolean doTryLockExclusive(FilePageID filePageID);
	
	public abstract void releaseExclusiveLock(FilePageID filePageID);
	public abstract void releaseSharedLock(FilePageID filePageID);
	public  abstract void downgradeToShared(FilePageID filePageID);
	
	public abstract long getNumberOfPagesOfDBFile(DBFile DBFile);
	public abstract ArrayList<FilePage> getPages(DBFile DBFile);

	protected BaseTransaction getBaseTransaction() {
		return this.baseTransaction;
	}

	protected void setBaseTransaction(BaseTransaction baseTransaction) {
		this.baseTransaction = baseTransaction;
	}
	/**
	 * @param tuple
	 * @param file 
	 * @throws TransactionAbortedException 
	 */
	public abstract void addTuple(StoredTuple tuple, HeapFile file) throws TransactionAbortedException;

}
