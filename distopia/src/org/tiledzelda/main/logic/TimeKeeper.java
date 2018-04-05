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

package org.tiledzelda.main.logic;

public class TimeKeeper {

	private long startTime;
	private long currTime;
	private long elapsedTimeSinceStart;
	private long elapsedTimeSinceLastUpdate;

	public TimeKeeper() {
		startTime = System.currentTimeMillis();
		currTime = startTime;
		elapsedTimeSinceStart = 0;
		elapsedTimeSinceLastUpdate = 0;
	}

	/*
	this.setStartTime(System.currentTimeMillis());
	this.setCurrTime(this.getStartTime());
	this.setElapsedTimeFromStart(0);
	this.setElapsedTimeSinceLastUpdate(0);*/

	public void update() {
		elapsedTimeSinceLastUpdate = System.currentTimeMillis() - currTime;
		currTime += elapsedTimeSinceLastUpdate;
		elapsedTimeSinceStart += elapsedTimeSinceLastUpdate;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getCurrTime() {
		return currTime;
	}

	protected void setCurrTime(long currTime) {
		this.currTime = currTime;
	}

	public long getElapsedTimeSinceLastUpdate() {
		return elapsedTimeSinceLastUpdate;
	}

	protected void setElapsedTimeSinceLastUpdate(long elapsedTimeSinceLastUpdate) {
		this.elapsedTimeSinceLastUpdate = elapsedTimeSinceLastUpdate;
	}

	public long getElapsedTimeSinceStart() {
		return elapsedTimeSinceStart;
	}

	protected void setElapsedTimeSinceStart(long elapsedTimeSinceStart) {
		this.elapsedTimeSinceStart = elapsedTimeSinceStart;
	}
}
