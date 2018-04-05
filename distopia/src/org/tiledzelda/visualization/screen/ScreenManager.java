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

package org.tiledzelda.visualization.screen;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Graphics2D;
import java.awt.Panel;

/**
 * @author Facundo Manuel Quiroga Nov 16, 2008
 */
public abstract class ScreenManager {

	/**
	 * 
	 */
	public ScreenManager() {
		super();
	}

	public abstract void setDimension(Dimension d);

	public abstract Dimension getDimension();

	public abstract void restoreScreen();

	public abstract void update();

	public abstract Graphics2D getGraphics();

	public abstract Component getRenderingSurface();

	public abstract DisplayMode getCurrentDisplayMode();

	public abstract Panel getSidePanel();

}