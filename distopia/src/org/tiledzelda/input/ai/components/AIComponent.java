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

package org.tiledzelda.input.ai.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.tiledzelda.events.actions.Action;
import org.tiledzelda.input.ai.AI;
import org.tiledzelda.input.ai.NoValidDirectionForMovingException;
import org.tiledzelda.main.GameObjects;
import org.tiledzelda.main.logic.RandomNumberGenerator;
import org.tiledzelda.model.elements.movableElements.creeps.Creep;
import org.tiledzelda.model.map.TileMap;
import org.tiledzelda.model.map.maputils.Area;
import org.tiledzelda.model.map.maputils.Direction;
import org.tiledzelda.model.map.maputils.Position;

/**
 * @author Facundo Manuel Quiroga Feb 10, 2009
 */
public abstract class AIComponent {

	protected AI ai;

	public AIComponent() {
	}

	public abstract List<Action> doThink();

	protected Collection<Position> emptyAdjacentPositions() {
		TileMap creepsMap = this.getCreep().getCurrentTileMap();

		Position creepsPosition = this.getCreep().getPosition();

		Area nearbyArea = Area.newAreaAround(creepsPosition, 1);
		Collection<Position> positions = new ArrayList<Position>();
		for (Position position : nearbyArea) {
			if (creepsMap.validPosition(position)) {
				if (creepsMap.occupable(position)) {
					positions.add(position);
				}
			}
		}
		assert (!positions.contains(creepsPosition));
		return positions;
	}

	public Creep getCreep() {
		return this.ai.getCreep();
	}

	protected Collection<Direction> validDirectionsForMoving() {
		Collection<Direction> directions = new ArrayList<Direction>();
		Position creepsPosition = this.getCreep().getPosition();
		for (Position position : this.emptyAdjacentPositions()) {
			directions.add(creepsPosition.directionToGoTo(position));
		}
		return directions;
	}

	protected Direction randomValidDirectionForMoving() throws NoValidDirectionForMovingException {
		Collection<Direction> directions = this.validDirectionsForMoving();
		if (directions.isEmpty()) {
			throw new NoValidDirectionForMovingException("");
		}
		RandomNumberGenerator randomNumberGenerator = GameObjects.getInstance().getRandomNumberGenerator();
		int moves = randomNumberGenerator.randomNumber() % directions.size();
		Iterator<Direction> i = directions.iterator();
		Direction direction = null;
		while (moves >= 0) {
			direction = i.next();
			moves--;
		}
		return direction;
	}

	protected int getAreaOfDetection() {
		return this.ai.getAreaOfDetection();
	}

	public AI getAi() {
		return this.ai;
	}

	public void setAi(AI ai) {
		this.ai = ai;
	}

}
