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

package org.tiledzelda.input.human;

import java.awt.Point;

import org.tiledzelda.main.resourcemanagement.Configuration;
import org.tiledzelda.model.elements.movableElements.player.Player;
import org.tiledzelda.model.map.maputils.Position;

/**
 * @author Facundo Manuel Quiroga Mar 16, 2009
 */
public class ScreenPointToPositionTranslator {

	public Position translate(Player player, int x, int y) {
		int numberOfBlocks = Configuration.getInstance().getValueAsInt("numberOfBlocks");
		int blockSize = Configuration.getInstance().getValueAsInt("blockSize");
		int screenRadius = numberOfBlocks / 2;
		// obtain the positions relative to the player 
		int relativePositionX = (x / blockSize) - screenRadius;
		int relativePositionY = (y / blockSize) - screenRadius;
		// obtain the absolute positions
		int positionX = player.getPosition().getX() + relativePositionX;
		int positionY = player.getPosition().getY() + relativePositionY;

		return Position.newPosition(positionX, positionY);
	}

	/**
	 * @param player
	 * @param point
	 * @return
	 */
	public Position translate(Player player, Point point) {
		return this.translate(player, point.x, point.y);
	}

}
