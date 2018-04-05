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

package org.tiledzelda.main.resourcemanagement.graphics.sprites.movable;

import java.util.HashMap;

import org.tiledzelda.main.GameFactories;
import org.tiledzelda.model.map.maputils.Direction;
import org.tiledzelda.visualization.graphics.animations.Animation;
import org.tiledzelda.visualization.graphics.element.movable.MovableSprite;

/**
 * @author Facundo Manuel Quiroga Mar 6, 2009
 */
public class MovableSpriteFactory {

	protected HashMap<String, MovableSpriteCloner> movableSpriteCloners;
	protected HashMap<String, MovableSpriteCloner> simpleMovableSpriteCloners;

	public MovableSpriteFactory() {
		this.movableSpriteCloners = new HashMap<String, MovableSpriteCloner>();
		this.simpleMovableSpriteCloners = new HashMap<String, MovableSpriteCloner>();
	}

	public MovableSprite getSimpleMovableSprite(String name) {
		MovableSpriteCloner movableSpriteCloner = this.simpleMovableSpriteCloners.get(name);
		if (movableSpriteCloner == null) {
			MovableSprite movableSprite = this.loadSimpleMovableSprite(name);
			movableSpriteCloner = new MovableSpriteCloner(movableSprite);
			simpleMovableSpriteCloners.put(name, movableSpriteCloner);
		}
		return movableSpriteCloner.getMovableSprite();
	}

	/**
	 * @param name of the MovableSprite
	 * @return the MovableSprite loaded from disk
	 */
	protected MovableSprite loadSimpleMovableSprite(String name) {
		Animation animation = GameFactories.getInstance().getAnimationFactory().getAnimation(name);
		HashMap<Direction, Animation> animations = new HashMap<Direction, Animation>();
		for (Direction direction : Direction.allDirections()) {
			animations.put(direction, animation);
		}
		return new MovableSprite(animations);
	}

	public MovableSprite getMovableSprite(String name) {
		MovableSpriteCloner movableSpriteCloner = this.movableSpriteCloners.get(name);
		if (movableSpriteCloner == null) {
			MovableSprite movableSprite = this.loadMovableSprite(name);
			movableSpriteCloner = new MovableSpriteCloner(movableSprite);
			movableSpriteCloners.put(name, movableSpriteCloner);
		}
		return movableSpriteCloner.getMovableSprite();
	}

	/**
	 * @param name of the MovableSprite
	 * @return the MovableSprite loaded from disk
	 */
	protected MovableSprite loadMovableSprite(String name) {
		HashMap<Direction, Animation> animations = new HashMap<Direction, Animation>();
		for (Direction direction : Direction.allDirections()) {
			String animationName = name + "." + direction.toString();
			Animation animation = GameFactories.getInstance().getAnimationFactory().getAnimation(animationName);
			animations.put(direction, animation);
		}
		return new MovableSprite(animations);
	}

}
