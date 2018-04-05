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

import java.util.ArrayList;

/**
 * @author Facundo Manuel Quiroga Jul 26, 2009
 */
public class StatisticObservable {

	protected ArrayList<StatisticObserver> statisticObservers;

	public StatisticObservable() {
		this.setStatisticObservers(new ArrayList<StatisticObserver>());
	}

	public void registerStatisticObserver(StatisticObserver statisticObserver) {
		if (!this.isStatisticObserver(statisticObserver)) {
			this.getStatisticObservers().add(statisticObserver);
		}
	}

	public void unregisterStatisticObserver(StatisticObserver statisticObserver) {
		if (this.isStatisticObserver(statisticObserver)) {
			this.getStatisticObservers().remove(statisticObserver);
		}
	}

	public boolean isStatisticObserver(StatisticObserver statisticObserver) {
		return this.getStatisticObservers().contains(statisticObserver);
	}

	/* SETTERS / GETTERS */
	protected ArrayList<StatisticObserver> getStatisticObservers() {
		return this.statisticObservers;
	}

	protected void setStatisticObservers(ArrayList<StatisticObserver> statisticObservers) {
		this.statisticObservers = statisticObservers;
	}

}
