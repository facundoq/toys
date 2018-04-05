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
import org.crappydbms.main.Database;
import org.crappydbms.relations.tuples.StoredTuple;
import org.crappydbms.transactions.states.TransactionActiveState;

/**
 * @author Facundo Manuel Quiroga Dec 21, 2008
 */
public class BaseTransaction implements Transaction {

	protected String status;

	protected long id;

	protected Database database;

	protected TransactionLockingManager transactionLockingManager;
	protected TransactionPagesManager transactionPagesManager;

	protected TransactionState transactionState;
	protected Thread transactionThread;
	protected volatile LastAction lastAction;

	public BaseTransaction(long id, Database database, Thread transactionThread) {
		super();
		this.setId(id);
		this.setDatabase(database);
		this.setTransactionLockingManager(new TransactionLockingManager(this));
		this.setTransactionPagesManager(new TransactionPagesManager(this));
		this.setTransactionState(new TransactionActiveState(this));
		this.setTransactionThread(transactionThread);
		this.setLastAction(new LastAction("created transaction"));
	}

	public long getId() {
		return this.id;
	}

	protected void setId(long id) {
		this.id = id;
	}

	protected TransactionState getTransactionState() {
		return this.transactionState;
	}

	protected void setTransactionState(TransactionState transactionState) {
		this.transactionState = transactionState;
	}
	
	// Transaction State management and transition
	public String getStatus() {
		return this.getTransactionState().toString();
	}

	public boolean isFinished() {
		return this.getTransactionState().isFinished();
	}

	@Override
	public void abort() throws TransactionAbortedException {
		this.doAbort("user aborted");
	}

	@Override
	public void abort(String reason) throws TransactionAbortedException {
		this.setLastAction(new LastAction("aborting"));
		this.getTransactionState().abort(reason);
		this.setLastAction(new LastAction("aborted"));
	}

	protected void doAbort(String reason) throws TransactionAbortedException {
		this.setLastAction(new LastAction("aborting"));
		this.getTransactionState().abort(reason);
		this.setLastAction(new LastAction("aborted"));
	}

	@Override
	public void commit() throws TransactionAbortedException {
		this.setLastAction(new LastAction("committing"));
		this.getTransactionState().commit();
		this.setLastAction(new LastAction("committed"));
	}
	
	public String toString() {
		return "Transaction " + id;
	}
	
	// OPERATIONS : 
	
	// PAGE ACQUIRING
	@Override
	public FilePage getPage(FilePageID filePageID, LockMode lockMode) throws TransactionAbortedException {
		this.setLastAction(new LastAction("getting page"));
		FilePage filePage = this.getTransactionState().getPage(filePageID, lockMode);
		this.setLastAction(new LastAction("got page"));
		return filePage;
	}



	// LOCK-MANAGEMENT
	public TransactionLockingManager getTransactionLockingManager() {
		return this.transactionLockingManager;
	}

	protected void setTransactionLockingManager(TransactionLockingManager transactionLockingManager) {
		this.transactionLockingManager = transactionLockingManager;
	}

	@Override
	public boolean doTryLockExclusive(FilePageID filePageID) {
		this.setLastAction(new LastAction("start doTryLockExclusive"));
		boolean lockSuccessful = this.getTransactionState().doTryLockExclusive(filePageID);
		this.setLastAction(new LastAction(" end doTryLockExclusive"));
		return lockSuccessful;
	}

	@Override
	public boolean doTryUpgradeLock(FilePageID filePageID) {
		this.setLastAction(new LastAction("start doTryUpgradeLock"));
		boolean lockSuccessful = this.getTransactionState().doTryUpgradeLock(filePageID);
		this.setLastAction(new LastAction("end doTryUpgradeLock"));
		return lockSuccessful;
	}

	@Override
	public void downgradeToShared(FilePageID filePageID) {
		this.setLastAction(new LastAction("start downgradeToShared"));
		this.getTransactionState().downgradeToShared(filePageID);
		this.setLastAction(new LastAction("end downgradeToShared"));
	}

	@Override
	public String getLockingStatus() {
		return this.getTransactionLockingManager().getLockingStatus();
	}

	@Override
	public boolean hasExclusiveLockOn(FilePageID filePageID) {
		this.setLastAction(new LastAction("start  hasExclusiveLockOn"));
		boolean hasExclusiveLock = this.getTransactionLockingManager().hasExclusiveLockOn(filePageID);
		this.setLastAction(new LastAction("end  hasExclusiveLockOn"));
		return hasExclusiveLock;
	}

	@Override
	public boolean hasSharedLockOn(FilePageID filePageID) {
		this.setLastAction(new LastAction("start  hasSharedLockOn"));
		boolean hasSharedLock = this.getTransactionLockingManager().hasSharedLockOn(filePageID);
		this.setLastAction(new LastAction("start  hasSharedLockOn"));
		return hasSharedLock;
	}

	@Override
	public void lockExclusive(FilePageID filePageID) throws TransactionAbortedException {
		this.setLastAction(new LastAction("start  lockExclusive"));
		this.getTransactionState().lockExclusive(filePageID);
		this.setLastAction(new LastAction("end  lockExclusive"));
	}

	@Override
	public void lockShared(FilePageID filePageID) throws TransactionAbortedException {
		this.setLastAction(new LastAction("start  lockShared"));
		this.getTransactionState().lockShared(filePageID);
		this.setLastAction(new LastAction("end  lockShared"));
	}

	@Override
	public void releaseExclusiveLock(FilePageID filePageID) {
		this.setLastAction(new LastAction("start  releaseExclusiveLock"));
		this.getTransactionState().releaseExclusiveLock(filePageID);
		this.setLastAction(new LastAction("end  releaseExclusiveLock"));
	}

	@Override
	public void releaseSharedLock(FilePageID filePageID) {
		this.setLastAction(new LastAction("start  releaseSharedLock"));
		this.getTransactionState().releaseSharedLock(filePageID);
		this.setLastAction(new LastAction("end  releaseSharedLock"));
	}

	public HashMap<FilePageID, PageLock> getExclusiveLocks() {
		return this.getTransactionLockingManager().getExclusiveLocks();
	}

	public HashMap<FilePageID, PageLock> getSharedLocks() {
		return this.getTransactionLockingManager().getSharedLocks();
	}
	
	/// NEW PAGES AND TUPLES
	public TransactionPagesManager getTransactionPagesManager() {
		return this.transactionPagesManager;
	}

	protected void setTransactionPagesManager(TransactionPagesManager transactionPagesManager) {
		this.transactionPagesManager = transactionPagesManager;
	}

	@Override
	public long getNumberOfPagesOfDBFile(DBFile DBFile) {
		this.setLastAction(new LastAction("start  getNumberOfPagesOfDBFile"));
		long numberOfPages = this.getTransactionPagesManager().getNumberOfPagesOfDBFile(DBFile);
		this.setLastAction(new LastAction("end  getNumberOfPagesOfDBFile"));
		return numberOfPages;
	}

	@Override
	public ArrayList<FilePage> getPages(DBFile DBFile) {
		this.setLastAction(new LastAction("start  getPages"));
		ArrayList<FilePage> pages = this.getTransactionState().getPages(DBFile);
		this.setLastAction(new LastAction("end  getPages"));
		return pages;
	}
	@Override
	public void addTuple(StoredTuple tuple, HeapFile DBFile) throws TransactionAbortedException {
		this.setLastAction(new LastAction("start  addTuple"));
		this.getTransactionState().addTuple(tuple, DBFile);
		this.setLastAction(new LastAction("end  addTuple"));
	}
	




	protected Thread getTransactionThread() {
		return this.transactionThread;
	}

	protected void setTransactionThread(Thread transactionThread) {
		this.transactionThread = transactionThread;
	}

	public LastAction getLastAction() {
		return this.lastAction;
	}

	protected void setLastAction(LastAction lastAction) {
		this.lastAction = lastAction;
	}
	
	public Database getDatabase() {
		return this.database;
	}

	protected void setDatabase(Database database) {
		this.database = database;
	}

	@Override
	public void abortFromAnotherThread() {
		this.getTransactionThread().interrupt();
	}
	
}
