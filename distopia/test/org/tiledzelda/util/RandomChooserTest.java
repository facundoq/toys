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

package org.tiledzelda.util;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * @author Facundo Manuel Quiroga Mar 16, 2009
 */
public class RandomChooserTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testNew() {
		String[] strings = { "a", "b" };
		int[] probabilities = { 100, 0 };
		try {
			new RandomChooser<String>(strings, probabilities);
		} catch (IllegalArgumentException e) {
			Assert.fail("Arguments where correct, but: " + e);
		}
	}

	public void testInvalidNew() {
		String[] strings = { "a", "b" };
		int[] probabilities = { 100, 1 };
		try {
			new RandomChooser<String>(strings, probabilities);
			Assert.fail("Should throw IllegalArgumentException, sum >100");
		} catch (IllegalArgumentException e) {

		}

		String[] strings2 = { "a", "b" };
		int[] probabilities2 = { 100 };
		try {
			new RandomChooser<String>(strings2, probabilities2);
			Assert.fail("Should throw IllegalArgumentException, arrays of different lenght");
		} catch (IllegalArgumentException e) {

		}

		String[] strings3 = {};
		int[] probabilities3 = {};
		try {
			new RandomChooser<String>(strings3, probabilities3);
			Assert.fail("Should throw IllegalArgumentException, empty arrays");
		} catch (IllegalArgumentException e) {

		}

		String[] strings4 = { "a", "b", "c" };
		int[] probabilities4 = { 30, 20, 10 };
		try {
			new RandomChooser<String>(strings4, probabilities4);
			Assert.fail("Should throw IllegalArgumentException");
		} catch (IllegalArgumentException e) {

		}
		String[] strings5 = { "a", "b", "c" };
		int[] probabilities5 = { 90, 60, -50 };
		try {
			new RandomChooser<String>(strings5, probabilities5);
			Assert.fail("Should throw IllegalArgumentException");
		} catch (IllegalArgumentException e) {

		}
	}

	public void testGetObject() {
		String[] strings = { "a", "b" };
		int[] probabilities = { 100, 0 };
		try {
			RandomChooser<String> randomChooser = new RandomChooser<String>(strings, probabilities);
			for (int i = 0; i < 50; i++) {
				Assert.assertEquals("Should only return 'a' ", "a", randomChooser.getObject());
			}
		} catch (IllegalArgumentException e) {
			Assert.fail("Arguments where correct, but: " + e);
		}

		String[] strings2 = { "a", "b" };
		int[] probabilities2 = { 0, 100 };
		try {
			RandomChooser<String> randomChooser = new RandomChooser<String>(strings2, probabilities2);
			for (int i = 0; i < 50; i++) {
				Assert.assertEquals("Should only return 'b' ", "b", randomChooser.getObject());
			}
		} catch (IllegalArgumentException e) {
			Assert.fail("Arguments where correct, but: " + e);
		}

		String[] strings3 = { "a", "b", "c" };
		int[] probabilities3 = { 50, 0, 50 };
		try {
			RandomChooser<String> randomChooser = new RandomChooser<String>(strings3, probabilities3);
			for (int i = 0; i < 50; i++) {
				boolean equalsB = randomChooser.getObject().equals("b");
				Assert.assertFalse("Should not be a B", equalsB);

			}
		} catch (IllegalArgumentException e) {
			Assert.fail("Arguments where correct, but: " + e);
		}
	}

}
