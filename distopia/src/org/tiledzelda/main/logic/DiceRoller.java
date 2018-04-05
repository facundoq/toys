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

import org.tiledzelda.main.GameObjects;

/**
 * @author Facundo Manuel Quiroga Jul 22, 2009
 */

public class DiceRoller {

	/**
	 * @param probability from 1 to 100
	 * @return true if probability is less than the value of the normalizedDice
	 */
	public static boolean rollDiceWithProbability(int probability) {
		if (probability < 0 || probability > 100) {
			// TODO ¿be lenient and prob<0 -> prob = 0 and prob>100 -> prob = 100? 
			throw new IllegalArgumentException("Wrong probability value, should be 0 <= probability <= 100, but probability = " + probability + " .");
		}
		int lower = GameObjects.getInstance().getRandomNumberGenerator().getLowerRange();
		int upper = GameObjects.getInstance().getRandomNumberGenerator().getUpperRange();
		int random = GameObjects.getInstance().getRandomNumberGenerator().randomNumber();

		int normalizedUpper = upper - lower;
		int normalizedRandom = random - lower;
		int normalizedDice = (100 / normalizedUpper) * normalizedRandom;

		return probability > normalizedDice;
	}

	public static boolean rollDiceWithProbability(int numerator, int denominator) {
		return rollDiceWithProbabilityDouble(numerator / denominator);
	}

	public static boolean rollDiceWithProbabilityDouble(double probability) {
		return rollDiceWithProbability((int) (probability * 100));
	}
}
