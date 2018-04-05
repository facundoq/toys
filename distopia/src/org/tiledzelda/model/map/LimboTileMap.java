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

package org.tiledzelda.model.map;

import java.util.Collection;

import org.tiledzelda.exceptions.GameException;
import org.tiledzelda.exceptions.UnsupportedMapOperationException;
import org.tiledzelda.model.elements.Element;
import org.tiledzelda.model.map.maputils.Area;
import org.tiledzelda.model.map.maputils.Position;

/**
 * @author Facundo Manuel Quiroga Nov 17, 2008
 */
public class LimboTileMap extends AbstractTileMap {

	public LimboTileMap() {
		super();
	}

	/* (non-Javadoc)
	 * @see map.AbstractTileMap#addElementAt(elements.Element, map.Position)
	 */
	@Override
	public void addElementAt(Element element, Position position) throws GameException {
		this.addElement(element);
	}

	/* (non-Javadoc)
	 * @see map.AbstractTileMap#elementAt(map.Position)
	 */
	@Override
	public Element elementAt(Position position) {
		throw new UnsupportedMapOperationException("");
	}

	/* (non-Javadoc)
	 * @see map.AbstractTileMap#elementsIn(map.Area)
	 */
	@Override
	public Collection<Element> elementsIn(Area area) {
		throw new UnsupportedMapOperationException("");
	}

	/* (non-Javadoc)
	 * @see map.AbstractTileMap#existsElementAt(map.Position)
	 */
	@Override
	public boolean existsElementAt(Position position) {
		throw new UnsupportedMapOperationException("");
	}

	/* (non-Javadoc)
	 * @see map.AbstractTileMap#moveElement(map.Position, map.Position)
	 */
	@Override
	public void moveElement(Position currentPosition, Position newPosition) throws GameException {
		throw new UnsupportedMapOperationException("");
	}

	/* (non-Javadoc)
	 * @see map.AbstractTileMap#occupable(map.Position)
	 */
	@Override
	public boolean occupable(Position position) {
		throw new UnsupportedMapOperationException("");
	}

	/* (non-Javadoc)
	 * @see map.AbstractTileMap#validPosition(map.Position)
	 */
	@Override
	public boolean validPosition(Position targetPosition) {
		throw new UnsupportedMapOperationException("");
	}

}
