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
import java.util.HashMap;

import org.crappydbms.dbfiles.DBFile;
import org.crappydbms.dbfiles.FilePage;
import org.crappydbms.dbfiles.FilePageID;
import org.crappydbms.dbfiles.heapfile.HeapFile;
import org.crappydbms.dbfiles.locking.PageLock;
import org.crappydbms.relations.tuples.StoredTuple;

/**
 * @author Facundo Manuel Quiroga
 * Nov 3, 2008
 * 
 */
public interface Transaction {

	public long getId();
	
	void abort(String message) throws TransactionAbortedException;
	void abort() throws TransactionAbortedException;
	void commit() throws TransactionAbortedException;

	/**
	 * @param currentDBHeapPage
	 * @throws TransactionAbortedException 
	 * @throws InterruptedException 
	 */
	void lockShared(FilePageID filePageID) throws TransactionAbortedException;

	/**
	 * @param currentDBHeapPage
	 * @throws TransactionAbortedException 
	 */
	void lockExclusive(FilePageID filePageID) throws TransactionAbortedException;

	/**
	 * @param filePageID
	 * @param lockMode 
	 * @return
	 * @throws TransactionAbortedException 
	 */
	FilePage getPage(FilePageID filePageID, LockMode lockMode) throws TransactionAbortedException;
	
	/**
	 * Releases an exclusive lock on the page. Assumes the caller has check that this transaction has an exclusive lock on the page. 
	 * @param filePageID of the page
	 */
	void releaseExclusiveLock(FilePageID filePageID);
	
	/**
	 * Releases a shared lock on the page. Assumes the caller has check that this transaction has a shared lock on the page. 
	 * @param filePageID of the page
	 */
	void releaseSharedLock(FilePageID filePageID);
	
	/**
	 * Checks if this transaction has an exclusive lock on the lock for the FilePage with filePageId 
	 * @param filePageID of the page
	 * @return true if the transaction has an exclusive lock on the page
	 */
	boolean hasExclusiveLockOn(FilePageID filePageID);
	
	/**
	 * Checks if this transaction has a shared lock on the lock for the FilePage with filePageId 
	 * @param filePageID of the page
	 * @return true if the transaction has a shared lock on the page
	 */
	boolean hasSharedLockOn(FilePageID filePageID);
	
	String getStatus();
	
	String getLockingStatus();

	public long getNumberOfPagesOfDBFile(DBFile DBFile);

	/**
	 * Add tuple to heapFile, but to the private cache of added pages of the transaction.
	 * No other transaction should see this tuples until they are committed and written to disk
	 * @param tuple
	 * @param heapFile 
	 * @throws TransactionAbortedException 
	 */
	public void addTuple(StoredTuple tuple, HeapFile heapFile) throws TransactionAbortedException;
	/**
	 *  Get the FilePages of a DBFile that are local to this transaction
	 * @param DBFile
	 * @return
	 */
	public ArrayList<FilePage> getPages(DBFile DBFile);

	/**
	 * Upgrade a shared lock to a exclusive lock, iff it can be done without waiting (locking until all readers except the calling transaction are gone ).
	 * Assumes the caller knows this transaction has a shared lock
	 * @param filePageID of the page of which the lock will be acquire
	 * @return true if the lock was upgraded
	 */
	public boolean doTryUpgradeLock(FilePageID filePageID);
	
	/**
	 * Obtain an exclusive lock, iff it can be done without waiting (locking until all readers are gone ).
	 * Assumes the caller knows this transaction does not have a shared lock
	 * @param filePageID of the page of which the lock will be acquire
	 * @return true if the lock was acquired
	 */
	public boolean doTryLockExclusive(FilePageID filePageID);

	/**
	 * Downgrades a lock from exclusive to shared. Does so without waiting.
	 * Assumes the caller knows this transaction has an exclusive lock
	 */
	public void downgradeToShared(FilePageID filePageID);

	public HashMap<FilePageID, PageLock> getExclusiveLocks();
	public HashMap<FilePageID, PageLock> getSharedLocks() ;
	
	public LastAction getLastAction();
	
	/**
	 * Aborts the transaction from another thread. Does not mean it
	 */
	public void abortFromAnotherThread();
	/**
	 * Kills the transaction, effectively removing the control of the current thread from it.
	 * can't implement this :(
	 */
	//public void kill();

}
