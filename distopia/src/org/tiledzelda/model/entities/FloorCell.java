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

package org.tiledzelda.model.entities;

import java.awt.Graphics2D;
import java.awt.Image;

public class FloorCell extends GameEntity {

	private Image background;
	private boolean walkable;
	private String name;

	/**
	 * DO NOT USE THIS CONSTRUCTOR! It is only meant for NullFloorCell subclass
	 * 
	 * @see NullFloorCell
	 */
	protected FloorCell() {

	}

	public FloorCell(Image background, boolean walkable, String name) {
		setBackground(background);
		setWalkable(walkable);
		setName(name);
	}

	public Image getBackground() {
		return background;
	}

	public boolean isWalkable() {
		return walkable;
	}

	public void setWalkable(boolean walkable) {
		this.walkable = walkable;
	}

	public void setBackground(Image background) {
		this.background = background;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void draw(Graphics2D g, int screenX, int screenY) {
		g.drawImage(getBackground(), screenX, screenY, null);
	}

}
