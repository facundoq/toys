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

package org.tiledzelda.visualization.graphics.element;

import java.awt.Graphics2D;
import java.awt.Image;

import org.tiledzelda.main.resourcemanagement.Configuration;

/**
 * Represents an element's avatar, strictly at pixel level (so it should have no interaction with game logic) updated by element, if necessary. Since we know the Element's position this graphic refers
 * to, we can always calculate the pixel position of the center of the tile. Hence, we use x,y to represent the pixel DISTANCE of the element, relative to the pixel position of the center of the tile.
 * So, we can use x,y it to move the element's image a bit around it's tile position. It applies also to non-movable elements, since some of them may have to move a little (not in the Map, but
 * relatively to their position).
 * 
 * @author Facundo Manuel Quiroga
 */

public abstract class ElementGraphic implements Graphic {

	protected Configuration configuration;

	// element's relative position

	protected ElementGraphic() {

		configuration = Configuration.getInstance();
	}

	public abstract void update(long elapsedTime);

	/**
	 * Draws an element's graphic according to it's current sprite
	 * 
	 * @param g Graphics context where to draw
	 * @param originalScreenX X Position on the screen to draw
	 * @param originalScreenY Y Position on the screen to draw
	 */
	public void draw(Graphics2D g, int originalScreenX, int originalScreenY) {
		Image image = this.getCurrentImage();
		ScreenPoint point = getDrawingScreenPoint(originalScreenX, originalScreenY, image);
		this.doDraw(g, image, point);
	}

	protected ScreenPoint getDrawingScreenPoint(int originalScreenX, int originalScreenY, Image image) {
		int blockSize = configuration.getValueAsInt("blockSize");
		int imageWidth = image.getWidth(null);
		int imageHeight = image.getHeight(null);

		int differenceX = blockSize - imageWidth;
		int differenceY = blockSize - imageHeight;

		//for width, make it so the center of the image matches the center of the tile
		int screenX = originalScreenX + (differenceX / 2);
		//for height, make it so the base of the image matches the floor of the tile
		int screenY = originalScreenY + differenceY;

		// take into account the element relative position 
		screenX += this.getXCorrection();
		screenY += this.getYCorrection();

		return ScreenPoint.newScreenPoint(screenX, screenY);
	}

	/**
	 * @return
	 */
	public abstract int getYCorrection();

	/**
	 * @return
	 */
	public abstract int getXCorrection();

	protected void doDraw(Graphics2D g, Image image, ScreenPoint point) {
		g.drawImage(image, point.x, point.y, null);
	}

	public abstract Image getCurrentImage();

}
