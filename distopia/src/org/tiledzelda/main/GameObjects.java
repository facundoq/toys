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

import org.tiledzelda.input.ai.AIManager;
import org.tiledzelda.main.logic.FPSCounter;
import org.tiledzelda.main.logic.RandomNumberGenerator;
import org.tiledzelda.main.logic.TimeKeeper;
import org.tiledzelda.model.map.LimboTileMap;

/**
 * @author Facundo Manuel Quiroga Mar 10, 2009
 */
public class GameObjects {
	protected static GameObjects instance;

	public static GameObjects getInstance() {
		if (GameObjects.instance == null) {
			GameObjects.instance = new GameObjects();
		}
		return GameObjects.instance;
	}

	TimeKeeper timeKeeper = null;

	public TimeKeeper getTimeKeeper() {
		if (timeKeeper == null) {
			timeKeeper = new TimeKeeper();
		}
		return timeKeeper;
	}

	RandomNumberGenerator randomNumberGenerator = null;

	public RandomNumberGenerator getRandomNumberGenerator() {
		if (randomNumberGenerator == null) {
			randomNumberGenerator = new RandomNumberGenerator(RandomNumberGenerator.defaultNumberOfNumbers());
		}
		return randomNumberGenerator;
	}

	protected FPSCounter fpsCounter = null;

	public FPSCounter getFPSCounter() {
		if (fpsCounter == null) {
			fpsCounter = new FPSCounter();
		}
		return fpsCounter;
	}

	protected AIManager aiManager = null;

	public AIManager getAIManager() {
		if (aiManager == null) {
			aiManager = new AIManager();
		}
		return aiManager;
	}

	protected LimboTileMap limboTileMap = null;

	public LimboTileMap getLimboTileMap() {
		if (limboTileMap == null) {
			limboTileMap = new LimboTileMap();
		}
		return limboTileMap;
	}
}
