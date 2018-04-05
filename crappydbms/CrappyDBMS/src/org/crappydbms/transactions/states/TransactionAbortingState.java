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

import org.crappydbms.dbfiles.FilePageID;
import org.crappydbms.transactions.BaseTransaction;
import org.crappydbms.transactions.TransactionAbortedException;

/**
 * @author Facundo Manuel Quiroga
 * Jan 14, 2009
 * 
 */
public class TransactionAbortingState extends TransactionNotActiveState {

	/**
	 * @param baseTransaction
	 */
	public TransactionAbortingState(BaseTransaction baseTransaction) {
		super(baseTransaction);
	}
	
	public void performAbort(String message) throws TransactionAbortedException{
		BaseTransaction baseTransaction = this.getBaseTransaction();
		// release shared locks
		this.getTransactionLockingManager().releaseSharedLocks();
		// discard modified pages
		this.discardModifiedPages();
		// release exclusive locks
		this.getTransactionLockingManager().releaseExclusiveLocks();
		// discard pages local to the transaction
		this.getTransactionPageAddingManager().discardPages();
		// proceed to the AbortedState.
		this.setTransactionState(new TransactionAbortedState(baseTransaction,message));
		this.getBaseTransaction().getDatabase().getTransactionManager().aborted(this.getBaseTransaction());
		throw new TransactionAbortedException(message);
	}

	protected void discardModifiedPages() {
		for (FilePageID filePageID : this.getTransactionLockingManager().getExclusiveLocks().keySet()) {
			filePageID.getFile().discardPage(filePageID);
		}
		
	}

	@Override
	public String toString() {
		return "Aborting";
	}

}
