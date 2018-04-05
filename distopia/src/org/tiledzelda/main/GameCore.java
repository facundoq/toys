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

import java.awt.Panel;

import org.tiledzelda.events.eventgeneration.CreepSpawner;
import org.tiledzelda.exceptions.GameError;
import org.tiledzelda.input.ai.AIManager;
import org.tiledzelda.input.human.InputAdapter;
import org.tiledzelda.main.logic.FPSCounter;
import org.tiledzelda.main.logic.RandomNumberGenerator;
import org.tiledzelda.main.logic.TimeKeeper;
import org.tiledzelda.model.elements.movableElements.creeps.Creep;
import org.tiledzelda.model.elements.movableElements.player.Player;
import org.tiledzelda.model.map.LimboTileMap;
import org.tiledzelda.model.map.TileMap;
import org.tiledzelda.model.map.maputils.Position;
import org.tiledzelda.visualization.graphics.SoundManager;
import org.tiledzelda.visualization.gui.SidePanelGUI;
import org.tiledzelda.visualization.rendering.TileMapRenderer;
import org.tiledzelda.visualization.screen.ScreenManager;

/**
 * @author Facundo Quiroga Creation date: 2/2/2008
 */
public class GameCore {

	protected static GameCore instance;

	public static GameCore getInstance() {
		if (GameCore.instance == null) {
			GameCore.instance = new GameCore();
		}
		return GameCore.instance;
	}

	private boolean isRunning;
	public SoundManager soundManager;
	protected ScreenManager screenManager;

	protected TileMap mainMap;
	protected CreepSpawner mainMapCreepSpawner;
	protected Player player;

	protected TileMapRenderer tileMapRenderer;

	protected InputAdapter inputAdapter;

	protected SidePanelGUI sidePanelGUI;

	/**
	 * GameObjects
	 */

	protected ProfilingStats profilingStats;
	protected TimeKeeper timeKeeper;
	protected RandomNumberGenerator randomNumberGenerator;
	protected AIManager aiManager;
	protected FPSCounter fpsCounter;
	protected LimboTileMap limboTileMap;

	private GameCore() {
		super();
	}

	public void run() {
		try {
			init();
			gameLoop();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//screenManager.restoreScreen();
			lazilyExit();
		}
	}

	/**
	 * Exits the VM from a daemon thread. The daemon thread waits 2 seconds then calls System.exit(0). Since the VM should exit when only daemon threads are running, this makes sure System.exit(0) is
	 * only called if necessary. It's necessary if the Java Sound system is running.
	 */
	public void lazilyExit() {
		Thread thread = new Thread() {
			public void run() {
				// wait it out
				try {
					Thread.sleep(2000);
				} catch (InterruptedException ex) {

				}
				// if it has not exited by itself, exit
				System.exit(0);
			}
		};
		thread.setDaemon(true);
		thread.start();
	}

	public void init() {
		GameInitializer gameInitializer = new GameInitializer();
		soundManager=new SoundManager();
		//screenManager = gameInitializer.initializeFullScreen();
		screenManager = gameInitializer.initializeWindowedScreen();
		mainMap = gameInitializer.createExampleMap();
		this.mainMapCreepSpawner = new CreepSpawner(mainMap);
		GameEntities.getInstance().addMap(mainMap);
		Creep creep;
		try {
			player = gameInitializer.createPlayer(mainMap, Position.newPosition(8, 8));
			GameEntities.getInstance().addElement(player);
			for (int i = 0; i < 7; i++) {
				creep = gameInitializer.createFollowingCreep(mainMap, Position.newPosition(8, 40 + i));
				GameEntities.getInstance().addElement(creep);
			}
			GameEntities.getInstance().addElement(player);
			for (int i = 0; i < 7; i++) {
				creep = gameInitializer.createFollowingCreep(mainMap, Position.newPosition(9, 40 + i));
				GameEntities.getInstance().addElement(creep);
			}
			for (int i = 0; i < 7; i++) {
				creep = gameInitializer.createEscapingCreep(mainMap, Position.newPosition(30, 40 + i));
				GameEntities.getInstance().addElement(creep);
			}
			for (int i = 0; i < 7; i++) {
				creep = gameInitializer.createEscapingCreep(mainMap, Position.newPosition(31, 40 + i));
				GameEntities.getInstance().addElement(creep);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		tileMapRenderer = new TileMapRenderer();
		inputAdapter = new InputAdapter(this, player);
		isRunning = true;

		profilingStats = new ProfilingStats(false);
		GameObjects gameObjects = GameObjects.getInstance();
		timeKeeper = gameObjects.getTimeKeeper();
		randomNumberGenerator = gameObjects.getRandomNumberGenerator();
		fpsCounter = gameObjects.getFPSCounter();
		aiManager = gameObjects.getAIManager();
		limboTileMap = gameObjects.getLimboTileMap();

		Panel sidePanel = screenManager.getSidePanel();
		this.sidePanelGUI = new SidePanelGUI(sidePanel, player);
	}

	public void gameLoop() {
		/**
		 * Updates the state of the game/animation based on the amount of elapsed time that has passed.
		 */
		while (this.isRunning()) {
			profilingStats.startOfLoop();

			timeKeeper.update();
			profilingStats.finishedSystem("TimeKeeper");

			long elapsedTime = GameObjects.getInstance().getTimeKeeper().getElapsedTimeSinceLastUpdate();
			// update
			randomNumberGenerator.randomize();
			profilingStats.finishedSystem("RandomNumberGenerator");

			GameEntities.getInstance().updateElements(elapsedTime);
			profilingStats.finishedSystem("UpdateElements");

			inputAdapter.checkInput(elapsedTime);
			profilingStats.finishedSystem("InputAdapter");

			aiManager.think();
			profilingStats.finishedSystem("AI");

			// ActionPipeline.getInstance().processQueue();
			
			this.mainMapCreepSpawner.update(elapsedTime);
			profilingStats.finishedSystem("CreepSpawner");

			tileMapRenderer.draw(screenManager, player, player);
			profilingStats.finishedSystem("Render");
			
			if (this.player.getHealthState().isDead()) {
				this.stop();
			}
			if (limboTileMap.getElements().size() > 0) {
				throw new GameError("there should not be any elements in the LimboMap");
			}
			fpsCounter.count();
			this.sidePanelGUI.update();
			profilingStats.finishedSystem("FPS and SidePanel");

			profilingStats.endOfLoop();
		}
	}

	private boolean isRunning() {
		return this.isRunning;
	}

	public void stop() {
		isRunning = false;
	}

	public ScreenManager getScreen() {
		return this.screenManager;
	}

	public Player getPlayer() {
		return this.player;
	}
}
