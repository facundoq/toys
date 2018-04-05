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

import java.util.ArrayList;

/**
 * @author Facundo Manuel Quiroga Mar 10, 2009
 */
public class ProfilingStats {

	protected boolean printing;

	protected ArrayList<String> messages;
	protected long lastTime;
	protected long total;

	public ProfilingStats(boolean printing) {
		this.printing = printing;
	}

	public void startOfLoop() {
		lastTime = System.currentTimeMillis();
		messages = new ArrayList<String>();
		total = 0;
	}

	public void finishedSystem(String systemName) {
		long thisTime = System.currentTimeMillis();
		long elapsedTime = thisTime - lastTime;
		String message = systemName + " took " + elapsedTime + "ms";
		messages.add(message);
		lastTime = thisTime;
		total += elapsedTime;
	}

	public void endOfLoop() {
		if (this.isPrinting()) {
			System.out.println("Start of loop. Total time: " + total);
			for (String message : messages) {
				System.out.println(message);
			}
			System.out.println("End of loop. Total time: " + total);
		}
	}

	protected boolean isPrinting() {
		return this.printing;
	}

	protected void setPrinting(boolean printing) {
		this.printing = printing;
	}

}
