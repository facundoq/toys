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

package org.tiledzelda.main.resourcemanagement;

import org.tiledzelda.exceptions.InvalidPositionException;
import org.tiledzelda.exceptions.OcuppiedPositionException;
import org.tiledzelda.main.GameFactories;
import org.tiledzelda.model.elements.staticElements.StaticElement;
import org.tiledzelda.model.elements.staticElements.StaticElementType;
import org.tiledzelda.model.map.MapTeleporter;
import org.tiledzelda.model.map.TileMap;
import org.tiledzelda.model.map.maputils.Position;
import org.tiledzelda.visualization.graphics.animations.Animation;
import org.tiledzelda.visualization.graphics.element.StaticElementGraphic;

/**
 * @author Facundo Manuel Quiroga Feb 10, 2009
 */
public class StaticElementFactory {

	public StaticElementFactory() {

	}

	public StaticElement newStaticElement(String name) {
		Animation animation = GameFactories.getInstance().getAnimationFactory().getAnimation(name);
		StaticElementGraphic graphic = new StaticElementGraphic(animation);
		return new StaticElement(new StaticElementType(name), graphic);
	}

	public StaticElement newStaticElement(String name, TileMap tileMap, Position position) throws InvalidPositionException, OcuppiedPositionException {
		StaticElement staticElement = this.newStaticElement(name);
		MapTeleporter.getInstance().teleportTo(staticElement, tileMap, position);
		return staticElement;
	}

}
