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
import org.tiledzelda.model.elements.movableElements.statistics.characteristics.attributes.Attribute;

/**
 * @author Facundo Manuel Quiroga Jul 26, 2009
 */
public class StaminaRecoveryRateStatistic extends DerivedStatistic {

	public static StaminaRecoveryRateStatistic newStaminaRecoveryRate(float baseValue, Attribute endurance) {
		ArrayList<Statistic> statistics = new ArrayList<Statistic>();
		;
		statistics.add(endurance);
		return new StaminaRecoveryRateStatistic("StaminaRecoveryRate", baseValue, statistics);
	}

	public StaminaRecoveryRateStatistic(String name, float baseValue, ArrayList<Statistic> dependingStatistics) {
		super(name, baseValue, dependingStatistics);
	}

	@Override
	public float calculate() {
		float value = this.getBaseValue() - (this.getValueOfDependingStatistic("Endurance") / 2);
		return (value > 0) ? value : 1;
	}

}
