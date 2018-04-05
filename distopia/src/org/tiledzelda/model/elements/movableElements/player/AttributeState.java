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

package org.tiledzelda.model.elements.movableElements.player;

import org.tiledzelda.model.elements.movableElements.statistics.Statistic;
import org.tiledzelda.model.elements.movableElements.statistics.StatisticObserver;
import org.tiledzelda.util.TimePartitioner;

/**
 * @author Facundo Manuel Quiroga Mar 15, 2009
 */
public class AttributeState implements StatisticObserver {

	protected String name;
	protected int currentValue;
	protected Statistic maximumValue;
	protected Statistic restorationRate;
	protected TimePartitioner timePartitioner;

	/**
	 * @param startingValue of the attribute
	 * @param name of the attribute
	 * @param maximumValue of the attribute
	 * @param restorationRate : One unit per every restorationRate milliseconds
	 */

	public AttributeState(String name, int startingValue, Statistic maximumValue, Statistic restorationRate) {
		this.setName(name);
		this.setCurrentValue(startingValue);
		this.setMaximumValue(maximumValue);
		this.setRestorationRate(restorationRate);
		this.setTimePartitioner(new TimePartitioner((int) restorationRate.getValue()));

		restorationRate.registerStatisticObserver(this);
	}

	public void update(long timeElapsed) {
		int amount = this.getTimePartitioner().update(timeElapsed);
		this.updateCurrentValue(amount);
	}

	public void updateCurrentValue(int amount) {
		int newCurrentValue = this.getCurrentValue() + amount;
		if (newCurrentValue > this.getMaximumValue().getValue()) {
			newCurrentValue = (int) this.getMaximumValue().getValue();
		} else if (newCurrentValue < 0) {
			newCurrentValue = 0;
		}
		this.setCurrentValue(newCurrentValue);
	}

	public String getName() {
		return this.name;
	}

	protected void setName(String name) {
		this.name = name;
	}

	public int getCurrentValue() {
		return this.currentValue;
	}

	protected void setCurrentValue(int currentValue) {
		this.currentValue = currentValue;
	}

	protected TimePartitioner getTimePartitioner() {
		return this.timePartitioner;
	}

	protected void setTimePartitioner(TimePartitioner timePartitioner) {
		this.timePartitioner = timePartitioner;
	}

	public Statistic getMaximumValue() {
		return this.maximumValue;
	}

	protected void setMaximumValue(Statistic maximumValue) {
		this.maximumValue = maximumValue;
	}

	protected Statistic getRestorationRate() {
		return this.restorationRate;
	}

	protected void setRestorationRate(Statistic restorationRate) {
		this.restorationRate = restorationRate;
	}

	public void statisticChanged(Statistic statistic) {
		if (statistic.equals(this.getRestorationRate())) {
			this.getTimePartitioner().changePartitionDuration((int) statistic.getValue());
		}
	}

}
