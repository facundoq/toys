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

import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.crappydbms.exceptions.CrappyDBMSError;
import org.crappydbms.transactions.BaseTransaction;
import org.crappydbms.transactions.Transaction;

/**
 * @author Facundo Manuel Quiroga
 * Jan 8, 2009
 * 
 */
public class TransactionManager {
	
	
	
	protected Database database;
	
	protected Vector<Transaction> activeTransactions;
	protected Vector<Transaction> committedTransactions;
	protected Vector<Transaction> abortedTransactions;
	protected long transactionNumber;
	protected int maximumConcurrentTransactions;
	
	protected ReentrantLock newTransactionLock;
	protected Condition waitingTransactions;
	protected int activeTransactionsCount = 0;
	
	/**
	 * @param database
	 */
	public TransactionManager(Database database,int maximumConcurrentTransactions) {
		this.setDatabase(database);
		this.setActiveTransactions(new Vector<Transaction>());
		this.setAbortedTransactions(new Vector<Transaction>());
		this.setCommittedTransactions(new Vector<Transaction>());
		this.setTransactionNumber(0);
		this.setMaximumConcurrentTransactions(maximumConcurrentTransactions);
		this.setNewTransactionLock(new ReentrantLock());
		this.setWaitingTransactions(this.getNewTransactionLock().newCondition());
	}
	
	
	protected Database getDatabase() {
		return this.database;
	}
	protected void setDatabase(Database database) {
		this.database = database;
	}
	
	/**
	 * Create a new transaction, which will be executed in the current thread
	 */
	public Transaction newTransaction() {
		this.getNewTransactionLock().lock();
		try{
			while (this.getActiveTransactions().size() == this.getMaximumConcurrentTransactions()){
				try {
					this.getWaitingTransactions().await();
				} catch (InterruptedException e) {
					throw new CrappyDBMSError("Unable to start new transaction, interrupted");
				}
			}
			long transactionNumber = this.getTransactionNumber();
			this.setTransactionNumber(transactionNumber+1);
			Transaction transaction = new BaseTransaction(transactionNumber,this.getDatabase(),Thread.currentThread());
			this.getActiveTransactions().add(transaction);
			return transaction;	
		}finally{
			this.getNewTransactionLock().unlock();
		}
	}

	public void aborted(Transaction transaction){
		this.removeFinishedTransaction(transaction);
		this.getAbortedTransactions().add(transaction);
	}

	public void committed(Transaction transaction){
		this.removeFinishedTransaction(transaction);
		this.getCommittedTransactions().add(transaction);
	}
	public void removeFinishedTransaction(Transaction transaction){
		this.getNewTransactionLock().lock();
		try{
			if (!this.getActiveTransactions().remove(transaction)){
				throw new CrappyDBMSError(transaction+" was not active");
			}
			this.getWaitingTransactions().signal();
		}finally{
			this.getNewTransactionLock().unlock();
		}
	}

	protected long getTransactionNumber() {
		return this.transactionNumber;
	}


	protected void setTransactionNumber(long transactionNumber) {
		this.transactionNumber = transactionNumber;
	}


	protected Vector<Transaction> getActiveTransactions() {
		return this.activeTransactions;
	}
	protected Vector<Transaction> getActiveTransactionsCopy() {
		this.getNewTransactionLock().lock();
		try{
			Vector<Transaction> transactions = new Vector<Transaction>(this.getActiveTransactions());
			return transactions;
		}finally{
			this.getNewTransactionLock().unlock();
		}
	}


	protected void setActiveTransactions(Vector<Transaction> activeTransactions) {
		this.activeTransactions = activeTransactions;
	}


	protected Vector<Transaction> getCommittedTransactions() {
		return this.committedTransactions;
	}


	protected void setCommittedTransactions(Vector<Transaction> commitedTransactions) {
		this.committedTransactions = commitedTransactions;
	}


	protected Vector<Transaction> getAbortedTransactions() {
		return this.abortedTransactions;
	}


	protected void setAbortedTransactions(Vector<Transaction> abortedTransactions) {
		this.abortedTransactions = abortedTransactions;
	}


	/**
	 * @return all the transactions, whether active, committed, or aborted
	 */
	public ArrayList<Transaction> getTransactions() {
		ArrayList<Transaction> transactions = new ArrayList<Transaction>();
		transactions.addAll(this.getActiveTransactions());
		transactions.addAll(this.getCommittedTransactions());
		transactions.addAll(this.getAbortedTransactions());
		return transactions;
	}


	protected int getMaximumConcurrentTransactions() {
		return this.maximumConcurrentTransactions;
	}


	protected void setMaximumConcurrentTransactions(int maximumConcurrentTransactions) {
		this.maximumConcurrentTransactions = maximumConcurrentTransactions;
	}


	protected ReentrantLock getNewTransactionLock() {
		return this.newTransactionLock;
	}


	protected void setNewTransactionLock(ReentrantLock newTransactionLock) {
		this.newTransactionLock = newTransactionLock;
	}


	protected Condition getWaitingTransactions() {
		return this.waitingTransactions;
	}


	protected void setWaitingTransactions(Condition waitingTransactions) {
		this.waitingTransactions = waitingTransactions;
	}

}
