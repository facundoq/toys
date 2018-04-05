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

package org.tiledzelda.model.elements.factories.types;

import org.tiledzelda.input.ai.AI;
import org.tiledzelda.model.elements.movableElements.MovableElementCharacteristics;
import org.tiledzelda.model.elements.movableElements.creeps.Creep;
import org.tiledzelda.model.elements.movableElements.creeps.CreepElementType;
import org.tiledzelda.model.elements.movableElements.player.AttributeState;
import org.tiledzelda.model.elements.movableElements.player.HealthState;
import org.tiledzelda.model.elements.movableElements.statistics.Statistic;
import org.tiledzelda.visualization.graphics.element.movable.MovableElementGraphic;

/**
 * @author Facundo Manuel Quiroga Mar 10, 2009
 */
public class CreepDescription {

	protected CreepElementTypeDescription creepElementTypeDescription;
	protected CreepElementGraphicDescription creepElementGraphicDescription;
	protected CreepAIDescription creepAIDescription;

	public CreepDescription(CreepElementTypeDescription creepElementTypeDescription, CreepElementGraphicDescription creepElementGraphicDescription, CreepAIDescription description) {
		super();
		this.creepElementTypeDescription = creepElementTypeDescription;
		this.creepElementGraphicDescription = creepElementGraphicDescription;
		this.creepAIDescription = description;
	}

	/**
	 * @return a new creep from its description
	 */
	public Creep createCreep() {
		MovableElementGraphic movableElementGraphic = this.creepElementGraphicDescription.createMovableElementGraphic();
		CreepElementType creepElementType = this.creepElementTypeDescription.createCreepElementType();
		HealthState healthState = this.createHealth(creepElementType.getCharacteristics());
		AI ai = this.creepAIDescription.createCreepAI();
		return new Creep(creepElementType, movableElementGraphic, ai, healthState);
	}

	/**
	 * @param characteristics
	 * @return
	 */
	protected HealthState createHealth(MovableElementCharacteristics characteristics) {
		Statistic maxHealthStatistic = characteristics.getStatisticNamed("MaxHealth");
		Statistic maxStaminaStatistic = characteristics.getStatisticNamed("MaxStamina");
		Statistic healthRecoveryRate = characteristics.getStatisticNamed("HealthRecoveryRate");
		Statistic staminaRecoveryRate = characteristics.getStatisticNamed("StaminaRecoveryRate");
		AttributeState health = new AttributeState("health", (int) maxHealthStatistic.getValue(), maxHealthStatistic, healthRecoveryRate);
		AttributeState stamina = new AttributeState("stamina", (int) maxStaminaStatistic.getValue(), maxStaminaStatistic, staminaRecoveryRate);
		return new HealthState(health, stamina);
	}

	public CreepElementTypeDescription getCreepElementTypeDescription() {
		return this.creepElementTypeDescription;
	}

	protected void setCreepElementTypeDescription(CreepElementTypeDescription creepElementTypeDescription) {
		this.creepElementTypeDescription = creepElementTypeDescription;
	}

	public CreepElementGraphicDescription getCreepElementGraphicDescription() {
		return this.creepElementGraphicDescription;
	}

	protected void setCreepElementGraphicDescription(CreepElementGraphicDescription creepElementGraphicDescription) {
		this.creepElementGraphicDescription = creepElementGraphicDescription;
	}

	public CreepAIDescription getCreepAIDescription() {
		return this.creepAIDescription;
	}

	protected void setCreepAIDescription(CreepAIDescription creepAIDescription) {
		this.creepAIDescription = creepAIDescription;
	}

	public String toString() {
		return "CreepDescription for " + this.getCreepElementTypeDescription().getName();
	}
}
