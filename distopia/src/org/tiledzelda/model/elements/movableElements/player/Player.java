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

package org.tiledzelda.model.elements.movableElements.player;

import org.tiledzelda.model.elements.movableElements.MovableElement;
import org.tiledzelda.model.map.maputils.Position;
import org.tiledzelda.visualization.graphics.element.movable.MovableElementGraphic;

public class Player extends MovableElement {

	protected PlayerElementType elementType;
	protected PlayerObservations playerObservations;

	public Player(PlayerElementType elementType, MovableElementGraphic graphic, HealthState healthState) {
		super(graphic);
		this.setElementType(elementType);
		this.setHealthState(healthState);

		this.setPlayerObservations(new PlayerObservations(this));
	}

	public PlayerElementType getElementType() {
		return this.elementType;
	}

	protected void setElementType(PlayerElementType elementType) {
		this.elementType = elementType;
	}

	/**
	 * @param position
	 */
	public void lookAt(Position position) {
		this.getPlayerObservations().lookAt(position);
	}

	protected PlayerObservations getPlayerObservations() {
		return this.playerObservations;
	}

	protected void setPlayerObservations(PlayerObservations playerObservations) {
		this.playerObservations = playerObservations;
	}

}
