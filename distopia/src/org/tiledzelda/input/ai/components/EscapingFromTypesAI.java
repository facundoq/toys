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
import org.tiledzelda.events.actions.WalkAction;
import org.tiledzelda.input.ai.NoPossibleMovementException;
import org.tiledzelda.main.GameObjects;
import org.tiledzelda.main.logic.RandomNumberGenerator;
import org.tiledzelda.model.elements.Element;
import org.tiledzelda.model.elements.movableElements.creeps.Creep;
import org.tiledzelda.model.map.TileMap;
import org.tiledzelda.model.map.maputils.Direction;
import org.tiledzelda.model.map.maputils.Position;

public class EscapingFromTypesAI extends ReactiveToTypesAI {

	public EscapingFromTypesAI(Collection<String> typeNames) {
		super(typeNames);
	}

	@Override
	protected List<Action> reactTo(Element element) {
		List<Action> actions = new ArrayList<Action>();
		try {
			Direction direction = this.determineNextDirectionForEscaping(element);
			actions.add(new WalkAction(this.getCreep(), direction));
		} catch (NoPossibleMovementException e) {
			// TODO cant move, do sth else
		}
		return actions;
	}

	protected Direction determineNextDirectionForEscaping(Element element) throws NoPossibleMovementException {
		Collection<Direction> possibleDirections = this.validDirectionsForMoving();
		if (possibleDirections.isEmpty()) {
			throw new NoPossibleMovementException("can't move in any direction");
		}

		Direction nextDirection = possibleDirections.iterator().next();
		Creep creep = this.getCreep();
		TileMap creepsMap = creep.getCurrentTileMap();

		//get the best directions for escaping the element, the opposite ones 
		Position creepsPosition = creep.getPosition();
		Direction directionToGoToElement = creepsPosition.directionToGoTo(element.getPosition());
		List<Direction> bestPossibleDirections = directionToGoToElement.oppositeDirections();

		// remove the directions that make me move to an invalid or occupied position
		Iterator<Direction> i = bestPossibleDirections.iterator();
		while (i.hasNext()) {
			Direction aDirection = i.next();
			Position nextPosition = creepsPosition.positionAfterMovingIn(aDirection);
			if (!creepsMap.validPosition(nextPosition) || !creepsMap.occupable(nextPosition)) {
				i.remove();
			}
		}

		// Take the chance to remove the first best directions so as not to always move in the same way
		RandomNumberGenerator randomNumberGenerator = GameObjects.getInstance().getRandomNumberGenerator();
		int randomNumber = randomNumberGenerator.randomNumber();
		while (randomNumber > 70 && !bestPossibleDirections.isEmpty()) {
			bestPossibleDirections.remove(0);
			randomNumber = randomNumberGenerator.randomNumber();
		}
		// if after the culling i still have a direction
		if (!bestPossibleDirections.isEmpty()) {
			nextDirection = bestPossibleDirections.iterator().next();
		}
		// else take any direction	
		return nextDirection;
	}

}
