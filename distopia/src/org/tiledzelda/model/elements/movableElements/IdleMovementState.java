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

import org.tiledzelda.exceptions.GameException;
import org.tiledzelda.model.map.maputils.Direction;
import org.tiledzelda.model.map.maputils.Position;

/**
 * State of a movable element when it is not moving. In this state, can start to move.
 * 
 * @author Facundo Manuel Quiroga 07/10/2008
 */

public class IdleMovementState extends MovementState {

	public IdleMovementState(MovableElement movableElement) {
		super(movableElement);
	}

	@Override
	public void move(Direction direction, int moveSpeed) {

		MovableElement movableElement = this.getMovableElement();
		if (!movableElement.facesDirection(direction)) { // not moving! just changing direction					       
			movableElement.changeDirection(direction);
			movableElement.setMovementState(new ChangingDirectionMovementState(movableElement, moveSpeed / 3));
		} else {
			Position currentPosition = movableElement.getPosition();
			Position newPosition = currentPosition.positionAfterMovingIn(direction);
			try {
				movableElement.getCurrentMap().moveElement(currentPosition, newPosition);
				movableElement.setPosition(newPosition);

				// calculate speed of movement
				int speed;
				//if we move diagonally
				if (direction.getHorizontalDirection() != 0 && direction.getVerticalDirection() != 0) { // 
					speed = movableElement.getDiagonalSpeed(moveSpeed);
				} else {
					speed = moveSpeed;
				}
				movableElement.setMovementState(new MovingMovementState(movableElement, speed));
				movableElement.getElementType().getCharacteristics().usedAttributeWithIntensityAndProbability("Speed", 1, 3);
				movableElement.getElementType().getCharacteristics().usedAttributeWithIntensityAndProbability("Endurance", 1, 3);

				movableElement.notifyObserversOfMove(direction, speed);
			} catch (GameException e) {
				// can't move, but it's ok anyway
			}
		}
	}

	@Override
	public void update(long timeElapsed) {
	}

	@Override
	public boolean isIdle() {
		return true;
	}

}
