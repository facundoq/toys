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

/**
 * * A cardinal DirectionCache /////////////////////////// // -1,-1 // 0,-1 // 1,-1 // /////////////////////////// /////////////////////////// // -1,0 // inv // 1,0 // ///////////////////////////
 * /////////////////////////// // -1, 1 // 0,1 // 1,1 // /////////////////////////// /////////////////////////// // 0,0 // 1,0 // 2,0 // /////////////////////////// /////////////////////////// // 0,1
 * // inv // 2,1 // /////////////////////////// /////////////////////////// // -0, 2 // 1,2 // 2,2 // ///////////////////////////
 * 
 * @author Facundo Manuel Quiroga 10/10/2008
 */
public class DirectionCache {

	protected Direction[][] directions;
	protected ArrayList<Direction> directionsList;

	static protected DirectionCache instance;

	public DirectionCache() {
		directionsList = new ArrayList<Direction>(8);
		directions = new Direction[3][3];
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if (!((i == 0) && (j == 0))) {
					Direction direction = new Direction(i, j);
					this.setDirectionAt(i, j, direction);
					this.directionsList.add(direction);
				}
			}
		}
	}

	public static DirectionCache getInstance() {
		if (DirectionCache.instance == null) {
			DirectionCache.instance = new DirectionCache();
		}
		return DirectionCache.instance;
	}

	protected void setDirectionAt(int horizontalDirection, int verticalDirection, Direction direction) {
		this.getDirections()[horizontalDirection + 1][verticalDirection + 1] = direction;
	}

	public Direction getDirectionAt(int horizontalDirection, int verticalDirection) {
		return this.getDirections()[horizontalDirection + 1][verticalDirection + 1];
	}

	public Direction getDirectionAt(String direction) {
		int horizontalDirection = 0;
		int verticalDirection = 0;

		if (direction == "n") {
			verticalDirection = -1;
		} else if (direction == "nw") {
			verticalDirection = -1;
			horizontalDirection = -1;
		} else if (direction == "ne") {
			verticalDirection = -1;
			horizontalDirection = 1;
		} else if (direction == "s") {
			verticalDirection = 1;
		} else if (direction == "sw") {
			verticalDirection = 1;
			horizontalDirection = -1;
		} else if (direction == "se") {
			verticalDirection = 1;
			horizontalDirection = 1;
		} else if (direction == "w") {
			horizontalDirection = -1;
		} else if (direction == "e") {
			horizontalDirection = 1;
		} else {
			throw new InvalidDirectionValuesException("Nombre de dirección incorrecto " + direction);
		}
		return this.getDirectionAt(horizontalDirection, verticalDirection);
	}

	protected Direction[][] getDirections() {
		return directions;
	}

	protected void setDirections(Direction[][] directions) {
		this.directions = directions;
	}

	/**
	 * @return
	 */
	public ArrayList<Direction> allDirections() {
		return this.directionsList;
	}

}
