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

/**
 * Represents a NullElementGraphic, ie, no graphic, which translate into not drawing anything
 * 
 * @author Facundo Quiroga Oct 19, 2008
 */
public class NullElementGraphic extends ElementGraphic {

	public NullElementGraphic() {

	}

	@Override
	public Image getCurrentImage() {

		return null;
	}

	public void draw(Graphics2D g, int originalScreenX, int originalScreenY) {
		// do nothing!
	}

	@Override
	public void update(long elapsedTime) {
		// do nothing!
	}

	@Override
	public int getXCorrection() {
		return 0;
	}

	@Override
	public int getYCorrection() {
		return 0;
	}

}
