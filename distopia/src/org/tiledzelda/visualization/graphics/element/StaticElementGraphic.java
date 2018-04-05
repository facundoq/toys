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

import java.awt.Image;

import org.tiledzelda.visualization.graphics.animations.Animation;

/**
 * As ElementGraphic, represents an element's avatar, strictly at pixel level, and holds the animation the static element may have.
 * 
 * @author Facundo Manuel Quiroga
 */

public class StaticElementGraphic extends ElementGraphic {
	private Animation animation;

	public Animation getAnimation() {
		return animation;
	}

	public void setAnimation(Animation animation) {
		this.animation = animation;
	}

	public StaticElementGraphic(Animation animation) {
		super();
		this.setAnimation(animation);
	}

	@Override
	public void update(long elapsedTime) {
		this.getAnimation().update(elapsedTime);
	}

	@Override
	public Image getCurrentImage() {
		return getAnimation().getImage();
	}

	public int getXCorrection() {
		return 0;
	}

	public int getYCorrection() {
		return 0;
	}
}
