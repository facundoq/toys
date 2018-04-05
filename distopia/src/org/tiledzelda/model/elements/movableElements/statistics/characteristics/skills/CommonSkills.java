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

import org.tiledzelda.model.elements.movableElements.statistics.characteristics.ExperienceKeeper;
import org.tiledzelda.model.elements.movableElements.statistics.characteristics.attributes.Attribute;

/**
 * @author Facundo Manuel Quiroga Jul 22, 2009
 */
public class CommonSkills {

	public static Skill newCombat() {
		return new Skill("Combat", ExperienceKeeper.commonExperiencePointsPerLevel());
	}

	public static Skill newWeapons(Skill combat, Attribute strength, Attribute dexterity) {
		Skill skill = new Skill("Weapons", ExperienceKeeper.commonExperiencePointsPerLevel());
		skill.addDependingSkill(combat, 1);
		skill.addDependingAttribute(strength, 2);
		skill.addDependingAttribute(dexterity, 1);
		return skill;
	}

	public static Skill newDodge(Skill defense, Attribute speed, Attribute dexterity, Attribute intelligence) {
		Skill skill = new Skill("Dodge", ExperienceKeeper.commonExperiencePointsPerLevel());
		skill.addDependingSkill(defense, 1);
		skill.addDependingAttribute(speed, 1);
		skill.addDependingAttribute(dexterity, 1);
		skill.addDependingAttribute(intelligence, 1);
		return skill;
	}

	public static Skill newDefense(Skill combat) {
		Skill skill = new Skill("Defense", ExperienceKeeper.commonExperiencePointsPerLevel());
		skill.addDependingSkill(combat, 1);
		return skill;
	}

	public static Skill newBlock(Skill defense, Attribute speed, Attribute dexterity) {
		Skill skill = new Skill("Block", ExperienceKeeper.commonExperiencePointsPerLevel());
		skill.addDependingSkill(defense, 1);
		skill.addDependingAttribute(speed, 1);
		skill.addDependingAttribute(dexterity, 1);
		return skill;
	}

}
