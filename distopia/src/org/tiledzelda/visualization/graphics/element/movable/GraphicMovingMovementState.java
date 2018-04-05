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

import org.tiledzelda.main.resourcemanagement.Configuration;
import org.tiledzelda.model.map.maputils.Direction;

/**
 * @author Facundo Manuel Quiroga Nov 20, 2008
 */
public class GraphicMovingMovementState extends GraphicMovementState {

	int x;
	int y;
	double speedX;
	double speedY;
	long time;
	Direction direction;

	protected GraphicMovingMovementState(MovableElementGraphic movableElementGraphic, MovableSprite movableSprite, Direction direction, int speed) {
		super(movableElementGraphic, movableSprite);
		this.setDirection(direction);
		int tileSize = Configuration.getInstance().getValueAsInt("blockSize");
		double spriteSpeed = (double) tileSize / (double) speed;
		// set destination to be in the center
		// set position opposite to where we are moving (so it'll look as if we didn't move at first
		this.setX(-1 * tileSize * direction.getHorizontalDirection());
		this.setY(-1 * tileSize * direction.getVerticalDirection());
		this.setSpeedX(spriteSpeed * direction.getHorizontalDirection());
		this.setSpeedY(spriteSpeed * direction.getVerticalDirection());
		time = 0;

	}

	@Override
	public void changeDirection(Direction direction) {
		throw new GraphicMovementLogicException("Cannot change direction when moving!");
	}

	@Override
	public int getXDifference() {
		return x;
	}

	@Override
	public int getYDifference() {
		return y;
	}

	@Override
	public boolean isIdle() {
		return false;
	}

	@Override
	public void move(Direction direction, int speed) {
		throw new GraphicMovementLogicException("Cannot move if already moving!");

	}

	@Override
	public void stoppedMoving() {
		this.getMovableElementGraphic().doStopMoving();
	}

	@Override
	public void update(long timeElapsed) {
		super.update(timeElapsed);
		int newX = (int) (this.getX() + (this.getSpeedX() * timeElapsed));
		int newY = (int) (this.getY() + (this.getSpeedY() * timeElapsed));
		//check if we have passed the 0 position
		time += timeElapsed;
		if (((newX * this.getDirection().getHorizontalDirection()) >= 0) && ((newY * this.getDirection().getVerticalDirection()) >= 0)) {
			this.setY(0);
			this.setX(0);
		} else {
			this.setY(newY);
			this.setX(newX);
		}
	}

	protected int getX() {
		return this.x;
	}

	protected void setX(int x) {
		this.x = x;
	}

	protected int getY() {
		return this.y;
	}

	protected void setY(int y) {
		this.y = y;
	}

	protected double getSpeedX() {
		return this.speedX;
	}

	protected void setSpeedX(double speedX) {
		this.speedX = speedX;
	}

	protected double getSpeedY() {
		return this.speedY;
	}

	protected void setSpeedY(double speedY) {
		this.speedY = speedY;
	}

	protected Direction getDirection() {
		return this.direction;
	}

	protected void setDirection(Direction direction) {
		this.direction = direction;
	}

}
