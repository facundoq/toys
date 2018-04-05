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

package org.tiledzelda.model.elements.movableElements.statistics;

/**
 * Statistics are like properties of Elements. They may be Attributes, Skills, or be DerivedStatistics calculated from the values of the Element's Attributes and Skills.
 * 
 * @author Facundo Manuel Quiroga Nov 21, 2008
 */
public abstract class Statistic extends StatisticObservable {

	protected String name;

	public Statistic(String name) {
		super();
		this.setName(name);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name the name to set
	 */
	protected void setName(String name) {
		this.name = name;
	}

	public abstract float getValue();

	protected void changed() {
		for (StatisticObserver statisticObserver : this.getStatisticObservers()) {
			statisticObserver.statisticChanged(this);
		}
	}

	public String toString() {
		return this.getName() + ": " + this.getValue();
	}

}
