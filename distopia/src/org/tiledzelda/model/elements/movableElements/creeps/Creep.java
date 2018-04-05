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

package org.tiledzelda.model.elements.movableElements.creeps;

import org.tiledzelda.input.ai.AI;
import org.tiledzelda.model.elements.movableElements.MovableElement;
import org.tiledzelda.model.elements.movableElements.player.HealthState;
import org.tiledzelda.visualization.graphics.element.movable.MovableElementGraphic;

public class Creep extends MovableElement {
	protected CreepElementType elementType;
	private AI ai;

	public Creep(CreepElementType elementType, MovableElementGraphic graphic, AI ai, HealthState healthState) {
		super(graphic);
		this.setElementType(elementType);
		ai.setCreep(this);
		this.setAI(ai);
		this.setHealthState(healthState);
	}

	public AI getAI() {
		return ai;
	}

	public void setAI(AI ai) {
		this.ai = ai;
	}

	public void die() {
		super.die();
		this.getAI().die();
	}

	public CreepElementType getElementType() {
		return this.elementType;
	}

	protected void setElementType(CreepElementType elementType) {
		this.elementType = elementType;
	}

}
