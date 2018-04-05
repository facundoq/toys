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
package org.tiledzelda.events.eventgeneration;

import org.tiledzelda.exceptions.GameError;
import org.tiledzelda.exceptions.InvalidPositionException;
import org.tiledzelda.exceptions.OcuppiedPositionException;
import org.tiledzelda.main.GameEntities;
import org.tiledzelda.main.GameFactories;
import org.tiledzelda.main.GameObjects;
import org.tiledzelda.model.elements.movableElements.creeps.Creep;
import org.tiledzelda.model.map.TileMap;
import org.tiledzelda.model.map.maputils.Position;

/**
 * @author Facundo Quiroga Creation date: 22/10/2008
 */
public class CreepSpawner {
	protected TileMap tileMap;
	protected long accumulatedTime;

	public CreepSpawner(TileMap tileMap) {
		this.setTileMap(tileMap);
		this.setAccumulatedTime(0);

	}

	public void update(long elapsedTime) {
		this.setAccumulatedTime(this.getAccumulatedTime() + elapsedTime);
		if (this.getAccumulatedTime() > this.defaultTimeBetweenUpdates()) {
			this.setAccumulatedTime(this.getAccumulatedTime() - this.defaultTimeBetweenUpdates());
			this.spawnCreep();
		}
	}

	protected void spawnCreep() {
		TileMap map = this.getTileMap();
		int x = GameObjects.getInstance().getRandomNumberGenerator().randomNumber() % map.getWidth();
		int y = GameObjects.getInstance().getRandomNumberGenerator().randomNumber() % map.getHeight();
		//Position spawningPosition = new Position(x,y);
		Position spawningPosition = Position.newPosition(x, y);
		if (map.validPosition(spawningPosition)) {
			if (map.occupable(spawningPosition)) {
				doSpawnCreep(map, spawningPosition);
			}
		}
	}

	protected void doSpawnCreep(TileMap map, Position spawningPosition) {

		try {
			Creep creep = GameFactories.getInstance().getCreepFactory().createRandomCreep(map, spawningPosition);
			/*RandomNumberGenerator randomNumberGenerator = GameObjects.getInstance().getRandomNumberGenerator();
			int interval = randomNumberGenerator.getUpperRange()-randomNumberGenerator.getLowerRange();
			int cutOff = (interval * 20) / 100;
			if (randomNumberGenerator.randomNumber() < cutOff){
				creep = GameFactories.getInstance().getCreepFactory().createCreep("monster.red",map, spawningPosition);
			}else if ( randomNumberGenerator.randomNumber() < cutOff) {
				creep = GameFactories.getInstance().getCreepFactory().createCreep("monster.blueGreen",map, spawningPosition);
			}else {
				creep = GameFactories.getInstance().getCreepFactory().createCreep("monster.yellow",map, spawningPosition);
			}*/
			GameEntities.getInstance().addElement(creep);
		} catch (InvalidPositionException e) {
			throw new GameError("Position was both valid and occupable");
		} catch (OcuppiedPositionException e) {
			throw new GameError("Position was both valid and occupable");
		}
	}

	public TileMap getTileMap() {
		return this.tileMap;
	}

	/**
	 * @return default MS between updates
	 */
	protected long defaultTimeBetweenUpdates() {
		return 1000;
	}

	public void setTileMap(TileMap tileMap) {
		this.tileMap = tileMap;
	}

	public long getAccumulatedTime() {
		return this.accumulatedTime;
	}

	public void setAccumulatedTime(long accumulatedTime) {
		this.accumulatedTime = accumulatedTime;
	}

}
