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

import org.crappydbms.exceptions.CrappyDBMSError;

/**
 * @author Facundo Manuel Quiroga Jan 19, 2009
 */
public class TransactionLockingStatistics {
	// TODO refactor, the calculations for each (shared, exclusive, upgraded) are the same

	public static long defaultMultiplicativeFactor = 1;
	public static long defaultMinWaitingTime = 30000;
	public static double defaultWeightedMeanAlpha = 0.2;
	public static long defaultCutOffTime = 2;

	protected long exclusiveSum;
	protected long exclusiveCount;

	protected long upgradedSum;
	protected long upgradedCount;

	protected long sharedSum;
	protected long sharedCount;

	protected long exclusiveWeightedMean;
	protected long upgradedWeightedMean;
	protected long sharedWeightedMean;

	// the alpha is how much importance is given to the last measure
	protected double weightedMeanAlpha;

	/*protected float upgradedWeightedMeanAlpha;
	protected float sharedWeightedMeanAlpha;*/

	protected enum State {
		IDLE, WAITING_SHARED, WAITING_UPGRADE, WAITING_EXCLUSIVE
	}

	protected State state;

	protected long startedTimestamp;

	protected long multiplicativeFactor;
	protected long minWaitingTime;
	protected long cutOffTime;

	public TransactionLockingStatistics(long multiplicativeFactor, long minWaitingTime, double weightedMeanAlpha, long cutOffTime) {
		if (!(weightedMeanAlpha > 0 && weightedMeanAlpha < 1)) {
			throw new CrappyDBMSError("Invalid weightedMeanAlpha, " + weightedMeanAlpha);
		}

		this.multiplicativeFactor = multiplicativeFactor;
		this.minWaitingTime = minWaitingTime;
		this.weightedMeanAlpha = weightedMeanAlpha;
		this.cutOffTime = cutOffTime;

		this.exclusiveCount = 0;
		this.exclusiveSum = 0;
		this.exclusiveWeightedMean = this.minWaitingTime;

		this.sharedCount = 0;
		this.sharedSum = 0;
		this.sharedWeightedMean = this.minWaitingTime;

		this.upgradedCount = 0;
		this.upgradedSum = 0;
		this.upgradedWeightedMean = this.minWaitingTime;

		this.state = State.IDLE;
	}

	public TransactionLockingStatistics() {
		this(defaultMultiplicativeFactor, defaultMinWaitingTime, defaultWeightedMeanAlpha, defaultCutOffTime);
	}

	/**
	 * @return the amount of maximum milliseconds the thread should wait for a exclusive lock.
	 */
	public long calculateMaxWaitTimeForExclusiveLock() {
		//System.out.println("Exclusive mean " + this.exclusiveWeightedMean + " mean " + this.exclusiveMean());
		return Math.max(this.minWaitingTime, this.exclusiveWeightedMean * this.multiplicativeFactor);
	}

	/**
	 * @return the amount of maximum milliseconds the thread should wait when upgrading a lock.
	 */
	public long calculateMaxWaitTimeForUpgradingLock() {
		//System.out.println("upgrade mean " + this.upgradedWeightedMean + " mean " + this.upgradedMean());
		return Math.max(this.minWaitingTime, this.upgradedWeightedMean * this.multiplicativeFactor);
	}

	/**
	 * @return the amount of maximum milliseconds the thread should wait for a shared lock.
	 */
	public long calculateMaxWaitTimeForSharedLock() {
		//System.out.println("shared mean " + this.sharedWeightedMean + " mean " + this.sharedMean());
		return Math.max(this.minWaitingTime, this.sharedWeightedMean * this.multiplicativeFactor);
	}

	/**
	 * 
	 */
	public void lockingShared() {
		this.startLockingInState(State.WAITING_SHARED);
	}

	public void lockedShared() {
		this.assertState(State.WAITING_SHARED);
		long currentTimestamp = this.getCurrentTime();
		long timeElapsed = currentTimestamp - this.startedTimestamp;
		if (timeElapsed >= this.cutOffTime) {
			this.sharedCount++;
			this.sharedSum += timeElapsed;
			this.sharedWeightedMean = this.calculateWeightedMean(this.sharedWeightedMean, timeElapsed);
		}
		this.state = State.IDLE;
	}

	public void lockingExclusive() {
		this.startLockingInState(State.WAITING_EXCLUSIVE);
	}

	public void lockedExclusive() {
		this.assertState(State.WAITING_EXCLUSIVE);
		long currentTimestamp = this.getCurrentTime();
		long timeElapsed = currentTimestamp - this.startedTimestamp;
		if (timeElapsed >= this.cutOffTime) {
			this.exclusiveCount++;
			this.exclusiveSum += timeElapsed;
			this.exclusiveWeightedMean = this.calculateWeightedMean(this.exclusiveWeightedMean, timeElapsed);
		}
		this.state = State.IDLE;
	}

	public void upgradingToExclusiveLock() {
		this.startLockingInState(State.WAITING_UPGRADE);
	}

	public void upgradedToExclusiveLock() {
		this.assertState(State.WAITING_UPGRADE);
		long currentTimestamp = this.getCurrentTime();
		long timeElapsed = currentTimestamp - this.startedTimestamp;
		if (timeElapsed >= this.cutOffTime) {
			this.upgradedCount++;
			this.upgradedSum += timeElapsed;
			this.upgradedWeightedMean = this.calculateWeightedMean(this.upgradedWeightedMean, timeElapsed);
		}
		this.state = State.IDLE;
	}

	protected void startLockingInState(State state) {
		this.assertState(State.IDLE);
		this.startedTimestamp = this.getCurrentTime();
		this.state = state;
	}

	protected void assertState(State state) {
		if (!(this.state == state)) {
			throw new CrappyDBMSError("");
		}
	}

	/**
	 * @param previousSharedWeightedMean
	 * @param timeElapsed in the last locking
	 * @return the new Weighted Mean
	 */
	private long calculateWeightedMean(long previousSharedWeightedMean, long timeElapsed) {
		long modifiedWeightedMean = (long) (previousSharedWeightedMean * (1 - this.weightedMeanAlpha));
		long modifiedTimeElapsed = (long) (timeElapsed * this.weightedMeanAlpha);
		return modifiedWeightedMean + modifiedTimeElapsed;
	}

	protected long getCurrentTime() {
		return System.currentTimeMillis();
	}

	public long exclusiveMean() {
		if (this.exclusiveCount == 0) {
			return 0;
		}
		return this.exclusiveSum / this.exclusiveCount;
	}

	public long sharedMean() {
		if (this.sharedCount == 0) {
			return 0;
		}
		return this.sharedSum / this.sharedCount;
	}

	public long upgradedMean() {
		if (this.upgradedCount == 0) {
			return 0;
		}
		return this.upgradedSum / this.upgradedCount;
	}
}
