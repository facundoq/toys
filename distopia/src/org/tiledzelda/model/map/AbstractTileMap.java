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

import java.util.ArrayList;
import java.util.Collection;

import org.tiledzelda.exceptions.GameException;
import org.tiledzelda.model.elements.Element;
import org.tiledzelda.model.entities.GameEntity;
import org.tiledzelda.model.map.maputils.Area;
import org.tiledzelda.model.map.maputils.Position;

/**
 * @author Facundo Manuel Quiroga Nov 17, 2008
 */
public abstract class AbstractTileMap extends GameEntity {
	private Collection<Element> elements;

	public AbstractTileMap() {
		super();
		this.setElements(new ArrayList<Element>());
	}

	public Collection<Element> getElements() {
		return elements;
	}

	protected void setElements(Collection<Element> elements) {
		this.elements = elements;
	}

	protected void addElement(Element element) {
		this.getElements().add(element);
	}

	public Element removeElement(Element element) {
		this.getElements().remove(element);
		return element;
	}

	public boolean hasElement(Element element) {
		return this.getElements().contains(element);
	}

	public abstract void addElementAt(Element element, Position position) throws GameException;

	public abstract boolean existsElementAt(Position position);

	public abstract boolean occupable(Position position);

	public abstract void moveElement(Position currentPosition, Position newPosition) throws GameException;

	public abstract Element elementAt(Position position);

	public abstract Collection<Element> elementsIn(Area area);

	/**
	 * @param targetPosition
	 * @return
	 */
	public abstract boolean validPosition(Position targetPosition);

}
