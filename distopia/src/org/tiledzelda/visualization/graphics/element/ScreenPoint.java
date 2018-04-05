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

package org.tiledzelda.visualization.graphics.element;

/**
 * @author Facundo Manuel Quiroga Nov 20, 2008
 */
public class ScreenPoint {
	public static ScreenPoint newScreenPoint(int x, int y) {
		return new ScreenPoint(x, y);
	}

	public int x;
	public int y;

	private ScreenPoint(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return this.x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return this.y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public ScreenPoint moveHorizontally(int newX) {
		return ScreenPoint.newScreenPoint(newX, this.y);
	}

	public ScreenPoint moveVertically(int newY) {
		return ScreenPoint.newScreenPoint(this.x, newY);
	}

	public ScreenPoint difference(ScreenPoint aScreenPoint) {
		int newX = this.x - aScreenPoint.x;
		int newY = this.y - aScreenPoint.y;
		return ScreenPoint.newScreenPoint(newX, newY);
	}

}
