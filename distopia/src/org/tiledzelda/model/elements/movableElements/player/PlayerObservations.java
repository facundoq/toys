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

package org.tiledzelda.model.elements.movableElements.player;

import java.util.ArrayList;

import org.tiledzelda.model.elements.Element;
import org.tiledzelda.model.map.TileMap;
import org.tiledzelda.model.map.maputils.Position;

/**
 * @author Facundo Manuel Quiroga Mar 16, 2009
 */
public class PlayerObservations {

	protected Player player;
	protected ArrayList<String> observations;

	public PlayerObservations(Player player) {
		this.setPlayer(player);
		this.setObservations(new ArrayList<String>());
	}

	protected Player getPlayer() {
		return this.player;
	}

	protected void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * @param position
	 */
	public void lookAt(Position position) {
		TileMap tileMap = this.getPlayer().getCurrentTileMap();
		if (tileMap.validPosition(position)) {
			if (tileMap.existsElementAt(position)) {
				Element element = tileMap.elementAt(position);
				String observation = "" + element.getElementType().getName() + " at position " + position.getX() + "," + position.getY();
				this.getObservations().add(observation);
			}
		}

	}

	protected ArrayList<String> getObservations() {
		return this.observations;
	}

	protected void setObservations(ArrayList<String> observations) {
		this.observations = observations;
	}

}
