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

import java.awt.Dimension;
import java.awt.DisplayMode;

import org.tiledzelda.exceptions.GameError;
import org.tiledzelda.exceptions.InvalidPositionException;
import org.tiledzelda.exceptions.OcuppiedPositionException;
import org.tiledzelda.exceptions.PositionException;
import org.tiledzelda.main.resourcemanagement.Configuration;
import org.tiledzelda.main.resourcemanagement.FloorCellFactory;
import org.tiledzelda.main.resourcemanagement.StaticElementFactory;
import org.tiledzelda.model.elements.movableElements.creeps.Creep;
import org.tiledzelda.model.elements.movableElements.player.Player;
import org.tiledzelda.model.entities.FloorCell;
import org.tiledzelda.model.map.TileMap;
import org.tiledzelda.model.map.maputils.Area;
import org.tiledzelda.model.map.maputils.Position;
import org.tiledzelda.visualization.screen.FullScreenManager;
import org.tiledzelda.visualization.screen.ScreenManager;
import org.tiledzelda.visualization.screen.WindowedScreenManager;

public class GameInitializer {

	/*
	 * private static final DisplayMode POSSIBLE_MODES[] = { new
	 * DisplayMode(800, 600, 16, 0), new DisplayMode(800, 600, 32, 0), new
	 * DisplayMode(800, 600, 24, 0), new DisplayMode(640, 480, 16, 0), new
	 * DisplayMode(640, 480, 32, 0), new DisplayMode(640, 480, 24, 0), new
	 * DisplayMode(1024, 768, 16, 0), new DisplayMode(1024, 768, 32, 0), new
	 * DisplayMode(1024, 768, 24, 0), new DisplayMode(1280, 1024, 16, 0), new
	 * DisplayMode(1280, 1024, 24, 0), new DisplayMode(1280, 1024, 32, 0) };
	 */

	public ScreenManager initializeFullScreen() {

		FullScreenManager screen = FullScreenManager.getInstance();
		// DisplayMode displayMode =
		// screen.findFirstCompatibleMode(POSSIBLE_MODES);
		// DisplayMode displayMode = POSSIBLE_MODES[6];
		// screen.setFullScreen(displayMode);
		screen.setFullScreen(new DisplayMode(1024, 768, 32, 0));
		return screen;
	}

	public ScreenManager initializeWindowedScreen() {

		WindowedScreenManager screen = WindowedScreenManager.getInstance();
		screen.setDimension(new Dimension(1024, 768));
		//screen.setDimension(new Dimension(1280, 1024));
		return screen;
	}

	public TileMap createExampleMap() {
		int size = Configuration.getInstance().getValueAsInt("mapSize");

		TileMap tileMap = new TileMap(size, size);
		FloorCellFactory floorCellFactory = GameFactories.getInstance().getFloorCellFactory();
		FloorCell earth = floorCellFactory.getFloorCell("earth");
		FloorCell grass = floorCellFactory.getFloorCell("grass");
		FloorCell water = floorCellFactory.getFloorCell("water");

		// add floor
		for (Position position : tileMap.area()) {
			tileMap.setFloor(position, earth);
		}

		Area area = new Area(Position.newPosition(12, 17), Position.newPosition(27, 24));

		for (Position position : area) {
			tileMap.setFloor(position, water);
		}

		area = new Area(Position.newPosition(40, 35), Position.newPosition(44, 39));
		for (Position position : area) {
			tileMap.setFloor(position, water);
		}

		area = new Area(Position.newPosition(10, 11), Position.newPosition(19, 37));
		for (Position position : area) {
			tileMap.setFloor(position, grass);
		}

		StaticElementFactory staticElementFactory = GameFactories.getInstance().getStaticElementFactory();
		// add group of rocks
		area = new Area(Position.newPosition(43, 45), Position.newPosition(48, 46));
		for (Position position : area) {
			try {
				staticElementFactory.newStaticElement("rock", tileMap, position);
			} catch (PositionException e) {
				throw new GameError("Positions should be both occupable and valid");
			}
		}

		// add little forest!
		area = new Area(Position.newPosition(33, 43), Position.newPosition(41, 45));
		for (Position position : area) {
			try {
				staticElementFactory.newStaticElement("tree", tileMap, position);
			} catch (PositionException e) {
				throw new GameError("Positions should be both occupable and valid");
			}
		}

		// add that single rock
		try {
			Position position = Position.newPosition(1, 2);
			staticElementFactory.newStaticElement("rock", tileMap, position);
		} catch (PositionException e) {
			throw new GameError("Positions should be both occupable and valid");
		}

		return tileMap;
	}

	public Player createPlayer(TileMap mainMap, Position position) throws InvalidPositionException, OcuppiedPositionException {
		return GameFactories.getInstance().getPlayerFactory().createPlayer(mainMap, position);
	}

	public Creep createFollowingCreep(TileMap mainMap, Position position) throws InvalidPositionException, OcuppiedPositionException {
		return GameFactories.getInstance().getCreepFactory().createCreep("monster.red", mainMap, position);
	}

	public Creep createEscapingCreep(TileMap mainMap, Position position) throws InvalidPositionException, OcuppiedPositionException {
		return GameFactories.getInstance().getCreepFactory().createCreep("monster.green", mainMap, position);
	}

}
