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
import java.util.Collection;
import java.util.Iterator;

import org.tiledzelda.util.NumberRange;

/**
 * An area of a map, think of it as a rectangle with from as the top-left position and to as the bottom-right position.
 * 
 * @author Facundo Manuel Quiroga 04/10/2008
 */

public class Area implements Iterable<Position> {
	protected Position from;
	protected Position to;
	protected NumberRange horizontalRange;
	protected NumberRange verticalRange;

	/**
	 * @param center of the square area
	 * @param radius
	 * @throws IllegalArgumentException
	 */
	public static Area newAreaAround(Position center, int radius) throws IllegalArgumentException {
		if (radius < 0) {
			throw new IllegalArgumentException("Radius should be >= 0");
		}
		Position from = Position.newPosition(center.getX() - radius, center.getY() - radius);
		Position to = Position.newPosition(center.getX() + radius, center.getY() + radius);
		return new Area(from, to);
	}

	public Area(Position from, Position to) {
		this.setFrom(from);
		this.setTo(to);
		this.updateRanges();
	}

	protected void updateRanges() {
		this.setVerticalRange(new NumberRange(this.getFrom().getY(), this.getTo().getY()));
		this.setHorizontalRange(new NumberRange(this.getFrom().getX(), this.getTo().getX()));
	}

	/**
	 * @return NumberRange starting from lowest Y and ending with highest Y
	 */
	protected NumberRange verticalRange() {
		return this.verticalRange;
	}

	/**
	 * @return NumberRange starting from lowest X and ending with highest X
	 */
	protected NumberRange horizontalRange() {
		return this.horizontalRange;
	}

	/**
	 * @param position that is to be checked
	 * @return if position is contained in the Area (in the rectangle the Area forms)
	 */
	public boolean contains(Position position) {
		boolean verticallyIncluded = this.verticalRange.contains(position.getY());
		boolean horizontallyIncluded = this.horizontalRange.contains(position.getX());
		return verticallyIncluded && horizontallyIncluded;
	}

	public boolean properlyContains(Position position) {
		boolean properlyVerticallyIncluded = this.verticalRange.properlyContains(position.getY());
		boolean properlyHorizontallyIncluded = this.horizontalRange.properlyContains(position.getX());
		return properlyVerticallyIncluded && properlyHorizontallyIncluded;
	}

	public Position getFrom() {
		return from;
	}

	protected void setFrom(Position from) {
		this.from = from;
	}

	public Position getTo() {
		return to;
	}

	protected void setTo(Position to) {
		this.to = to;
	}

	/**
	 * @return an iterator that iterates over every position this area contains in a left to right, up to down order.
	 */
	public Iterator<Position> iteratorLeftRightUpDown() {
		return new AreaIteratorLeftRightUpDown(this);
	}

	public Iterator<Position> iterator() {
		return this.iteratorLeftRightUpDown();
	}

	/**
	 * @return a collection of all the positions in the area
	 */
	public Collection<Position> getPositions() {
		Collection<Position> positions = new ArrayList<Position>();
		for (Position position : this) {
			positions.add(position);
		}
		return positions;
	}

	public String toString() {
		return "Area from: " + this.getFrom().toString() + " to: " + this.getTo().toString();
	}

	protected void setHorizontalRange(NumberRange horizontalRange) {
		this.horizontalRange = horizontalRange;
	}

	protected void setVerticalRange(NumberRange verticalRange) {
		this.verticalRange = verticalRange;
	}

}
