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

import org.tiledzelda.exceptions.GameError;

/**
 * A cache of positions, so as not to waste too much memory because positions are ubiquitous. has a absoluteMaximum that indicates how much in each four directions it caches positions, regardless of
 * sign. So if size = 10, it'll cache positions from -10,-10 to 10,10, passing through -10,10 , 0,0 and 10,-10 This class is important for performance so, on purpose, it is written with as few method
 * calls as possible.
 * 
 * @author Facundo Manuel Quiroga Nov 19, 2008
 */
public class PositionCache {

	protected static PositionCache instance = null;

	public static PositionCache getInstance() {
		if (PositionCache.instance == null) {
			PositionCache.instance = new PositionCache(70);
		}
		return PositionCache.instance;
	}

	Position[][] positions;
	protected int absoluteMaximum;
	protected int negativeBorder;
	protected int positiveBorder;

	protected PositionCache(int absoluteMaximum) {
		if (absoluteMaximum <= 0) {
			throw new GameError("Position cache - absoluteMaximum cannot be negative or zero");
		}
		this.absoluteMaximum = absoluteMaximum;
		int arraySize = this.arraySize();
		this.positiveBorder = this.absoluteMaximum;
		this.negativeBorder = this.absoluteMaximum * -1;

		this.positions = new Position[arraySize][arraySize];
		for (int i = this.negativeBorder; i <= this.positiveBorder; i++) {
			for (int j = this.negativeBorder; j <= this.positiveBorder; j++) {
				this.setPositionAt(i, j, new Position(i, j));
			}
		}

	}

	protected int arraySize() {
		return (this.absoluteMaximum * 2) + 1;// add 1 to account for 0,0 / 0,x / x,0 positions
	}

	public boolean inRange(int x, int y) {
		boolean inVerticalRange = (x <= this.positiveBorder) && (x >= this.negativeBorder);
		boolean inHorizontalRange = (y <= this.positiveBorder) && (y >= this.negativeBorder);
		return inVerticalRange && inHorizontalRange;
	}

	protected void setPositionAt(int x, int y, Position position) {
		if (!this.inRange(x, y)) {
			throw new InvalidPositionParameterException("position " + x + "," + y + " is not in range");
		}
		int properX = x + this.absoluteMaximum;
		int properY = y + this.absoluteMaximum;
		this.positions[properX][properY] = position;
	}

	protected Position getPositionAt(int x, int y) {
		if (inRange(x, y)) {
			int properX = x + this.absoluteMaximum;
			int properY = y + this.absoluteMaximum;
			return this.positions[properX][properY];
		} else {
			return new Position(x, y);
		}
	}

}
