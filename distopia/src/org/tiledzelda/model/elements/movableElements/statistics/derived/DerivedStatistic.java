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

package org.tiledzelda.model.elements.movableElements.statistics.derived;

import java.util.ArrayList;

import org.tiledzelda.model.elements.movableElements.statistics.Statistic;
import org.tiledzelda.model.elements.movableElements.statistics.StatisticObserver;

/**
 * @author Facundo Manuel Quiroga Jul 25, 2009
 */
public abstract class DerivedStatistic extends Statistic implements StatisticObserver {

	protected float value;
	protected float baseValue;

	protected ArrayList<Statistic> dependingStatistics;

	/**
	 * @param name
	 */
	public DerivedStatistic(String name, float baseValue, ArrayList<Statistic> dependingStatistics) {
		super(name);
		this.setDependingStatistics(new ArrayList<Statistic>(dependingStatistics));
		this.setBaseValue(baseValue);
		this.registerForDependingStatistics();
		this.recalculate();
	}

	public DerivedStatistic(String name, ArrayList<Statistic> dependingStatistics) {
		this(name, 0, dependingStatistics);
	}

	public DerivedStatistic(String name) {
		this(name, new ArrayList<Statistic>());
	}

	public void registerForDependingStatistics() {
		for (Statistic statistic : this.getDependingStatistics()) {
			statistic.registerStatisticObserver(this);
		}
	}

	public abstract float calculate();

	protected Statistic getStatistic(String name) {
		for (Statistic statistic : this.getDependingStatistics()) {
			if (statistic.getName().equals(name)) {
				return statistic;
			}
		}
		throw new IllegalArgumentException("Invalid statistic " + name + " in DerivedStatistic " + name);
	}

	protected float getValueOfDependingStatistic(String name) {
		return this.getStatistic(name).getValue();
	}

	protected void recalculate() {
		this.setValue(this.calculate());
		this.changed();
	}

	public void statisticChanged(Statistic statistic) {
		//System.out.println("Got update "+this.toString() );
		this.recalculate();
	}

	/* SETTERS AND GETTERS */
	protected void setValue(float value) {
		this.value = value;
	}

	public float getValue() {
		return this.value;
	}

	protected float getBaseValue() {
		return this.baseValue;
	}

	protected void setBaseValue(float baseValue) {
		this.baseValue = baseValue;
	}

	protected ArrayList<Statistic> getDependingStatistics() {
		return this.dependingStatistics;
	}

	protected void setDependingStatistics(ArrayList<Statistic> dependingStatistics) {
		this.dependingStatistics = dependingStatistics;
	}
}
