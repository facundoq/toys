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

import java.util.List;

import org.tiledzelda.model.elements.movableElements.statistics.Statistic;

/**
 * A Characteristic of a character (player, creep or NPC) that gets improved/worsened by the actions of the player. Can be an Attribute, like Strength, Intelligence, or a Skill like Dodge or Medicine
 * 
 * @author Facundo Manuel Quiroga Jul 25, 2009
 */
public abstract class Characteristic extends Statistic {

	protected ExperienceKeeper experienceKeeper;
	protected String name;
	protected int modifierPerUse;

	public Characteristic(String name, int experiencePointsModifierPerUse, List<Integer> experiencePointsRequiredPerLevel) {
		super(name);
		this.setExperienceKeeper(new ExperienceKeeper(experiencePointsRequiredPerLevel));
		this.setModifierPerUse(experiencePointsModifierPerUse);
	}

	public boolean usedWith(int intensity) {
		if (this.addExperiencePoints(intensity * this.getModifierPerUse())) {
			this.changed();
			return true;
		} else {
			return false;
		}
	}

	public float getValue() {
		return this.getCurrentLevel();
	}

	/*
	 * Wrapping of ExperienceKeeper
	 */
	/**
	 * adds experience points and
	 * 
	 * @param amount of xp to add
	 */
	protected boolean addExperiencePoints(int amount) {
		return this.getExperienceKeeper().addExperiencePoints(amount);
	}

	protected boolean reachedMaxLevel() {
		return this.getExperienceKeeper().reachedMaxLevel();
	}

	protected int maxLevel() {
		return this.getExperienceKeeper().maxLevel();
	}

	/**
	 * @return the amount of experience points that are needed to pass to the next level
	 */
	public int getExperiencePointsRequiredForCurrentLevel() {
		return this.getExperienceKeeper().experiencePointsRequiredForCurrentLevel();
	}

	/**
	 * @return the experience points gained for the current level
	 */
	public int getExperiencePointsOfCurrentLevel() {
		return this.getExperienceKeeper().getExperiencePointsOfCurrentLevel();
	}

	/**
	 * @return the current level of experience of the skill
	 */

	public int getCurrentLevel() {
		return this.getExperienceKeeper().getCurrentLevel();
	}

	public void resetCurrentLevelTo(int tentativeLevel, int experiencePoints) {
		this.experienceKeeper.resetCurrentLevelTo(tentativeLevel, experiencePoints);
	}

	/*
	 * SETTERS/GETTERS
	 */
	protected int getModifierPerUse() {
		return this.modifierPerUse;
	}

	protected void setModifierPerUse(int modifierPerUse) {
		this.modifierPerUse = modifierPerUse;
	}

	public ExperienceKeeper getExperienceKeeper() {
		return this.experienceKeeper;
	}

	protected void setExperienceKeeper(ExperienceKeeper experienceKeeper) {
		this.experienceKeeper = experienceKeeper;
	}

	public String toString() {
		int level = this.getCurrentLevel();
		int currentExp = this.getExperiencePointsOfCurrentLevel();
		int requiredExp = this.getExperiencePointsRequiredForCurrentLevel();
		String string = this.getName() + ": " + level + " (" + currentExp + "/" + requiredExp + ")";
		return string;
	}
}
