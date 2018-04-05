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

package org.tiledzelda.visualization.graphics.element.movable;

import org.tiledzelda.model.map.maputils.Direction;

/**
 * @author Facundo Manuel Quiroga Nov 20, 2008
 */
public class GraphicIdleMovementState extends GraphicMovementState {

	/**
	 * @param movableElementGraphic
	 * @param movableSprite
	 */
	protected GraphicIdleMovementState(MovableElementGraphic movableElementGraphic, MovableSprite movableSprite) {
		super(movableElementGraphic, movableSprite);
	}

	@Override
	public void changeDirection(Direction direction) {
		this.getMovableElementGraphic().doChangeDirection(direction);
	}

	@Override
	public int getXDifference() {
		return 0;
	}

	@Override
	public int getYDifference() {
		return 0;
	}

	@Override
	public boolean isIdle() {
		return true;
	}

	@Override
	public void move(Direction direction, int speed) {
		this.getMovableElementGraphic().doMove(direction, speed);
	}

	@Override
	public void stoppedMoving() {
		throw new GraphicMovementLogicException("Cannot stop moving if it was idle!");

	}

}
