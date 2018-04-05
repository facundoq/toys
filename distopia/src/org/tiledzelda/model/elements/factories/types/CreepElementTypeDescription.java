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

import org.tiledzelda.model.elements.movableElements.MovableElementCharacteristics;
import org.tiledzelda.model.elements.movableElements.creeps.CreepElementType;

/**
 * @author Facundo Manuel Quiroga Mar 15, 2009
 */
public class CreepElementTypeDescription {

	protected String name;
	MovableElementCharacteristics characteristics;

	public CreepElementTypeDescription(String name, int damage, int damageReduction, int staminaHitCost, int baseSpeed, int chanceToHit, int chanceToDodge, int maxHealth, int maxStamina, int healthRestorationRate, int staminaRestorationRate) {
		super();
		this.name = name;
		this.characteristics = new MovableElementCharacteristics(damage, damageReduction, chanceToHit, chanceToDodge, staminaHitCost, baseSpeed, maxHealth, maxStamina, healthRestorationRate, staminaRestorationRate);
	}

	public CreepElementType createCreepElementType() {
		return new CreepElementType(this.name, this.characteristics);
	}

	public String getName() {
		return this.name;
	}

	protected void setName(String name) {
		this.name = name;
	}
}
