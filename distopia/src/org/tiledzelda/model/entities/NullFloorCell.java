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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import org.tiledzelda.main.resourcemanagement.Configuration;

/**
 * Represents a NullFloorCell, ie, a floor cell where there is nothing
 * 
 * @author Facundo Manuel Quiroga Oct 19, 2008
 */
public class NullFloorCell extends FloorCell {

	private static NullFloorCell instance = null;
	private BufferedImage blankImage;

	public static NullFloorCell getInstance() {
		if (NullFloorCell.instance == null) {
			NullFloorCell.instance = new NullFloorCell();
		}
		return NullFloorCell.instance;
	}

	protected NullFloorCell() {
		super();
		this.setWalkable(false);
		this.setName("NullFloorCell");
		this.blankImage = new BufferedImage(Configuration.getInstance().getValueAsInt("blockSize"), Configuration.getInstance().getValueAsInt("blockSize"), BufferedImage.TYPE_INT_BGR);
	}

	@Override
	public void draw(Graphics2D g, int screenX, int screenY) {
		// do nothing! yay! this has to be really quick!
		int blockSize = Configuration.getInstance().getValueAsInt("blockSize");
		g.setColor(Color.cyan);
		g.fillRect(screenX, screenY, blockSize, blockSize);
	}

	@Override
	public Image getBackground() {
		return this.blankImage;
	}

	@Override
	public String getName() {
		return super.getName();
	}

	@Override
	public boolean isWalkable() {
		return super.isWalkable();
	}

	@Override
	public void setBackground(Image background) {
		super.setBackground(background);
	}

	@Override
	public void setName(String name) {
		super.setName(name);
	}

	public String toString() {
		return this.getName();
	}

	@Override
	public void setWalkable(boolean walkable) {
		super.setWalkable(walkable);
	}

}
