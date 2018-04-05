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

/**
 * @author Facundo Manuel Quiroga Dec 26, 2008
 */
public class Timer {

	SortedQueue<Long, Notifiable> notifiablesQueue;
	protected long timeElapsedSinceStart;

	public Timer() {
		this.setNotifiablesQueue(new SortedQueue<Long, Notifiable>());
	}

	/**
	 * Register
	 * 
	 * @param notifiable
	 */
	public void notifyMeIn(long time, Notifiable notifiable) {
		long timeElapsedSinceStart = this.getTimeElapsedSinceStart();
		this.getNotifiablesQueue().add(time + timeElapsedSinceStart, notifiable);
	}

	public void timePassed(long timeElapsedSinceStart) {
		this.setTimeElapsedSinceStart(timeElapsedSinceStart);
		SortedQueue<Long, Notifiable> notifiablesQueue = this.getNotifiablesQueue();
		while (!notifiablesQueue.isEmpty() && notifiablesQueue.topKey() <= timeElapsedSinceStart) {
			Notifiable notifiable = notifiablesQueue.popValue();
			notifiable.notified();
		}
	}

	protected SortedQueue<Long, Notifiable> getNotifiablesQueue() {
		return this.notifiablesQueue;
	}

	protected void setNotifiablesQueue(SortedQueue<Long, Notifiable> notifiablesQueue) {
		this.notifiablesQueue = notifiablesQueue;
	}

	protected long getTimeElapsedSinceStart() {
		return this.timeElapsedSinceStart;
	}

	protected void setTimeElapsedSinceStart(long timeElapsedSinceStart) {
		this.timeElapsedSinceStart = timeElapsedSinceStart;
	}

	/*protected class Waiter{
		
		protected Notifiable notifiable;
		protected long time;
		
		public Waiter(Notifiable notifiable, long time){
			this.setNotifiable(notifiable);
			this.setTime(time);
		}

		protected Notifiable getNotifiable() {
			return this.notifiable;
		}

		protected void setNotifiable(Notifiable notifiable) {
			this.notifiable = notifiable;
		}

		protected long getTime() {
			return this.time;
		}

		protected void setTime(long time) {
			this.time = time;
		}
	}
	*/

}
