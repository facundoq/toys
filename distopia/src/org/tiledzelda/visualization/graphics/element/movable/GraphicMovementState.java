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
public abstract class GraphicMovementState {
	protected MovableElementGraphic movableElementGraphic;
	protected MovableSprite movableSprite;

	/**
	 * @param movableElementGraphic to which this State belongs
	 */
	protected GraphicMovementState(MovableElementGraphic movableElementGraphic, MovableSprite movableSprite) {
		this.setMovableElementGraphic(movableElementGraphic);
		this.setMovableSprite(movableSprite);
	}

	public abstract void move(Direction direction, int speed);

	public abstract void changeDirection(Direction direction);

	public abstract void stoppedMoving();

	public abstract boolean isIdle();

	public void update(long timeElapsed) {
		this.getMovableSprite().update(timeElapsed);
	}

	public abstract int getXDifference();

	public abstract int getYDifference();

	protected MovableElementGraphic getMovableElementGraphic() {
		return this.movableElementGraphic;
	}

	protected void setMovableElementGraphic(MovableElementGraphic movableElementGraphic) {
		this.movableElementGraphic = movableElementGraphic;
	}

	protected void setMovableSprite(MovableSprite movableSprite) {
		this.movableSprite = movableSprite;
	}

	protected MovableSprite getMovableSprite() {
		return this.movableSprite;
	}

}
