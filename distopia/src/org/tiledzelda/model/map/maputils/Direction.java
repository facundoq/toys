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

package org.tiledzelda.model.map.maputils;

import java.util.ArrayList;
import java.util.List;

import org.tiledzelda.exceptions.GameError;

/**
 * <pre>
 * A cardinal Direction (N, S, E, W, NW, NE, etc ) 
 *  // -1,-1 // 0,-1 // 1,-1 // 
 *  // -1,0  //invali // 1,0 // 
 *  // -1, 1 // 0,1   // 1,1 //
 * </pre>
 * 
 * @author Facundo Manuel Quiroga
 */
public class Direction {

	public static Direction newDirection(int horizontalDirection, int verticalDirection) {
		return DirectionCache.getInstance().getDirectionAt(horizontalDirection, verticalDirection);
	}

	public static Direction newDirection(String direction) {
		return DirectionCache.getInstance().getDirectionAt(direction);
	}

	private int horizontalDirection;

	private int verticalDirection;

	protected Direction(int horizontalDirection, int verticalDirection) throws InvalidDirectionValuesException {
		this.setHorizontalDirection(horizontalDirection);
		this.setVerticalDirection(verticalDirection);
		if (horizontalDirection < -1 || horizontalDirection > 1 || verticalDirection < -1 || verticalDirection > 1 || (verticalDirection == 0 && horizontalDirection == 0)) {
			throw new InvalidDirectionValuesException("Código de dirección incorrecto h: " + horizontalDirection + " v: " + verticalDirection);
		}
	}

	public boolean equals(Direction direction) {
		boolean equals;
		equals = this.getDirection().equals(direction.getDirection());
		return equals;
	}

	public int getHorizontalDirection() {
		return horizontalDirection;
	}

	public int getVerticalDirection() {
		return verticalDirection;
	}

	private String getDirection() {
		String direction = "";
		int horizontalDirection = this.getHorizontalDirection();
		int verticalDirection = this.getVerticalDirection();

		if (verticalDirection == 1) {
			direction = "s";
		} else if (verticalDirection == -1) {
			direction = "n";
		}

		if (horizontalDirection == 1) {
			direction += "e";
		} else if (horizontalDirection == -1) {
			direction += "w";
		}

		return direction;
	}

	public String toString() {
		return this.getDirection();
	}

	private void setHorizontalDirection(int horizontalDirection) {
		this.horizontalDirection = horizontalDirection;

	}

	private void setVerticalDirection(int verticalDirection) {
		this.verticalDirection = verticalDirection;
	}

	public Direction opposite() {
		return Direction.newDirection(this.getHorizontalDirection() * -1, this.getVerticalDirection() * -1);
	}

	/**
	 * @return a list of the five opposite directions in order of "farness" from this direction
	 */
	public List<Direction> oppositeDirections() {
		Direction oppositeDirection = this.opposite();
		List<Direction> possibleDirections = new ArrayList<Direction>();
		possibleDirections.add(oppositeDirection);
		possibleDirections.add(oppositeDirection.nextClockwise());
		possibleDirections.add(oppositeDirection.previousClockwise());
		possibleDirections.add(oppositeDirection.nextClockwise().nextClockwise());
		possibleDirections.add(oppositeDirection.previousClockwise().previousClockwise());
		return possibleDirections;
	}

	public Direction nextClockwise() {
		String direction = this.toString();
		Direction nextDirection = null;
		if (direction.equals("n")) {
			nextDirection = Direction.newDirection("ne");
		} else if (direction.equals("ne")) {
			nextDirection = Direction.newDirection("e");
		} else if (direction.equals("e")) {
			nextDirection = Direction.newDirection("se");
		} else if (direction.equals("se")) {
			nextDirection = Direction.newDirection("s");
		} else if (direction.equals("s")) {
			nextDirection = Direction.newDirection("sw");
		} else if (direction.equals("sw")) {
			nextDirection = Direction.newDirection("w");
		} else if (direction.equals("w")) {
			nextDirection = Direction.newDirection("nw");
		} else if (direction.equals("nw")) {
			nextDirection = Direction.newDirection("n");
		} else {
			throw new GameError("Check if conditions, " + direction + " not valid " + (nextDirection == null));
		}
		return nextDirection;
	}

	public Direction previousClockwise() {
		String direction = this.toString();
		Direction nextDirection;
		if (direction.equals("n")) {
			nextDirection = Direction.newDirection("nw");
		} else if (direction.equals("nw")) {
			nextDirection = Direction.newDirection("w");
		} else if (direction.equals("w")) {
			nextDirection = Direction.newDirection("sw");
		} else if (direction.equals("sw")) {
			nextDirection = Direction.newDirection("s");
		} else if (direction.equals("s")) {
			nextDirection = Direction.newDirection("se");
		} else if (direction.equals("se")) {
			nextDirection = Direction.newDirection("e");
		} else if (direction.equals("e")) {
			nextDirection = Direction.newDirection("ne");
		} else if (direction.equals("ne")) {
			nextDirection = Direction.newDirection("n");
		} else {
			throw new GameError("Check if conditions, " + direction + " not valid");
		}
		return nextDirection;
	}

	/**
	 * @return
	 */
	public static ArrayList<Direction> allDirections() {
		return DirectionCache.getInstance().allDirections();
	}
}
