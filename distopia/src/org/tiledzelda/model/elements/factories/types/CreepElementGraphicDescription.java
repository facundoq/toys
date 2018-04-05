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

package org.tiledzelda.model.elements.factories.types;

import org.tiledzelda.main.GameFactories;
import org.tiledzelda.main.resourcemanagement.graphics.MovableElementGraphicFactory;
import org.tiledzelda.visualization.graphics.element.movable.MovableElementGraphic;

/**
 * @author Facundo Manuel Quiroga Mar 15, 2009
 */
public class CreepElementGraphicDescription {

	protected String graphic;
	protected boolean graphicIsSimple;

	public CreepElementGraphicDescription(String graphic, boolean graphicIsSimple) {
		super();
		this.graphic = graphic;
		this.graphicIsSimple = graphicIsSimple;
	}

	protected MovableElementGraphic createMovableElementGraphic() {
		MovableElementGraphic movableElementGraphic;
		MovableElementGraphicFactory movableElementFactory = GameFactories.getInstance().getMovableElementGraphicFactory();
		if (graphicIsSimple) {
			movableElementGraphic = movableElementFactory.getSimpleMovableElementGraphic(this.graphic);
		} else {
			movableElementGraphic = movableElementFactory.getMovableElementGraphic(this.graphic);
		}
		return movableElementGraphic;
	}

	public String getGraphic() {
		return this.graphic;
	}

	protected void setGraphic(String graphic) {
		this.graphic = graphic;
	}

	public boolean isGraphicIsSimple() {
		return this.graphicIsSimple;
	}

	protected void setGraphicIsSimple(boolean graphicIsSimple) {
		this.graphicIsSimple = graphicIsSimple;
	}
}
