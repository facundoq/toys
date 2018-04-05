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

package org.tiledzelda.main;

import java.util.ArrayList;
import java.util.Collection;

import org.tiledzelda.model.elements.Element;
import org.tiledzelda.model.entities.FloorCell;
import org.tiledzelda.model.map.TileMap;

public class GameEntities {

	private static GameEntities instance;

	public static GameEntities getInstance() {
		if (GameEntities.instance == null) {
			GameEntities.instance = new GameEntities();
		}
		return GameEntities.instance;
	}

	private Collection<TileMap> tileMaps;
	private Collection<Element> elements;
	private Collection<FloorCell> floorCells;

	private GameEntities() {
		this.setMaps(new ArrayList<TileMap>());
		this.setElements(new ArrayList<Element>());
		this.setFloorCells(new ArrayList<FloorCell>());
	}

	public TileMap addMap(TileMap tileMap) {
		this.getMaps().add(tileMap);
		return tileMap;
	}

	public Element addElement(Element element) {
		this.getElements().add(element);
		return element;
	}

	public FloorCell addFloorCell(FloorCell floorCell) {
		this.getFloorCells().add(floorCell);
		return floorCell;
	}

	public void updateElements(long elapsedTime) {
		for (Element element : this.getElements()) {
			element.update(elapsedTime);
		}
	}

	/*
	public void updateAll(){
		this.updateMaps();
		this.updateElements();
		this.updateFloorCells();
	}*/

	public Collection<TileMap> getMaps() {
		return tileMaps;
	}

	private void setMaps(Collection<TileMap> tileMaps) {
		this.tileMaps = tileMaps;
	}

	public Collection<Element> getElements() {
		return elements;
	}

	private void setElements(Collection<Element> elements) {
		this.elements = elements;
	}

	public Collection<FloorCell> getFloorCells() {
		return floorCells;
	}

	private void setFloorCells(Collection<FloorCell> floorCells) {
		this.floorCells = floorCells;
	}

}
