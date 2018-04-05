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

package org.tiledzelda.input.ai;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.tiledzelda.events.actionProcessing.ActionPipeline;
import org.tiledzelda.events.actions.Action;
import org.tiledzelda.input.ai.components.AIComponent;
import org.tiledzelda.main.GameObjects;
import org.tiledzelda.main.logic.RandomNumberGenerator;
import org.tiledzelda.model.elements.movableElements.creeps.Creep;
import org.tiledzelda.model.map.TileMap;
import org.tiledzelda.model.map.maputils.Area;
import org.tiledzelda.model.map.maputils.Direction;
import org.tiledzelda.model.map.maputils.Position;

/**
 * Represents a Creep's IA Determines next actions for creep and adds them to the action pipeline.
 * 
 * @author Facundo 30/09/2008
 */
public class AI {

	private static int getDefaultAreaOfInfluence() {
		return 7;
	}

	private Creep creep;

	protected int areaOfInfluence;
	protected AIComponent currentAIComponent;

	public AI(AIComponent aiComponent) {
		this(aiComponent, AI.getDefaultAreaOfInfluence());
	}

	public AI(AIComponent aiComponent, int areaOfInfluence) {
		this.setAreaOfInfluence(areaOfInfluence);
		this.currentAIComponent = aiComponent;
		aiComponent.setAi(this);
	}

	public void think() {
		// Don't try to act if creep is dead; funny things can happen.
		if (!this.getCreep().getHealthState().isDead()) {
			for (Action action : this.doThink()) {
				ActionPipeline.getInstance().executeAction(action);
			}
		}
	}

	public List<Action> doThink() {
		return this.currentAIComponent.doThink();
	}

	public Creep getCreep() {
		return creep;
	}

	public void setCreep(Creep creep) {
		this.creep = creep;
		if (!GameObjects.getInstance().getAIManager().isRegistered(this)) {
			GameObjects.getInstance().getAIManager().register(this);
		}
	}

	public int getAreaOfDetection() {
		return areaOfInfluence;
	}

	public void setAreaOfInfluence(int areaOfInfluence) {
		this.areaOfInfluence = areaOfInfluence;
	}

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
		return positions;
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

	public void die() {
		GameObjects.getInstance().getAIManager().unregister(this);
	}
}
