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

package org.crappydbms.dbfiles.heapfile;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.crappydbms.dbfiles.FilePage;
import org.crappydbms.dbfiles.FilePageID;
import org.crappydbms.dbfiles.FilePageIterator;
import org.crappydbms.transactions.LockMode;
import org.crappydbms.transactions.Transaction;
import org.crappydbms.transactions.TransactionAbortedException;

/**
 * @author Facundo Manuel Quiroga Nov 9, 2008
 * 
 */

public class HeapFilePageIterator implements FilePageIterator<FilePage> {

	protected long nextPageNumber;

	protected HeapFile heapFile;

	protected Transaction transaction;

	protected LockMode lockMode;

	protected Iterator<FilePage> newlyAddedPagesIterator;

	public HeapFilePageIterator(HeapFile heapFile, Transaction transaction, LockMode lockMode) {
		this.setNextPageNumber(0);
		this.setHeapFile(heapFile);
		this.setTransaction(transaction);
		this.setLockMode(lockMode);
		this.setNewlyAddedPagesIterator(transaction.getPages(heapFile).iterator());
	}

	@Override
	public boolean hasNext() {
		return this.fileHasNext() || this.getNewlyAddedPagesIterator().hasNext() ; 
	}
	
	protected boolean fileHasNext(){
		// ask transaction for the number of pages to avoid phantom reads
		return this.getNextPageNumber() < (this.getTransaction().getNumberOfPagesOfDBFile(this.getHeapFile()));
	}
	
	@Override
	public FilePage next() throws TransactionAbortedException {
		
		if (!this.hasNext()) {
			throw new NoSuchElementException("No more pages available");
		}
		FilePage filePage;
		if (this.fileHasNext()){
			long nextPageNumber = this.getNextPageNumber();
			FilePageID filePageID = new HeapFilePageID(this.getHeapFile(), nextPageNumber);
			filePage = this.getTransaction().getPage(filePageID, this.getLockMode());
			this.setNextPageNumber(nextPageNumber + 1);	
		}else{
			filePage = this.getNewlyAddedPagesIterator().next();
		}
		
		return filePage;

	}

	protected long getNextPageNumber() {
		return this.nextPageNumber;
	}

	protected void setNextPageNumber(long currentPage) {
		this.nextPageNumber = currentPage;
	}

	protected HeapFile getHeapFile() {
		return this.heapFile;
	}

	protected void setHeapFile(HeapFile heapFile) {
		this.heapFile = heapFile;
	}

	protected Transaction getTransaction() {
		return this.transaction;
	}

	protected void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	protected LockMode getLockMode() {
		return this.lockMode;
	}

	protected void setLockMode(LockMode lockMode) {
		this.lockMode = lockMode;
	}

	protected Iterator<FilePage> getNewlyAddedPagesIterator() {
		return this.newlyAddedPagesIterator;
	}

	protected void setNewlyAddedPagesIterator(Iterator<FilePage> newlyAddedPagesIterator) {
		this.newlyAddedPagesIterator = newlyAddedPagesIterator;
	}

}
