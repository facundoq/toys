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
import java.util.HashMap;
import java.util.Iterator;

import org.crappydbms.dbfiles.DBFile;
import org.crappydbms.dbfiles.FilePage;
import org.crappydbms.dbfiles.FilePageID;
import org.crappydbms.dbfiles.locking.PageLock;
import org.crappydbms.main.PageBufferFullException;
import org.crappydbms.transactions.BaseTransaction;
import org.crappydbms.transactions.TransactionAbortedException;

/**
 * @author Facundo Manuel Quiroga
 * Jan 14, 2009
 * 
 */
public class TransactionCommittingState extends TransactionNotActiveState {

	protected String immediateStatus;
	/**
	 * @param baseTransaction
	 */
	public TransactionCommittingState(BaseTransaction baseTransaction) {
		super(baseTransaction);
		this.immediateStatus = "Creating";
	}
	
	public void performCommit() throws TransactionAbortedException{
		this.immediateStatus = "Releasing Shared pages..";
		this.getTransactionLockingManager().releaseSharedLocks();
		this.immediateStatus = "Shared Pages released";
		try {
			//System.out.println(this.getBaseTransaction()+" flushing /*  */ /* */ ");
			this.immediateStatus = "Flushing pages..";
			this.flushPages();
			this.immediateStatus = "Pages flushed";
		} catch (IOException e) {
			System.out.println(" /*  */ /* */  "+this.getBaseTransaction()+" aborted when commiting. /*  */ /* */ ");
			this.abort("Error writing pages on commit , " + e.getMessage());
		}
		//System.out.println(this.getBaseTransaction()+" releasing/*  */ /* */ ");
		this.immediateStatus = "Releasing Exclusive pages..";
		this.getTransactionLockingManager().releaseExclusiveLocks();
		this.immediateStatus = "Exclusive Pages released";
		this.immediateStatus = "Transitioning to commited state";
		//System.out.println(this.getBaseTransaction()+" changing state /*  */ /* */ ");
		this.setTransactionState(new TransactionCommitedState(this.getBaseTransaction()));
		this.getBaseTransaction().getDatabase().getTransactionManager().committed(this.getBaseTransaction());
	}
	
	
	public void abort(String message) throws TransactionAbortedException{
		TransactionAbortingState transactionAbortingState = new TransactionAbortingState(this.getBaseTransaction());
		this.setTransactionState(transactionAbortingState);
		transactionAbortingState.performAbort(message);
	}
	
	protected void releasePages()  {
		this.getTransactionLockingManager().releaseSharedLocks();
		this.getTransactionLockingManager().releaseExclusiveLocks();
	}
	
	protected void flushPages() throws IOException, TransactionAbortedException {
	  //System.out.println(this.getBaseTransaction()+" flushing locked /*  */ /* */ ");
		// flush locked pages
		HashMap<FilePageID, PageLock> exclusivelyLockedPages = this.getTransactionLockingManager().getExclusiveLocks();
		Iterator<FilePageID> exclusiveLocksIterator = exclusivelyLockedPages.keySet().iterator();
		while (exclusiveLocksIterator.hasNext()) {
			FilePageID filePageID = exclusiveLocksIterator.next();
			DBFile file = filePageID.getFile();
			try {
				file.flush(filePageID);
			} catch (PageBufferFullException e) {
				this.abort("Error flushing pages, not enough memory: " + e.getMessage());
			}
		}
		//System.out.println(this.getBaseTransaction()+" creating new /*  */ /* */ ");
		// create newly added pages
		HashMap<DBFile, ArrayList<FilePage>> pagesAddedPerDBFile = this.getTransactionPageAddingManager().getPagesAddedPerDBFile();
		for (DBFile DBFile : pagesAddedPerDBFile.keySet()) {
			ArrayList<FilePage> filePages = pagesAddedPerDBFile.get(DBFile);
			if (!filePages.isEmpty()){
				DBFile.addNewPages(filePages);
			}
		}
	}
	

	@Override
	public String toString() {
		return "Commiting ("+this.immediateStatus+")";
	}

}
