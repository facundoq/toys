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

import org.tiledzelda.model.elements.BaseElementType;

/** 
 * @author Facundo Manuel Quiroga	
 * Mar 10, 2009
 */
/**
 * @author Facundo Manuel Quiroga Jul 22, 2009
 */
public class MovableElementType extends BaseElementType {

	protected MovableElementCharacteristics characteristics;

	/**
	 * milliseconds needed to move a vertical or horizontal block's distance
	 */

	public MovableElementType(String name, MovableElementCharacteristics movableElementCharacteristics) {
		super(name);
		this.setCharacteristics(movableElementCharacteristics);
	}

	public int getDamage() {
		return (int) this.getCharacteristics().getStatisticNamed("Damage").getValue();
	}

	public int getStaminaHitCost() {
		return (int) this.getCharacteristics().getStatisticNamed("StaminaCostPerHit").getValue();
	}

	public int getBaseSpeed() {
		return (int) this.getCharacteristics().getStatisticNamed("MovementSpeed").getValue();
	}

	public int getDamageReduction() {
		return (int) this.getCharacteristics().getStatisticNamed("DamageReduction").getValue();
	}

	public int getChanceToHit() {
		return (int) this.getCharacteristics().getStatisticNamed("ChanceToHit").getValue();
	}

	public int getChanceToDodge() {
		return (int) this.getCharacteristics().getStatisticNamed("ChanceToDodge").getValue();
	}

	public MovableElementCharacteristics getCharacteristics() {
		return this.characteristics;
	}

	protected void setCharacteristics(MovableElementCharacteristics characteristics) {
		this.characteristics = characteristics;
	}

	protected int getCurrentLevelOfAttribute(String attribute) {
		return this.getCharacteristics().getAttributeNamed(attribute).getCurrentLevel();
	}

	protected int getCurrentLevelOfSkill(String skill) {
		return this.getCharacteristics().getSkillNamed(skill).getCurrentLevel();
	}

}
