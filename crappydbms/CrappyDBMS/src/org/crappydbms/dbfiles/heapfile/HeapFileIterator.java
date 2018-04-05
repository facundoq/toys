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

import java.util.ArrayList;
import java.util.Iterator;

import org.crappydbms.dbfiles.FilePage;
import org.crappydbms.dbfiles.FilePageIterator;
import org.crappydbms.exceptions.InvalidAttributeNameException;
import org.crappydbms.queries.operations.Modification;
import org.crappydbms.relations.StoredRelationIterator;
import org.crappydbms.relations.tuples.InvalidAttributeTypeException;
import org.crappydbms.relations.tuples.StoredTuple;
import org.crappydbms.relations.tuples.Tuple;
import org.crappydbms.transactions.LockMode;
import org.crappydbms.transactions.Transaction;
import org.crappydbms.transactions.TransactionAbortedException;

/**
 * @author Facundo Manuel Quiroga Nov 9, 2008
 */
public class HeapFileIterator implements StoredRelationIterator<Tuple> {

	protected HeapFile heapFile;

	protected FilePageIterator<FilePage> heapFileIterator;

	protected Iterator<StoredTuple> heapPageIterator;

	protected FilePage currentDBHeapPage;

	protected FilePage DBHeapPageOfLastTupleReturned;

	protected boolean fileEnded;

	protected Transaction transaction;

	protected StoredTuple lastTuple;

	/**
	 * @param heapFile
	 * @param transaction
	 * @throws TransactionAbortedException
	 */
	public HeapFileIterator(HeapFile heapFile, Transaction transaction, LockMode lockMode) throws TransactionAbortedException {

		this.setHeapFile(heapFile);
		FilePageIterator<FilePage> heapFilePagesIterator = heapFile.getHeapFilePagesIterator(transaction, lockMode);
		this.setHeapFilePagesIterator(heapFilePagesIterator);
		this.searchNonEmptyPage();
		this.setTransaction(transaction);
		this.setDBHeapPageOfLastTupleReturned(null);
	}

	/**
	 * there can be empty pages so that a page exists does not mean that it has tuples
	 * 
	 * @throws TransactionAbortedException
	 */
	protected void searchNonEmptyPage() throws TransactionAbortedException {
		FilePageIterator<FilePage> heapFilePagesIterator = this.getHeapFilePagesIterator();
		boolean foundNotEmptyPage = false;
		FilePage selectedPage = null;
		//System.out.println(" - HeapFileIterator: searchNonEmptyPage - ");
		//System.out.println("buscando..");
		while (!foundNotEmptyPage && heapFilePagesIterator.hasNext()) {
			//System.out.println("loop");
			selectedPage = heapFilePagesIterator.next();
			// System.out.println(selectedPage.getNumberOfTuples());
			foundNotEmptyPage = !selectedPage.isEmpty();
			//System.out.println("loop");
		}
		if (!foundNotEmptyPage) {
			this.setFileEnded(true);
			//System.out.println("no encontró");
		} else {
			// System.out.println("encontró!");
			this.setHeapFilePagesIterator(heapFilePagesIterator);
			this.setCurrentDBHeapPage(selectedPage);
			this.setHeapPageIterator(selectedPage.iterator());
		}
	}

	/**
	 * @throws TransactionAbortedException
	 */
	@Override
	public boolean hasNext() throws TransactionAbortedException {
		// System.out.println("HeapFileIterator - hasNext ");
		if (this.fileEnded()) {
			return false;
		} else {
			if (this.getHeapPageIterator().hasNext()) {
				return true;
			} else {
				this.searchNonEmptyPage();
				return !this.fileEnded();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#next()
	 */
	@Override
	public StoredTuple next() throws TransactionAbortedException {
		if (!this.getHeapPageIterator().hasNext()) {
			this.searchNonEmptyPage();
		}
		StoredTuple lastTuple = this.getHeapPageIterator().next();
		this.setDBHeapPageOfLastTupleReturned(this.getCurrentDBHeapPage());
		this.setLastTuple(lastTuple);
		return lastTuple;
	}

	@Override
	public void modifyTuple(ArrayList<Modification> modifications) throws TransactionAbortedException {
		//Lock the page in exclusive mode (will surely as an upgrade, or the page was already locked in that mode)
		this.getTransaction().lockExclusive(this.getCurrentDBHeapPage().getFilePageID());
		for (Modification modification : modifications) {
			try {
				modification.applyTo(this.getLastTuple());
			} catch (InvalidAttributeNameException e) {
				throw new TransactionAbortedException("Invalid attribute name modifying tuple" + this.getLastTuple() + "with modification" + modification);
			} catch (InvalidAttributeTypeException e) {
				throw new TransactionAbortedException("Invalid attribute types modifying tuple" + this.getLastTuple() + "with modification" + modification);
			}
		}
		this.markAsDirty();
	}

	@Override
	public void remove() throws TransactionAbortedException {
		//Lock the page in exclusive mode (will surely as an upgrade, or the page was already locked in that mode)

		this.getTransaction().lockExclusive(this.getDBHeapPageOfLastTupleReturned().getFilePageID());
		// remove the tuple from the current HeapPage
		this.getHeapPageIterator().remove();
		// mark the page as dirty
		this.markAsDirty();
	}

	public void markAsDirty() {
		this.getDBHeapPageOfLastTupleReturned().markAsDirty();
	}

	protected HeapFile getHeapFile() {
		return this.heapFile;
	}

	protected void setHeapFile(HeapFile heapFile) {
		this.heapFile = heapFile;
	}

	protected FilePageIterator<FilePage> getHeapFilePagesIterator() {
		return this.heapFileIterator;
	}

	protected void setHeapFilePagesIterator(FilePageIterator<FilePage> heapFileIterator) {
		this.heapFileIterator = heapFileIterator;
	}

	protected Iterator<StoredTuple> getHeapPageIterator() {
		return this.heapPageIterator;
	}

	protected void setHeapPageIterator(Iterator<StoredTuple> heapPageIterator) {
		this.heapPageIterator = heapPageIterator;
	}

	protected FilePage getCurrentDBHeapPage() {
		return this.currentDBHeapPage;
	}

	protected void setCurrentDBHeapPage(FilePage currentDBHeapPage) {
		this.currentDBHeapPage = currentDBHeapPage;
	}

	protected boolean fileEnded() {
		return this.fileEnded;
	}

	protected void setFileEnded(boolean empty) {
		this.fileEnded = empty;
	}

	protected Transaction getTransaction() {
		return this.transaction;
	}

	protected void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	protected StoredTuple getLastTuple() {
		return this.lastTuple;
	}

	protected void setLastTuple(StoredTuple lastTuple) {
		this.lastTuple = lastTuple;
	}

	protected FilePage getDBHeapPageOfLastTupleReturned() {
		return this.DBHeapPageOfLastTupleReturned;
	}

	protected void setDBHeapPageOfLastTupleReturned(FilePage heapPageOfLastTupleReturned) {
		this.DBHeapPageOfLastTupleReturned = heapPageOfLastTupleReturned;
	}

}
