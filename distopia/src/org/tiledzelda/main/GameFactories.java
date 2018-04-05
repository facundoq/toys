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

package org.tiledzelda.main;

import org.tiledzelda.main.resourcemanagement.FloorCellFactory;
import org.tiledzelda.main.resourcemanagement.StaticElementFactory;
import org.tiledzelda.main.resourcemanagement.graphics.MovableElementGraphicFactory;
import org.tiledzelda.main.resourcemanagement.graphics.animations.AnimationFactory;
import org.tiledzelda.main.resourcemanagement.graphics.sprites.movable.MovableSpriteFactory;
import org.tiledzelda.model.elements.factories.CreepFactory;
import org.tiledzelda.model.elements.factories.PlayerFactory;

/**
 * @author Facundo Manuel Quiroga Mar 6, 2009
 */
public class GameFactories extends Object {
	protected static GameFactories instance;

	public static GameFactories getInstance() {
		if (GameFactories.instance == null) {
			GameFactories.instance = new GameFactories();
		}
		return GameFactories.instance;
	}

	protected String animationsDirectoryPath;

	private GameFactories() {
		this.animationsDirectoryPath = "resources/animations";
	}

	protected MovableSpriteFactory movableSpriteFactory = null;

	public MovableSpriteFactory getMovableSpriteFactory() {
		if (movableSpriteFactory == null) {
			movableSpriteFactory = new MovableSpriteFactory();
		}
		return movableSpriteFactory;
	}

	protected AnimationFactory animationFactory = null;

	public AnimationFactory getAnimationFactory() {
		if (animationFactory == null) {
			animationFactory = new AnimationFactory(animationsDirectoryPath);
		}
		return animationFactory;
	}

	protected MovableElementGraphicFactory movableElementGraphicFactory = null;

	public MovableElementGraphicFactory getMovableElementGraphicFactory() {
		if (movableElementGraphicFactory == null) {
			movableElementGraphicFactory = new MovableElementGraphicFactory();
		}
		return movableElementGraphicFactory;
	}

	protected PlayerFactory playerFactory = null;

	public PlayerFactory getPlayerFactory() {
		if (playerFactory == null) {
			playerFactory = new PlayerFactory();
		}
		return playerFactory;
	}

	protected CreepFactory creepFactory = null;

	public CreepFactory getCreepFactory() {
		if (creepFactory == null) {
			creepFactory = new CreepFactory();
		}
		return creepFactory;
	}

	protected FloorCellFactory floorCellFactory = null;

	public FloorCellFactory getFloorCellFactory() {
		if (floorCellFactory == null) {
			floorCellFactory = new FloorCellFactory();
		}
		return floorCellFactory;
	}

	protected StaticElementFactory staticElementFactory = null;

	public StaticElementFactory getStaticElementFactory() {
		if (staticElementFactory == null) {
			staticElementFactory = new StaticElementFactory();
		}
		return staticElementFactory;
	}

	protected Logger factoriesLogger = null;

	public Logger getFactoriesLogger() {
		if (factoriesLogger == null) {
			factoriesLogger = new Logger();
		}
		return factoriesLogger;
	}
}
