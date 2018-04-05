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

/**
 * A tile Position, just x and y, could be of any map.
 * 
 * @author Facundo Manuel Quiroga
 */

public class Position {

	private int x;
	private int y;
	private static PositionCache positionCache = PositionCache.getInstance();

	public static Position newPosition(int x, int y) {
		return Position.positionCache.getPositionAt(x, y);
		//return new Position(x, y);
	}

	protected Position(int x, int y) {
		this.setX(x);
		this.setY(y);
	}

	public boolean equals(Position position) {
		return (this.getX() == position.getX() && this.getY() == position.getY());
	}

	/**
	 * Returns a new Position considering a move in a Direction from current Position
	 */
	public Position positionAfterMovingIn(Direction direction) {
		int newX = this.getX() + direction.getHorizontalDirection();
		int newY = this.getY() + direction.getVerticalDirection();
		return Position.newPosition(newX, newY);
	}

	/**
	 * Position after moving a distance
	 */
	public Position positionAt(Distance distance) {
		int newX = this.getX() + distance.getHorizontalDistance();
		int newY = this.getY() + distance.getVerticalDistance();
		return Position.newPosition(newX, newY);
	}

	/**
	 * Returns true if positions are inmediate neigbours (can be reached in one movement). Note: returns false if positions are equal
	 */
	public boolean isAdjacentTo(Position position) {
		int differenceX = this.getX() - position.getX();
		int differenceY = this.getY() - position.getY();
		boolean horizontalAdjacency = (differenceX >= -1 && differenceX <= 1);
		boolean verticalAdjacency = (differenceY >= -1 && differenceY <= 1);
		return (horizontalAdjacency && verticalAdjacency && !this.equals(position));
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public String toString() {
		return ("x=" + new Integer(this.getX()).toString() + " y=" + new Integer(this.getY()).toString());
	}

	public Distance distanceTo(Position position) {
		return new Distance(this, position);
	}

	private void setX(int x) {
		this.x = x;
	}

	private void setY(int y) {
		this.y = y;
	}

	/*
	 * 
	 */
	public Direction directionToGoTo(Position position) {
		if (position.equals(this)) {
			throw new InvalidPositionParameterException("There's no possible direction to go from a position to itself");
		}
		int horizontalDirection = 0;
		int verticalDirection = 0;
		if (this.getX() < position.getX()) {
			horizontalDirection = +1;
		} else if (this.getX() > position.getX()) {
			horizontalDirection = -1;
		}

		if (this.getY() < position.getY()) {
			verticalDirection = +1;
		} else if (this.getY() > position.getY()) {
			verticalDirection = -1;
		}

		return Direction.newDirection(horizontalDirection, verticalDirection);
	}

}
