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

package org.tiledzelda.model.elements.movableElements.statistics.characteristics;

import java.util.ArrayList;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * @author Facundo Manuel Quiroga Nov 20, 2008
 */
public class ExperienceKeeperTest extends TestCase {

	Integer[] perLevelValues = { 5, 6, 7, 10, 25 };
	ArrayList<Integer> experiencePointsRequiredPerLevel;

	int[] amounts = { 4, 1, 4, 3, 7, 8, 39, 26, 1, 50 };
	int[] expectedExperience = { 4, 0, 4, 0, 0, 8, 0, 0, 0, 0 };
	int[] expectedLevels = { 0, 1, 1, 2, 3, 3, 4, 5, 5, 5 };

	protected void setUp() throws Exception {
		experiencePointsRequiredPerLevel = new ArrayList<Integer>();
		for (Integer value : perLevelValues) {
			experiencePointsRequiredPerLevel.add(value);
		}
	}

	public void testNew() {
		ExperienceKeeper experienceKeeper = this.newExperienceKeeper();
		Assert.assertEquals("level should be 0", 0, experienceKeeper.getCurrentLevel());
		Assert.assertEquals("level should be 0", 0, experienceKeeper.getExperiencePointsOfCurrentLevel());
	}

	private ExperienceKeeper newExperienceKeeper() {
		return new ExperienceKeeper(experiencePointsRequiredPerLevel);
	}

	public void testUse() {
		ExperienceKeeper experienceKeeper = this.newExperienceKeeper();
		for (int i = 0; i < amounts.length; i++) {
			experienceKeeper.addExperiencePoints(amounts[i]);
			Assert.assertEquals("test equal level after iteration " + i, expectedLevels[i], experienceKeeper.getCurrentLevel());
			Assert.assertEquals("test equal experiencePoints after iteration " + i, expectedExperience[i], experienceKeeper.getExperiencePointsOfCurrentLevel());
		}
	}

	public void testReset() {
		ExperienceKeeper experienceKeeper = this.newExperienceKeeper();
		for (int i = 0; i < amounts.length; i++) {
			experienceKeeper.addExperiencePoints(amounts[i]);
			Assert.assertEquals("test equal level after iteration " + i, expectedLevels[i], experienceKeeper.getCurrentLevel());
			Assert.assertEquals("test equal experiencePoints after iteration " + i, expectedExperience[i], experienceKeeper.getExperiencePointsOfCurrentLevel());
		}
	}

	protected void tearDown() throws Exception {

	}

}
