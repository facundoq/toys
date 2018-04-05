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

package org.tiledzelda.model.elements.movableElements.statistics.characteristics.attributes;

import java.util.ArrayList;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * @author Facundo Manuel Quiroga Nov 20, 2008
 */
public class AttributeTest extends TestCase {
	Integer[] perLevelValues = { 5, 6, 7, 10, 25 };
	ArrayList<Integer> experiencePointsRequiredPerUse;
	String name;
	int modifierPerUse = 2;

	int[] intensities = { 2, 1, 1, 2, 4, 15, 13, 30, 0 };
	int[] expectedExperience = { 4, 0, 2, 0, 0, 0, 0, 0, 0 };
	int[] expectedLevels = { 0, 1, 1, 2, 3, 4, 5, 5, 5 };

	protected void setUp() throws Exception {
		experiencePointsRequiredPerUse = new ArrayList<Integer>();
		for (Integer value : perLevelValues) {
			experiencePointsRequiredPerUse.add(value);
		}
		name = "Strength";
	}

	public void testNew() {
		Attribute attribute = this.newAttribute();
		Assert.assertEquals("attribute names should match", name, attribute.getName());
		Assert.assertEquals("level should be 0", 0, attribute.getCurrentLevel());
		Assert.assertEquals("experience points should be 0", 0, attribute.getExperiencePointsOfCurrentLevel());
	}

	protected Attribute newAttribute() {
		return new Attribute(name, modifierPerUse, experiencePointsRequiredPerUse);
	}

	public void testUse() {
		Attribute attribute = this.newAttribute();
		for (int i = 0; i < intensities.length; i++) {
			attribute.usedWith(intensities[i]);
			Assert.assertEquals("test equal level after iteration " + i, expectedLevels[i], attribute.getCurrentLevel());
			Assert.assertEquals("test equal experiencePoints after iteration " + i, expectedExperience[i], attribute.getExperiencePointsOfCurrentLevel());
		}
	}

	protected void tearDown() throws Exception {

	}

}
