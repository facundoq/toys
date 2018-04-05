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

package org.tiledzelda.visualization.graphics.element.movable;

import java.awt.Image;
import java.util.HashMap;

import org.tiledzelda.exceptions.GameError;
import org.tiledzelda.model.map.maputils.Direction;
import org.tiledzelda.visualization.graphics.animations.Animation;
import org.tiledzelda.visualization.graphics.element.Sprite;

/**
 * Has Animations for both the graphic of an element in it's normal state (standing still) and for when it is moving. Since this can happen in 8 different directions, it stores 8 animations in a
 * 3x3grid, 0,0 being -1,-1, etc, with 1,1 being the center of the grid.
 * 
 * @author Facundo Manuel Quiroga
 */
public class MovableSprite extends Sprite {
	//TODO decide how to interpret horizontal and vertical Direction
	// is it the array position, or the direction its facing/moving?
	HashMap<Direction, Animation> animations;
	Direction direction;

	/**
	 * @param direction : initial direction the sprite will be looking at
	 */
	public MovableSprite(Direction initialDirection) {
		super();
		setAnimations(new HashMap<Direction, Animation>());
		setDirection(initialDirection);
	}

	public MovableSprite(HashMap<Direction, Animation> animations) {
		this(Direction.newDirection("n"), animations);
	}

	public MovableSprite(Direction initialDirection, HashMap<Direction, Animation> animations) {
		super();
		setDirection(initialDirection);
		setAnimations(new HashMap<Direction, Animation>(animations));
	}

	//interface	
	public Animation getAnimation(Direction direction) {
		if (this.getAnimations().containsKey(direction)) {
			return getAnimations().get(direction);
		} else {
			throw new GameError("No animation for direction:" + direction);
		}
	}

	public void addAnimation(Animation animation, Direction direction) {
		this.getAnimations().put(direction, animation);
	}

	public Animation getCurrentAnimation() {
		return getAnimation(this.getDirection());
	}

	public void changeDirection(Direction direction) {
		this.setDirection(direction);
	}

	@Override
	public int getHeight() {
		return this.getImage().getHeight(null);
	}

	@Override
	public Image getImage() {
		Image image = null;
		image = this.getCurrentAnimation().getImage();
		return image;
	}

	@Override
	public int getWidth() {
		return this.getImage().getWidth(null);
	}

	@Override
	public void update(long time) {
		this.getCurrentAnimation().update(time);
	}

	private Direction getDirection() {
		return direction;
	}

	private void setDirection(Direction direction) {
		this.direction = direction;
	}

	private HashMap<Direction, Animation> getAnimations() {
		return animations;
	}

	protected void setAnimations(HashMap<Direction, Animation> animations) {
		this.animations = animations;
	}

	/**
	 * @param speed
	 */
	public void setSpeed(int speed) {
		for (Animation animation : this.getAnimations().values()) {
			animation.setTotalDuration(speed);
			animation.reset();
		}
	}

	public MovableSprite clone() {
		HashMap<Direction, Animation> clonedAnimations = new HashMap<Direction, Animation>();
		for (Direction direction : Direction.allDirections()) {
			Animation animation = this.getAnimation(direction);
			clonedAnimations.put(direction, animation.clone());
		}
		return new MovableSprite(this.getDirection(), clonedAnimations);
	}

}
