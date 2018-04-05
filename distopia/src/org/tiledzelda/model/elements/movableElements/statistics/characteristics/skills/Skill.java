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

package org.tiledzelda.model.elements.movableElements.statistics.characteristics.skills;

import java.util.ArrayList;
import java.util.List;

import org.tiledzelda.exceptions.GameError;
import org.tiledzelda.model.elements.movableElements.statistics.characteristics.Characteristic;
import org.tiledzelda.model.elements.movableElements.statistics.characteristics.attributes.Attribute;

/**
 * A specific Characteristic of an Element, it represents it's ability in a certain area of expertise
 * 
 * @author Facundo Manuel Quiroga Nov 20, 2008
 */
public class Skill extends Characteristic {

	protected List<Skill> dependingSkills;
	// the amount that the use of this skill of increases the skill 
	protected List<Integer> dependingSkillsModifier;

	protected List<Attribute> dependingAttributes;
	// the amount that the use of this skill of increases the attribute 
	protected List<Integer> dependingAttributesModifier;

	public Skill(String name, int experiencePointsModifierPerUse, List<Integer> experiencePointsRequiredPerLevel) {
		super(name, experiencePointsModifierPerUse, experiencePointsRequiredPerLevel);
		this.setDependingAttributes(new ArrayList<Attribute>());
		this.setDependingAttributesModifier(new ArrayList<Integer>());
		this.setDependingSkills(new ArrayList<Skill>());
		this.setDependingSkillsModifier(new ArrayList<Integer>());
	}

	public Skill(String name, List<Integer> experiencePointsRequiredPerLevel) {
		this(name, 1, experiencePointsRequiredPerLevel);
	}

	public Skill(String name, int experiencePointsModifierPerUse, List<Integer> experiencePointsRequiredPerLevel, List<Attribute> dependingAttributes, List<Integer> dependingAttributesModifier) {
		this(name, experiencePointsModifierPerUse, experiencePointsRequiredPerLevel);
		if (dependingAttributes.size() != dependingAttributesModifier.size()) {
			String message = "The number of attributes must be the same as the number of attribute modifiers (" + dependingAttributes.size() + " vs " + dependingAttributesModifier.size() + ")";
			throw new GameError(message);
		}
		this.getDependingAttributes().addAll(dependingAttributes);
		this.getDependingAttributesModifier().addAll(dependingAttributesModifier);
	}

	public boolean usedWith(int intensity) {
		if (super.usedWith(intensity)) {
			this.triggerUseOfDependingAttributes(intensity);
			this.triggerUseOfDependingSkills(intensity);
			return true;
		} else {
			return false;
		}

	}

	public boolean usedWithByDependent(int intensity) {
		if (super.usedWith(intensity)) {
			this.triggerUseOfDependingAttributes(intensity);
			return true;
		} else {
			return false;
		}
	}

	private void triggerUseOfDependingAttributes(int intensity) {
		for (int i = 0; i < this.getDependingAttributes().size(); i++) {
			Attribute attribute = this.getDependingAttributes().get(i);
			attribute.usedWith(intensity * this.getDependingAttributesModifier().get(i));
		}
	}

	private void triggerUseOfDependingSkills(int intensity) {
		for (int i = 0; i < this.getDependingSkills().size(); i++) {
			Skill skill = this.getDependingSkills().get(i);
			skill.usedWithByDependent(intensity * this.getDependingSkillsModifier().get(i));
		}
	}

	public Skill addDependingAttribute(Attribute attribute, Integer modifier) {
		this.getDependingAttributes().add(attribute);
		this.getDependingAttributesModifier().add(modifier);
		return this;
	}

	public Skill addDependingSkill(Skill skill, Integer modifier) {
		this.getDependingSkills().add(skill);
		this.getDependingSkillsModifier().add(modifier);
		return this;
	}

	protected List<Skill> getDependingSkills() {
		return this.dependingSkills;
	}

	protected void setDependingSkills(List<Skill> dependingSkills) {
		this.dependingSkills = dependingSkills;
	}

	protected List<Integer> getDependingSkillsModifier() {
		return this.dependingSkillsModifier;
	}

	protected void setDependingSkillsModifier(List<Integer> dependingSkillsModifier) {
		this.dependingSkillsModifier = dependingSkillsModifier;
	}

	public List<Attribute> getDependingAttributes() {
		return this.dependingAttributes;
	}

	protected void setDependingAttributes(List<Attribute> dependingAttributes) {
		this.dependingAttributes = dependingAttributes;
	}

	public List<Integer> getDependingAttributesModifier() {
		return this.dependingAttributesModifier;
	}

	protected void setDependingAttributesModifier(List<Integer> dependingAttributesModifier) {
		this.dependingAttributesModifier = dependingAttributesModifier;
	}
}
