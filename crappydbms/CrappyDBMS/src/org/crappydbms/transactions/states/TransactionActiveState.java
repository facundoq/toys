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
package org.crappydbms.transactions.states;

import java.io.IOException;
import java.util.ArrayList;

import org.crappydbms.dbfiles.DBFile;
import org.crappydbms.dbfiles.FilePage;
import org.crappydbms.dbfiles.FilePageID;
import org.crappydbms.dbfiles.heapfile.HeapFile;
import org.crappydbms.exceptions.CrappyDBMSError;
import org.crappydbms.main.PageBufferFullException;
import org.crappydbms.relations.tuples.StoredTuple;
import org.crappydbms.transactions.BaseTransaction;
import org.crappydbms.transactions.LockMode;
import org.crappydbms.transactions.TransactionAbortedException;
import org.crappydbms.transactions.TransactionState;

/**
 * @author Facundo Manuel Quiroga
 * Jan 14, 2009
 * 
 */
public class TransactionActiveState extends TransactionState {

	protected String immediateState;
	public TransactionActiveState(BaseTransaction baseTransaction) {
		super(baseTransaction);
		immediateState = "Creating..";
	}

	@Override
	public void abort(String message) throws TransactionAbortedException {
		immediateState = "Aborting ("+message+")..";
		TransactionAbortingState transactionAbortingState = new TransactionAbortingState(this.getBaseTransaction());
		this.setTransactionState(transactionAbortingState);
		transactionAbortingState.performAbort(message);
	}
	
	@Override
	public void commit() throws TransactionAbortedException {
		immediateState = "Commiting..";
		BaseTransaction baseTransaction = this.getBaseTransaction();
		TransactionCommittingState transactionCommittingState = new TransactionCommittingState(baseTransaction);
		this.setTransactionState(transactionCommittingState);
		transactionCommittingState.performCommit();
	}
    public boolean isFinished(){
    	return false;
    }

	@Override
	public FilePage getPage(FilePageID filePageID, LockMode lockMode) throws TransactionAbortedException {
		immediateState = "Getting page "+ filePageID+" - "+lockMode+" - locking - ..";
		this.getTransactionLockingManager().lockPage(filePageID, lockMode);
		immediateState = "Getting page "+ filePageID+" - "+lockMode+" - locked - ..";
		try {
			immediateState = "Getting page "+ filePageID+" - "+lockMode+" - getting - ..";
			FilePage filePage = filePageID.getFile().getPage(filePageID.getPageNumber());
			immediateState = "Getting page "+ filePageID+" - "+lockMode+" - got- ..";
			return filePage;
		} catch (IOException e) {
			String message = "Error getting page " + filePageID;
			this.abort(message);
		} catch (PageBufferFullException e) {
			this.abort("Not enough memory: " + e.getMessage());
		}
		throw new CrappyDBMSError("A Transaction aborted, TransactionAbortedException should have been thrown");
	}
	
	// Transaction Page Manager
	@Override
	public long getNumberOfPagesOfDBFile(DBFile DBFile) {
		return this.getBaseTransaction().getTransactionPagesManager().getNumberOfPagesOfDBFile(DBFile);
	}

	@Override
	public ArrayList<FilePage> getPages(DBFile DBFile) {
		immediateState = "Getting pages of "+DBFile+"..";
		ArrayList<FilePage> pages =  this.getBaseTransaction().getTransactionPagesManager().getPages(DBFile);
		immediateState = "Got pages of "+DBFile+"..";
		return pages;
	}	

	// TRANSACTION LOCKING MANAGER
	@Override
	public boolean doTryLockExclusive(FilePageID filePageID) {
		immediateState = "Trying to lock exclusively "+filePageID+"..";
		return this.getTransactionLockingManager().doTryLockExclusive(filePageID);
	}

	@Override
	public boolean doTryUpgradeLock(FilePageID filePageID) {
		immediateState = "Trying to lock upgrade "+filePageID+"..";
		return this.getTransactionLockingManager().doTryUpgradeLock(filePageID);
	}

	@Override
	public void downgradeToShared(FilePageID filePageID) {
		immediateState = "Downgrading "+filePageID+"..";
		this.getTransactionLockingManager().downgradeToShared(filePageID);
	}

	@Override
	public void lockExclusive(FilePageID filePageID) throws TransactionAbortedException {
		immediateState = "Locking exclusively "+filePageID+"..";
		this.getTransactionLockingManager().lockExclusive(filePageID);
	}

	@Override
	public void lockShared(FilePageID filePageID) throws TransactionAbortedException {
		immediateState = "Locking shared "+filePageID+"..";
		this.getTransactionLockingManager().lockShared(filePageID);
	}

	@Override
	public void releaseExclusiveLock(FilePageID filePageID) {
		immediateState = "Releasing exclusive lock "+filePageID+"..";
		this.getTransactionLockingManager().releaseExclusiveLock(filePageID);
	}

	@Override
	public void releaseSharedLock(FilePageID filePageID) {
		immediateState = "Releasing shared lock "+filePageID+"..";
		this.getTransactionLockingManager().releaseSharedLock(filePageID);
	}

	@Override
	public String toString() {
		return "Active ("+this.immediateState+")";
	}

	@Override
	public void addTuple(StoredTuple tuple, HeapFile heapFile) throws TransactionAbortedException {
		try {
		this.getTransactionPageAddingManager().addTuple(tuple,heapFile);
	} catch (PageBufferFullException e) {
		this.getBaseTransaction().abort(e.getMessage());
	} catch (IOException e) {
		this.getBaseTransaction().abort(e.getMessage());
	}
	}

}
