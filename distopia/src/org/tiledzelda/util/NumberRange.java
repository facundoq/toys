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
package org.tiledzelda.util;

/**
 * A range between numbers. Includes from and to as part of the range.
 */
public class NumberRange {
	protected int from;
	protected int to;

	public NumberRange(int from, int to) {
		this.setFrom(from);
		this.setTo(to);
	}

	public boolean contains(int number) {
		return ((number >= this.from) && (number <= this.to));
	}

	public boolean properlyContains(int number) {
		return ((number > this.from) && (number < this.to));
	}

	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public int getTo() {
		return to;
	}

	public void setTo(int to) {
		this.to = to;
	}

}
