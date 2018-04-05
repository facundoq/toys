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

import org.tiledzelda.exceptions.GameError;
import org.tiledzelda.main.logic.DiceRoller;
import org.tiledzelda.model.elements.movableElements.statistics.Statistic;
import org.tiledzelda.model.elements.movableElements.statistics.characteristics.attributes.Attribute;
import org.tiledzelda.model.elements.movableElements.statistics.characteristics.skills.Skill;

/**
 * The set of Attributes, Skills, and Statistics of a player
 * 
 * @author Facundo Manuel Quiroga Nov 20, 2008
 */
public abstract class Characteristics {

	public abstract ArrayList<Attribute> getAttributes();

	public Attribute getAttributeNamed(String name) {
		for (Attribute attribute : this.getAttributes()) {
			if (attribute.getName().equals(name)) {
				return attribute;
			}
		}
		throw new GameError("Non-existent attribute " + name + " .");
	}

	public void usedAttributeWithIntensityAndProbability(String name, int intensity, int probability) {
		if (DiceRoller.rollDiceWithProbability(probability)) {
			this.getAttributeNamed(name).usedWith(intensity);
		}
	}

	public void usedAttributeWithIntensity(String name, int intensity) {
		this.usedAttributeWithIntensityAndProbability(name, intensity, 100);
	}

	public abstract ArrayList<Skill> getSkills();

	public Skill getSkillNamed(String name) {
		for (Skill skill : this.getSkills()) {
			if (skill.getName().equals(name)) {
				return skill;
			}
		}
		throw new GameError("Non-existent skill " + name + " .");
	}

	public abstract ArrayList<Statistic> getStatistics();

	public Statistic getStatisticNamed(String name) {
		for (Statistic statistic : this.getStatistics()) {
			if (statistic.getName().equals(name)) {
				return statistic;
			}
		}
		throw new GameError("Non-existent statistic  " + name + " .");
	}

	public void usedSkillWithIntensityAndProbability(String name, int intensity, int probability) {
		if (DiceRoller.rollDiceWithProbability(probability)) {
			this.getSkillNamed(name).usedWith(intensity);
		}
	}

	public void usedSkillWithIntensity(String name, int intensity) {
		this.usedSkillWithIntensityAndProbability(name, intensity, 100);
	}

}
