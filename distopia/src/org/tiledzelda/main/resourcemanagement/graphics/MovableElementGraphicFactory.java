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

package org.tiledzelda.main.resourcemanagement.graphics;

import java.util.HashMap;

import org.tiledzelda.main.GameFactories;
import org.tiledzelda.main.resourcemanagement.graphics.sprites.movable.MovableSpriteFactory;
import org.tiledzelda.visualization.graphics.element.movable.MovableElementGraphic;
import org.tiledzelda.visualization.graphics.element.movable.MovableSprite;

/**
 * @author Facundo Manuel Quiroga Mar 10, 2009
 */
public class MovableElementGraphicFactory {
	protected HashMap<String, MovableElementGraphic> movableElementGraphics;
	protected HashMap<String, MovableElementGraphic> simpleMovableElementGraphics;

	public MovableElementGraphicFactory() {
		this.simpleMovableElementGraphics = new HashMap<String, MovableElementGraphic>();
		this.movableElementGraphics = new HashMap<String, MovableElementGraphic>();
	}

	/**
	 * @param name of the MovableElementGraphic, in foo.foo.(...).foo.bar form, where each foo is a directory, and bar is the directory inside the last foo where the MovableElementGraphic can be
	 * found. The MovableElementGraphic must be a complex one, having complex MovableSprites ( complex = 1 Animation per Direction, unlike the simple ones with the same for all Directions)
	 * @return the MovableElementGraphic
	 */
	public MovableElementGraphic getMovableElementGraphic(String name) {
		MovableElementGraphic movableElementGraphic = this.movableElementGraphics.get(name);
		if (movableElementGraphic == null) {
			movableElementGraphic = this.loadMovableElementGraphic(name);
			movableElementGraphics.put(name, movableElementGraphic);
		}
		return movableElementGraphic.clone();
	}

	/**
	 * @param name of the MovableElementGraphic
	 * @return the MovableElementGraphic loaded from disk
	 */
	protected MovableElementGraphic loadMovableElementGraphic(String name) {
		MovableSpriteFactory movableSpriteFactory = GameFactories.getInstance().getMovableSpriteFactory();
		MovableSprite idle = movableSpriteFactory.getMovableSprite(name + ".idle");
		MovableSprite walking = movableSpriteFactory.getMovableSprite(name + ".moving");
		return new MovableElementGraphic(idle, walking);
	}

	/**
	 * @param name of the MovableElementGraphic, in foo.foo.foo.bar form, where each foo is a directory, and bar is the directory inside the last foo where the MovableElementGraphic can be found. The
	 * MovableElementGraphic must be a simple one, having simple MovableSprites ( simple = the same Animation for all Directions, unlike the complex ones with one Animation for every Direction)
	 * @return the MovableElementGraphic
	 */
	public MovableElementGraphic getSimpleMovableElementGraphic(String name) {
		MovableElementGraphic movableElementGraphic = this.movableElementGraphics.get(name);
		if (movableElementGraphic == null) {
			movableElementGraphic = this.loadSimpleMovableElementGraphic(name);
			movableElementGraphics.put(name, movableElementGraphic);
		}
		return movableElementGraphic.clone();
	}

	/**
	 * @param name of the simple MovableElementGraphic
	 * @return the MovableElementGraphic loaded from disk
	 */
	private MovableElementGraphic loadSimpleMovableElementGraphic(String name) {
		MovableSpriteFactory movableSpriteFactory = GameFactories.getInstance().getMovableSpriteFactory();
		MovableSprite idle = movableSpriteFactory.getSimpleMovableSprite(name + ".idle");
		MovableSprite walking = movableSpriteFactory.getSimpleMovableSprite(name + ".moving");
		return new MovableElementGraphic(idle, walking);
	}

}
