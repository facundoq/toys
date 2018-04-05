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

import java.util.ArrayList;

import org.crappydbms.dbfiles.DBFile;
import org.crappydbms.dbfiles.FilePage;
import org.crappydbms.dbfiles.FilePageID;
import org.crappydbms.dbfiles.heapfile.HeapFile;
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
public abstract class TransactionNotActiveState extends TransactionState {

	public TransactionNotActiveState(BaseTransaction baseTransaction) {
		super(baseTransaction);
	}

	@Override
	public void abort(String message) throws TransactionAbortedException {
		throw new TransactionInvalidStateException("Action cannot be performed in state"+ this.toString());
	}

	@Override
	public boolean isFinished() {
		throw new TransactionInvalidStateException("Action cannot be performed in state"+ this.toString());
	}

	@Override
	public void commit() throws TransactionAbortedException {
		throw new TransactionInvalidStateException("Action cannot be performed in state"+ this.toString());

	}


	@Override
	public long getNumberOfPagesOfDBFile(DBFile DBFile) {
		throw new TransactionInvalidStateException("Action cannot be performed in state"+ this.toString());
	}

	@Override
	public FilePage getPage(FilePageID filePageID, LockMode lockMode) throws TransactionAbortedException {
		throw new TransactionInvalidStateException("Action cannot be performed in state"+ this.toString());
	}
	

	@Override
	public ArrayList<FilePage> getPages(DBFile DBFile) {
		throw new TransactionInvalidStateException("Action cannot be performed in state"+ this.toString());
	}



	@Override
	public void lockExclusive(FilePageID filePageID) throws TransactionAbortedException {
		throw new TransactionInvalidStateException("Action cannot be performed in state"+ this.toString());
	}

	@Override
	public void lockShared(FilePageID filePageID) throws TransactionAbortedException {
		throw new TransactionInvalidStateException("Action cannot be performed in state"+ this.toString());
	}

	@Override
	public void releaseExclusiveLock(FilePageID filePageID) {
		throw new TransactionInvalidStateException("Action cannot be performed in state"+ this.toString());
	}

	@Override
	public void releaseSharedLock(FilePageID filePageID) {
		throw new TransactionInvalidStateException("Action cannot be performed in state"+ this.toString());	
	}

	@Override
	public boolean doTryLockExclusive(FilePageID filePageID) {
		throw new TransactionInvalidStateException("Action cannot be performed in state"+ this.toString());
	}

	@Override
	public boolean doTryUpgradeLock(FilePageID filePageID) {
		throw new TransactionInvalidStateException("Action cannot be performed in state"+ this.toString());
	}

	@Override
	public void downgradeToShared(FilePageID filePageID) {
		 throw new TransactionInvalidStateException("Action cannot be performed in state"+ this.toString());
	}
	
	@Override
	public void addTuple(StoredTuple tuple, HeapFile heapFile) {
		throw new TransactionInvalidStateException("Action cannot be performed in state"+ this.toString());
	}
	
}
