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

package org.tiledzelda.model.elements.factories;

import org.tiledzelda.model.map.maputils.Direction;

/**
 * @author Facundo Manuel Quiroga Oct 22, 2008
 */
public abstract class MovableElementFactory {

	protected Direction N = Direction.newDirection("n");
	protected Direction S = Direction.newDirection("s");
	protected Direction E = Direction.newDirection("e");
	protected Direction W = Direction.newDirection("w");
	protected Direction NW = Direction.newDirection("nw");
	protected Direction NE = Direction.newDirection("ne");
	protected Direction SW = Direction.newDirection("sw");
	protected Direction SE = Direction.newDirection("se");

}
