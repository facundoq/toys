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

import org.crappydbms.transactions.LastAction;
import org.crappydbms.transactions.Transaction;

/**
 * @author Facundo Manuel Quiroga
 * Jan 21, 2009
 * 
 */
public class StalledTransactionChecker extends Thread {
	
	protected static long defaultTimeBetweenChecks(){
		return 20000;
	}
	
	protected static long defaultSleepTimeWhenNotChecking(){
		return 60000;
	}
	
	protected static long initialMaxTimeElapsed(){
		return 100;
	}
	
	protected static long defaultMultiplicativeFactor() {
		return 2;
	}
	protected static double defaultWeightedAlpha() {
		return 0.89;
	}
	protected Database database;

	protected volatile boolean checking;
	protected volatile boolean finished;

	protected long timeBetweenChecks;

	protected long maxTimeElapsed;
	protected long sumOfTimesElapsed;
	protected int countOfTimesElapsed;
	protected double weightedAlpha;
	protected long multiplicativeFactor;

	public StalledTransactionChecker(Database database, long timeBetweenChecks) {
		this.setDatabase(database);
		this.finished = false;
		this.checking = false;
		this.timeBetweenChecks = timeBetweenChecks;

		this.maxTimeElapsed = initialMaxTimeElapsed();
		// initialize sum of times elapsed and count of time elapsed as if one time with maxTimeElapsed had already been counted
		this.sumOfTimesElapsed = this.maxTimeElapsed;
		this.countOfTimesElapsed = 1;
		this.multiplicativeFactor = defaultMultiplicativeFactor();
		this.weightedAlpha = defaultWeightedAlpha();
	}

	public StalledTransactionChecker(Database database) {
		this(database,defaultTimeBetweenChecks());
	}

	public void startChecking(){
		//System.out.println("started checking");
		this.checking = true;
		this.interrupt();
	}

	public void stopChecking(){
		//System.out.println("stopped checking");
		this.checking = false;
		this.interrupt();
	}
	
	public void finishChecking(){
		//System.out.println("finished");
		this.finished = true;
		this.interrupt();
	}

	public void run(){
		while (!this.finished){
			try {
			if (!this.checking){
				//System.out.println("not checking..");
					Thread.sleep(defaultSleepTimeWhenNotChecking());
			}else{
				//System.out.println("checking..");
				this.check();
				Thread.sleep(this.timeBetweenChecks);
			}
			} catch (InterruptedException e) {
				//System.out.println("interrupted.. "+e.getMessage()+ " checking " +checking + " finished "+ finished);
				// nothing wrong, just came out of sleep, check if we are finished, if not, if we should be checking
			}
		}
		if (Thread.interrupted()){
			//System.out.println("interrupted.. but no exception, checking " +checking + " finished "+ finished);
		}
	}

	/**
	 * Check if there are any stalled transactions; if so, interrupt them so the can abort
	 */
	protected void check() {
		
		long currentTime = System.currentTimeMillis();
		for (Transaction transaction : this.getTransactionManager().getActiveTransactionsCopy()){
			//System.out.println(transaction);
			LastAction lastAction = transaction.getLastAction();
			long timeElapsed = currentTime - lastAction.getTimestamp(); 
			this.updateTimeStatistics(timeElapsed);
			
			//System.out.println("timeOutTime " + this.getTimeoutTime() + "time elapsed "+ timeElapsed);
			if ( timeElapsed > this.getTimeoutTime() ){
				//System.out.println("aborting " + transaction);
				//transaction.abortFromAnotherThread();
			}

		}
		
	}

	/**
	 * @param timeElapsed
	 */
	private void updateTimeStatistics(long timeElapsed) {
		if (timeElapsed > this.maxTimeElapsed){
			this.maxTimeElapsed = timeElapsed;
		}
		this.sumOfTimesElapsed += timeElapsed;
		this.countOfTimesElapsed++;
	}

	/** 
	 * Calculates a weighted average between the maximum time elapsed yet seen,  
	 *  the accumulated mean of times elapsed.
	 *  Returns the weighted average  *  a multiplicative factor
	 * @return the maximum time a transaction should be occupied doing something
	 */
	private long getTimeoutTime() {
		long modifiedMaxTimeElapsed = (long) (this.maxTimeElapsed * this.weightedAlpha);
		long modifiedMean = (long) (  (this.sumOfTimesElapsed/this.countOfTimesElapsed) * (1-this.weightedAlpha) );
		return (modifiedMaxTimeElapsed+modifiedMean) * multiplicativeFactor;
	}

	protected Database getDatabase() {
		return this.database;
	}

	protected void setDatabase(Database database) {
		this.database = database;
	}
	protected TransactionManager getTransactionManager(){
		return this.getDatabase().getTransactionManager();
	}
	
	public boolean finished(){
		return this.finished;
	}
}
