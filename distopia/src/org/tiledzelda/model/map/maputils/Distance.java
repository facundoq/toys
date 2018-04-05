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
 * Represents a relative distance in a map (not between any two Positions, its just a distance)
 * 
 * @author Facundo Manuel Quiroga 02/10/2008
 */
public class Distance {
	private int verticalDistance, horizontalDistance;

	public Distance(int x, int y) {
		this.setHorizontalDistance(x);
		this.setVerticalDistance(y);
	}

	public Distance(Position from, Position to) {
		this(to.getX() - from.getX(), to.getY() - from.getY());
	}

	public String toString() {
		return "Horizontal distance: " + this.getHorizontalDistance() + " VerticalDistance" + this.getVerticalDistance();
	}

	public int getVerticalDistance() {
		return verticalDistance;
	}

	public void setVerticalDistance(int verticalDistance) {
		this.verticalDistance = verticalDistance;
	}

	public int getHorizontalDistance() {
		return horizontalDistance;
	}

	public void setHorizontalDistance(int horizontalDistance) {
		this.horizontalDistance = horizontalDistance;
	}

	public double norm() {
		return Math.sqrt(horizontalDistance^2+verticalDistance^2);
	}

}
