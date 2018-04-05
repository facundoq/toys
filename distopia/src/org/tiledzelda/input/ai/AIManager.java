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

import org.tiledzelda.main.GameObjects;

/**
 * Holds a reference to all AI's and makes them think every updateInterval ms
 * 
 * @author Facundo Manuel Quiroga Oct 19, 2008
 */

public class AIManager {
	public static int updateInterval = 100;

	private Collection<AI> ais;

	public AIManager() {
		this.setAIs(new ArrayList<AI>());
	}

	long time = 0;

	public void think() {
		//update only every updateInterval ms
		time = time + GameObjects.getInstance().getTimeKeeper().getElapsedTimeSinceLastUpdate();
		while (time > updateInterval) {
			time = time - updateInterval;
			this.doThink();
		}
	}

	public void doThink() {
		for (AI ai : new ArrayList<AI>(this.getAIs())) {
			ai.think();
		}
	}

	public void register(AI ai) {
		this.getAIs().add(ai);
	}

	public void unregister(AI ai) {
		this.getAIs().remove(ai);
	}

	public boolean isRegistered(AI ai) {
		return this.getAIs().contains(ai);
	}

	public Collection<AI> getAIs() {
		return ais;
	}

	public void setAIs(Collection<AI> ais) {
		this.ais = ais;
	}

}
