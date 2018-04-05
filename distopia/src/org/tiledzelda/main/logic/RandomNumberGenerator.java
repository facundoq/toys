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
 * Generates an array of random numbers and returns a different one each time a random number is requested The array's values can be created again with the message randomize
 * 
 * @author Facundo Quiroga Creation date: 27/09/2008 23:29:51
 */
public class RandomNumberGenerator {

	public static int defaultNumberOfNumbers() {
		return 150;
	}

	// generate
	private int[] numbers;
	private int currentNumber;
	private int numberOfNumbers;
	private int lowerRange = 0;
	private int upperRange = 100;

	public RandomNumberGenerator(int numberOfNumbers) {
		this.setNumberOfNumbers(numberOfNumbers);
		this.setCurrentNumber(0);
		this.setLowerRange(0);
		this.setUpperRange(100);
	}

	public int getLowerRange() {
		return lowerRange;
	}

	public void setLowerRange(int lowerRange) {
		this.lowerRange = lowerRange;
		this.randomize();
	}

	public int getUpperRange() {
		return upperRange;
	}

	public void setUpperRange(int upperRange) {
		this.upperRange = upperRange;
		this.randomize();
	}

	private int getCurrentNumber() {
		return currentNumber;
	}

	private void setCurrentNumber(int currentNumber) {
		this.currentNumber = currentNumber;
	}

	public int getNumberOfNumbers() {
		return numberOfNumbers;
	}

	public void setNumberOfNumbers(int numberOfNumbers) {
		this.setCurrentNumber(0);
		this.numberOfNumbers = numberOfNumbers;
		this.setNumbers(new int[numberOfNumbers]);
		this.randomize();
	}

	private int[] getNumbers() {
		return numbers;
	}

	private void setNumbers(int[] numbers) {
		this.numbers = numbers;
	}

	/**
	 * @return a random number between lowerRange and upperRange
	 */
	public int randomNumber() {
		int currentNumber = getCurrentNumber();
		int numberOfNumbers = getNumberOfNumbers();
		int result = getNumbers()[currentNumber];
		currentNumber = (currentNumber + result) % numberOfNumbers;
		this.setCurrentNumber(currentNumber);
		return result;
	}

	public void randomize() {
		int[] numbers = getNumbers();
		int lowerRange = getLowerRange();
		int upperRange = getUpperRange();
		int difference = upperRange - lowerRange;
		for (int i = 0; i < numberOfNumbers; i++) {
			numbers[i] = (int) (Math.random() * difference) + lowerRange;
		}
	}
}
