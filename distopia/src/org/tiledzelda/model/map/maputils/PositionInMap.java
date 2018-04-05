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

import org.tiledzelda.model.map.TileMap;

/**
 * A tile Position in a certain Map, just x and y but now of map.
 * 
 * @author Facundo Manuel Quiroga
 */

public class PositionInMap {
	private Position position;
	private TileMap tileMap;

	public PositionInMap(TileMap tileMap, Position position) {
		this.setMap(tileMap);
		this.setPosition(position);
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public TileMap getMap() {
		return tileMap;
	}

	public void setMap(TileMap tileMap) {
		this.tileMap = tileMap;
	}

}
