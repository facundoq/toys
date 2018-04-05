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

import org.tiledzelda.model.elements.movableElements.player.EntityState;
import org.tiledzelda.model.map.maputils.Direction;

/**
 * @author Facundo Manuel Quiroga 07/10/2008
 */
public abstract class MovementState extends EntityState {
	protected MovableElement movableElement;

	public MovementState(MovableElement movableElement) {
		this.setMovableElement(movableElement);
	}

	public abstract void update(long timeElapsed);

	public abstract void move(Direction direction, int speed);

	public MovableElement getMovableElement() {
		return this.movableElement;
	}

	public void setMovableElement(MovableElement movableElement) {
		this.movableElement = movableElement;
	}

	public abstract boolean isIdle();

}
