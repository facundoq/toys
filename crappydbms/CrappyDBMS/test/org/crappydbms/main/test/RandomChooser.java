/* CrappyDBMS, a simple relational DBMS written in Java.
    Copyright (C) 2008  Facundo Manuel Quiroga <facundoq@gmail.com>
 	
 	This file is part of CrappyDBMS.

    CrappyDBMS is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    CrappyDBMS is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with CrappyDBMS.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.crappydbms.main.test;

import java.util.Arrays;
import java.util.Random;


/**
 * @author Facundo Manuel Quiroga
 * Jan 26, 2009
 * 
 */
public class RandomChooser<T> {

	T[] objects;
	int[] probabilities;
	
		public RandomChooser(T[] objects, int[] probabilities){
			if (objects.length != probabilities.length){
				throw new IllegalArgumentException("Array of objects to be returned and array of probabilities don't have the same lenght, "+objects.length+" - "+probabilities.length);
			}
			if ( (objects.length == 0) || (probabilities.length == 0) ){
				throw new IllegalArgumentException("Both arrays need to contain elements");
			}
			int sumOfProbabilities = this.sumOfProbabilities(probabilities); 
			if ( sumOfProbabilities != 100){
				throw new IllegalArgumentException("The sum of the individual probabilities must equal 100");
			}
			if (this.hasNegativeNumbers(probabilities)){
				throw new IllegalArgumentException("Probabilities should be >0 and <=100");
			}
			
			
			this.objects = Arrays.copyOf(objects, objects.length);
			this.probabilities = Arrays.copyOf(probabilities,probabilities.length);
		}


		/**
		 * @param probabilities2
		 * @return
		 */
		private boolean hasNegativeNumbers(int[] probabilities) {
			for (int probability : probabilities){
				if (probability <0){
					return true;
				}
			}
			return false;
		}


		protected int sumOfProbabilities(int[] probabilities) {
			int sumOfProbabilities = 0;
			for (int probability : probabilities){
				sumOfProbabilities += probability;
			}
			return sumOfProbabilities;
		}

		public T getObject(){
			int randomNumber = new Random().nextInt(100)+1;
			int sumOfProbabilities = 0;
			int objectIndex = 0;
			for (int i = 0 ;  sumOfProbabilities < randomNumber; i++ ){
				assert(i<this.probabilities.length);
				sumOfProbabilities += this.probabilities[i];
				objectIndex = i;
			}
			return this.objects[objectIndex];
		}
}
