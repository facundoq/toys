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

package org.tiledzelda.main.logic;

import org.tiledzelda.main.GameObjects;

/**
 * @author Facundo Manuel Quiroga Nov 19, 2008
 */
public class FPSCounter {

	protected int fps;

	protected int lastFps;

	protected long second;

	protected long currentTime;

	protected long elapsedTimeFromStart;

	public FPSCounter() {
		fps = 0;
		lastFps = 0;
		second = 0;
		elapsedTimeFromStart = 0;
		currentTime = GameObjects.getInstance().getTimeKeeper().getElapsedTimeSinceStart();
	}

	public void count() {
		long now = GameObjects.getInstance().getTimeKeeper().getElapsedTimeSinceStart();
		elapsedTimeFromStart += now - currentTime;
		currentTime = now;

		long currentSecond = elapsedTimeFromStart / 1000;
		if (currentSecond > second) {
			second = currentSecond;
			lastFps = fps;
			fps = 0;
		}
		fps++;
	}

	public int getFPS() {
		return this.lastFps;
	}
}
