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
import java.util.List;

/**
 * A class that given a list of experience requirements per level, manages the adding of experience, as well as the possible increase in level
 * 
 * @author Facundo Manuel Quiroga Nov 20, 2008
 */
public class ExperienceKeeper {

	public static List<Integer> commonExperiencePointsPerLevel() {
		ArrayList<Integer> experiencePointsPerLevel = new ArrayList<Integer>();
		for (int i = 0; i < 100; i++) {
			experiencePointsPerLevel.add(10 + (i * i));
		}
		return experiencePointsPerLevel;
	}

	protected int experiencePointsOfCurrentLevel;
	protected List<Integer> experiencePointsRequiredPerLevel;
	protected int currentLevel;

	/**
	 * The max level is implicit as the size of the experiencePointsRequiredPerLevel list. When the max level is reached, further attempts to increase the experience points will result in nothing
	 * happening
	 * 
	 * @param experiencePointsRequiredPerLevel
	 */

	public ExperienceKeeper(List<Integer> experiencePointsRequiredPerLevel) {
		this.setExperiencePointsRequiredPerLevel(new ArrayList<Integer>());
		this.getExperiencePointsRequiredPerLevel().addAll(experiencePointsRequiredPerLevel);
		this.resetCurrentLevelTo(0, 0);
	}

	/**
	 * Sets the level to newLevel with experiencePoints earned for the newLevel
	 * 
	 * @param newLevel to be set
	 * @param experiencePoints of new level
	 */
	public void resetCurrentLevelTo(int newLevel, int experiencePoints) {
		int level = Math.min(newLevel, this.maxLevel());
		this.setCurrentLevel(level);
		this.setExperiencePointsOfCurrentLevel(experiencePoints);
	}

	/**
	 * adds experience points and passes to next level if necessary.
	 * 
	 * @param amount of xp to add
	 * @returns true if the adding of experience points caused an increase to the next level
	 */
	public boolean addExperiencePoints(int amount) {
		//TODO what happens if i get enough experience for two levels in a row?
		if (!this.reachedMaxLevel()) {
			int newExperiencePoints = this.getExperiencePointsOfCurrentLevel() + amount;
			if (newExperiencePoints >= this.experiencePointsRequiredForCurrentLevel()) {
				// set the remaining experience points of the next level to 0
				newExperiencePoints = 0;
				this.currentLevel++;
				return true;
			} else {
				this.setExperiencePointsOfCurrentLevel(newExperiencePoints);
				return false;
			}
		}
		return false;
	}

	public boolean reachedMaxLevel() {
		return this.getCurrentLevel() == this.maxLevel();
	}

	public int maxLevel() {
		return this.getExperiencePointsRequiredPerLevel().size();
	}

	/**
	 * @return the amount of experience points that are needed to pass to the next level
	 */
	public int experiencePointsRequiredForCurrentLevel() {

		return this.getExperiencePointsRequiredPerLevel().get(this.getCurrentLevel());
	}

	public int getExperiencePointsOfCurrentLevel() {
		return this.experiencePointsOfCurrentLevel;
	}

	protected void setExperiencePointsOfCurrentLevel(int experiencePoints) {
		this.experiencePointsOfCurrentLevel = experiencePoints;
	}

	public List<Integer> getExperiencePointsRequiredPerLevel() {
		return this.experiencePointsRequiredPerLevel;
	}

	protected void setExperiencePointsRequiredPerLevel(List<Integer> experiencePointsRequiredPerLevel) {
		this.experiencePointsRequiredPerLevel = experiencePointsRequiredPerLevel;
	}

	public int getCurrentLevel() {
		return this.currentLevel;
	}

	protected void setCurrentLevel(int currentLevel) {
		this.currentLevel = currentLevel;
	}

}
