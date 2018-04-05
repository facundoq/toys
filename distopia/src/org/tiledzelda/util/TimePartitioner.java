/* TiledZelda, a top-down 2d action-rpg game written in Java.
    Copyright (C) 2008  Facundo Manuel Quiroga <facundoq@gmail.com>
 	
 	This file is part of TiledZelda.

    TiledZelda is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    TiledZelda is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with TiledZelda.  If not, see <http://www.gnu.org/licenses/>.
*/

package org.tiledzelda.util;

/**
 * Partitions time in bigger units than ms so that another object can know how much partitions have passed. For example, It can partition time in units of 100ms, and objects can know how many times
 * that amount of time has passed and how much time is left for the next time it'll pass without having to repeat the counting logic.
 * 
 * @author Facundo Manuel Quiroga Mar 15, 2009
 */
public class TimePartitioner {
	protected int remainingMilliseconds;
	protected int partitionDuration;

	public TimePartitioner(int partitionDuration) {
		this.setPartitionDuration(partitionDuration);
		this.setRemainingMilliseconds(0);
	}

	/*
	 * Updates the TimePartitioner with the timeElapsed, and returns how many partitions have passed
	 * (not since the last update, but not since ever either; since the last time a partition was returned)
	 */
	public int update(long timeElapsed) {
		int remainingMilliseconds = this.getRemainingMilliseconds();
		remainingMilliseconds += timeElapsed;
		int partitionsPassed = remainingMilliseconds / partitionDuration;
		remainingMilliseconds = remainingMilliseconds % partitionDuration;
		this.setRemainingMilliseconds(remainingMilliseconds);
		return partitionsPassed;
	}

	/**
	 * Changes the duration of the partitions
	 */
	public void changePartitionDuration(int partitionDuration) {
		this.setPartitionDuration(partitionDuration);
	}

	protected int getRemainingMilliseconds() {
		return this.remainingMilliseconds;
	}

	protected void setRemainingMilliseconds(int remainingMilliseconds) {
		this.remainingMilliseconds = remainingMilliseconds;
	}

	protected int getPartitionDuration() {
		return this.partitionDuration;
	}

	protected void setPartitionDuration(int partitionDuration) {
		this.partitionDuration = partitionDuration;
	}

}
