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

package org.tiledzelda.visualization.graphics.animations;

import java.awt.Image;

/**
 * Represents an animation frame, with the image and the percent of the animation it takes
 * 
 * @author Facundo Manuel Quiroga Feb 11, 2009
 */
public class AnimationFrame {

	protected Image image;
	protected long percent;

	public AnimationFrame(Image image, long percent) {
		this.setImage(image);
		this.setPercent(percent);
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	protected long getPercentage() {
		return this.percent;
	}

	protected void setPercent(long percent) {
		this.percent = percent;
	}
}
