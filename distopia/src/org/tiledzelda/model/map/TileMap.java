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
import org.tiledzelda.exceptions.InvalidPositionException;
import org.tiledzelda.exceptions.OcuppiedPositionException;
import org.tiledzelda.model.elements.Element;
import org.tiledzelda.model.entities.FloorCell;
import org.tiledzelda.model.entities.NullElement;
import org.tiledzelda.model.entities.NullFloorCell;
import org.tiledzelda.model.map.maputils.Area;
import org.tiledzelda.model.map.maputils.Position;

/**
 * A TileMap where the game objects are held and move; it has a certain size and two layers, one for floorTiles (ie grass, dirt, water), one for elements (trees, players, creeps, rocks)
 * 
 * @author Facundo Manuel Quiroga
 */

public class TileMap extends AbstractTileMap {

	protected int width;
	protected int height;

	protected Element[][] elementsLayer;
	protected FloorCell[][] floorLayer;

	public TileMap(int width, int height) {
		super();
		this.setWidth(width);
		this.setHeight(height);
		elementsLayer = new Element[width][height];
		floorLayer = new FloorCell[width][height];
		for (Position position : this.area()) {
			this.setElement(position, NullElement.getInstance());
			this.setFloor(position, NullFloorCell.getInstance());
		}
	}

	public boolean existsElementAt(Position position) {
		return !(this.elementAt(position).equals(NullElement.getInstance()));
	}

	/**
	 * @return true if the floor layer at this position is walkable and there are not any elements in the position.
	 */
	public boolean occupable(Position position) {
		return (!this.existsElementAt(position) && this.getFloor(position).isWalkable());
	}

	public void moveElement(Position currentPosition, Position newPosition) throws GameException {
		if (this.validPosition(newPosition) && this.occupable(newPosition)) {
			Element element = this.elementAt(currentPosition);
			this.clearElementAt(currentPosition);
			this.setElement(newPosition, element);
		} else {
			throw new GameException("Cant move to" + newPosition.toString());
		}
	}

	public void addElementAt(Element element, Position position) throws InvalidPositionException, OcuppiedPositionException {
		if (!this.validPosition(position)) {
			throw new InvalidPositionException("Position" + position + " is invalid");
		} else if (!this.occupable(position)) {
			throw new OcuppiedPositionException("Block at position " + position + " is occupied or unwalkable");
		}
		this.setElement(position, element);
		this.addElement(element);
	}

	protected void setElement(Position position, Element element) {
		this.elementsLayer[position.getX()][position.getY()] = element;
	}

	public Element removeElement(Element element) {
		this.clearElementAt(element.getPosition());
		return super.removeElement(element);
	}

	protected void clearElementAt(Position position) {
		this.setElement(position, NullElement.getInstance());
	}

	public Element elementAt(Position position) {
		return this.elementsLayer[position.getX()][position.getY()];
	}

	public Collection<Element> elementsIn(Area area) {
		Collection<Element> elements = new ArrayList<Element>();
		for (Element element : this.getElements()) {
			if (area.contains(element.getPosition())) {
				elements.add(element);
			}
		}
		return elements;
	}

	public void setFloor(Position position, FloorCell floorCell) {
		this.floorLayer[position.getX()][position.getY()] = floorCell;
	}

	public FloorCell getFloor(Position position) {
		return this.floorLayer[position.getX()][position.getY()];
	}

	public int getWidth() {
		return width;
	}

	protected void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	protected void setHeight(int height) {
		this.height = height;
	}

	public Area area() {
		return new Area(Position.newPosition(0, 0), Position.newPosition(this.getWidth() - 1, this.getHeight() - 1));
	}

	/**
	 * @param position that is to be tested as valid
	 * @return true if position is inside the bounds of the map
	 */
	public boolean validPosition(Position position) {
		return this.area().contains(position);
		/*int x = position.getX();
		int y = position.getY();
		return (x >= 0 && y >= 0 && x < this.getWidth() && y < this.getHeight());*/
	}
}
