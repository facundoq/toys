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

package org.tiledzelda.model.elements.factories;

import org.tiledzelda.exceptions.InvalidPositionException;
import org.tiledzelda.exceptions.OcuppiedPositionException;
import org.tiledzelda.main.GameFactories;
import org.tiledzelda.model.elements.movableElements.MovableElementCharacteristics;
import org.tiledzelda.model.elements.movableElements.player.AttributeState;
import org.tiledzelda.model.elements.movableElements.player.HealthState;
import org.tiledzelda.model.elements.movableElements.player.Player;
import org.tiledzelda.model.elements.movableElements.player.PlayerElementType;
import org.tiledzelda.model.elements.movableElements.statistics.Statistic;
import org.tiledzelda.model.map.MapTeleporter;
import org.tiledzelda.model.map.TileMap;
import org.tiledzelda.model.map.maputils.Position;
import org.tiledzelda.visualization.graphics.element.movable.MovableElementGraphic;

/**
 * @author Facundo Manuel Quiroga Oct 22, 2008
 */
public class PlayerFactory extends MovableElementFactory {

	public PlayerFactory() {

	}

	public Player createPlayer(TileMap tileMap, Position position) throws InvalidPositionException, OcuppiedPositionException {

		MovableElementGraphic graphic = GameFactories.getInstance().getMovableElementGraphicFactory().getMovableElementGraphic("player");
		MovableElementCharacteristics characteristics = new MovableElementCharacteristics(40, 2, 70, 10, 5, 120, 50, 20, 200, 200);
		PlayerElementType playerElementType = new PlayerElementType("player", characteristics);
		Statistic maxHealth = characteristics.getStatisticNamed("MaxHealth");
		Statistic maxStamina = characteristics.getStatisticNamed("MaxStamina");
		Statistic healthRecoveryRate = characteristics.getStatisticNamed("HealthRecoveryRate");
		Statistic staminaRecoveryRate = characteristics.getStatisticNamed("StaminaRecoveryRate");
		AttributeState health = new AttributeState("health", 100, maxHealth, healthRecoveryRate);
		AttributeState stamina = new AttributeState("stamina", 40, maxStamina, staminaRecoveryRate);
		HealthState healthState = new HealthState(health, stamina);
		Player player = new Player(playerElementType, graphic, healthState);
		MapTeleporter.getInstance().teleportTo(player, tileMap, position);
		return player;
	}

}
