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

package org.tiledzelda.main.resourcemanagement;

import java.awt.Image;
import java.util.HashMap;

import org.tiledzelda.exceptions.GameLoadingError;
import org.tiledzelda.model.entities.FloorCell;

/**
 * @author Facundo Manuel Quiroga Dec 26, 2008
 */
public class FloorCellFactory {

	protected HashMap<String, FloorCell> floorCells;

	public FloorCellFactory() {
		this.floorCells = new HashMap<String, FloorCell>();
		ImageManager imageManager = ImageManager.getInstance();

		Image earth = imageManager.getValue("earth");
		Image water = imageManager.getValue("water");
		Image grass = imageManager.getValue("grass");

		this.floorCells.put("water", new FloorCell(water, false, "water"));
		this.floorCells.put("grass", new FloorCell(grass, true, "grass"));
		this.floorCells.put("earth", new FloorCell(earth, true, "earth"));

	}

	public FloorCell getFloorCell(String name) {
		FloorCell floorCell = this.floorCells.get(name);
		if (floorCell == null) {
			throw new GameLoadingError("FloorCell " + name + " does not exist");
		}
		return floorCell;
	}

}
