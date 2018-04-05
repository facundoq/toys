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

package org.tiledzelda.model.elements.movableElements;

import java.util.ArrayList;

import org.tiledzelda.model.elements.movableElements.statistics.Statistic;
import org.tiledzelda.model.elements.movableElements.statistics.characteristics.Characteristics;
import org.tiledzelda.model.elements.movableElements.statistics.characteristics.attributes.Attribute;
import org.tiledzelda.model.elements.movableElements.statistics.characteristics.attributes.CommonAttributes;
import org.tiledzelda.model.elements.movableElements.statistics.characteristics.skills.CommonSkills;
import org.tiledzelda.model.elements.movableElements.statistics.characteristics.skills.Skill;
import org.tiledzelda.model.elements.movableElements.statistics.derived.ChanceToDodgeStatistic;
import org.tiledzelda.model.elements.movableElements.statistics.derived.ChanceToHitStatistic;
import org.tiledzelda.model.elements.movableElements.statistics.derived.DamageReductionStatistic;
import org.tiledzelda.model.elements.movableElements.statistics.derived.DamageStatistic;
import org.tiledzelda.model.elements.movableElements.statistics.derived.HealthRecoveryRateStatistic;
import org.tiledzelda.model.elements.movableElements.statistics.derived.MaxHealthStatistic;
import org.tiledzelda.model.elements.movableElements.statistics.derived.MaxStaminaStatistic;
import org.tiledzelda.model.elements.movableElements.statistics.derived.MovementSpeedStatistic;
import org.tiledzelda.model.elements.movableElements.statistics.derived.StaminaCostPerHitStatistic;
import org.tiledzelda.model.elements.movableElements.statistics.derived.StaminaRecoveryRateStatistic;

/**
 * @author Facundo Manuel Quiroga Jul 22, 2009
 */
public class MovableElementCharacteristics extends Characteristics {
	protected ArrayList<Attribute> attributes;
	protected ArrayList<Skill> skills;
	protected ArrayList<Statistic> statistics;

	public MovableElementCharacteristics(float baseDamage, float baseDamageReduction, float baseChanceToHit, float baseChanceToDodge, float staminaHitCost, float baseSpeed, float baseHealth, float baseStamina, float baseHealthRestorationRate, float baseStaminaRestorationRate) {

		this.setAttributes(new ArrayList<Attribute>(CommonAttributes.getAll()));
		Attribute constitution = this.getAttributeNamed("Constitution");
		Attribute speed = this.getAttributeNamed("Speed");
		Attribute dexterity = this.getAttributeNamed("Dexterity");
		Attribute concentration = this.getAttributeNamed("Concentration");
		Attribute endurance = this.getAttributeNamed("Endurance");
		Attribute strength = this.getAttributeNamed("Strength");
		Attribute intelligence = this.getAttributeNamed("Intelligence");

		ArrayList<Skill> skills = new ArrayList<Skill>();
		Skill combat = CommonSkills.newCombat();
		Skill defense = CommonSkills.newDefense(combat);
		Skill weapons = CommonSkills.newWeapons(combat, strength, dexterity);
		Skill dodge = CommonSkills.newDodge(defense, speed, dexterity, intelligence);
		skills.add(combat);
		skills.add(defense);
		skills.add(weapons);
		skills.add(dodge);
		this.setSkills(skills);

		ArrayList<Statistic> statistics = new ArrayList<Statistic>();
		statistics.add(DamageStatistic.newDamage(baseDamage, strength, weapons));
		statistics.add(DamageReductionStatistic.newDamageReduction(baseDamageReduction, constitution));
		statistics.add(ChanceToHitStatistic.newChanceToHit(baseChanceToHit, speed, dexterity, weapons));
		statistics.add(ChanceToDodgeStatistic.newChanceToDodge(baseChanceToDodge, speed, dexterity, concentration));
		statistics.add(MovementSpeedStatistic.newMovementSpeed(baseSpeed, speed));
		statistics.add(StaminaCostPerHitStatistic.newStaminaCostPerHit(staminaHitCost, endurance, dexterity, weapons));

		statistics.add(MaxHealthStatistic.newMaxHealth(baseHealth, constitution, strength));
		statistics.add(MaxStaminaStatistic.newMaxStamina(baseStamina, constitution, endurance));
		statistics.add(HealthRecoveryRateStatistic.newHealthRecoveryRate(baseHealthRestorationRate, constitution));
		statistics.add(StaminaRecoveryRateStatistic.newStaminaRecoveryRate(baseStaminaRestorationRate, endurance));

		this.setStatistics(statistics);
	}

	public ArrayList<Attribute> getAttributes() {
		return this.attributes;
	}

	protected void setAttributes(ArrayList<Attribute> attributes) {
		this.attributes = attributes;
	}

	public ArrayList<Skill> getSkills() {
		return this.skills;
	}

	protected void setSkills(ArrayList<Skill> skills) {
		this.skills = skills;
	}

	public ArrayList<Statistic> getStatistics() {
		return this.statistics;
	}

	protected void setStatistics(ArrayList<Statistic> statistics) {
		this.statistics = statistics;
	}

}
