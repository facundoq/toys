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

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterates over every position of the area contains in a left to right, up to down order
 * 
 * @author Facundo Manuel Quiroga 05/10/2008
 */
public class AreaIteratorLeftRightUpDown implements Iterator<Position> {

	protected Area area;
	protected Position currentPosition;

	public AreaIteratorLeftRightUpDown(Area area) {
		this.setArea(area);
		this.setCurrentPosition(area.getFrom());
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		return this.getArea().contains(this.getCurrentPosition());
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#next()
	 */
	public Position next() {
		if (!this.hasNext()) {
			throw new NoSuchElementException();
		}
		Position previousPosition = this.getCurrentPosition();
		Position newPosition;
		// if i'm at the end of the row, go to the next rot
		if (this.getArea().horizontalRange().getTo() == previousPosition.getX()) {
			newPosition = Position.newPosition(this.getArea().getFrom().getX(), previousPosition.getY() + 1);
		} else { // go to next column of the row
			newPosition = Position.newPosition(previousPosition.getX() + 1, previousPosition.getY());
		}
		this.setCurrentPosition(newPosition);
		return previousPosition;
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
		throw new UnsupportedOperationException();
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public Position getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(Position currentPosition) {
		this.currentPosition = currentPosition;
	}

}
