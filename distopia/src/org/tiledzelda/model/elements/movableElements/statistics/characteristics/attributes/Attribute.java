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

package org.tiledzelda.model.elements.movableElements.statistics.characteristics.attributes;

import java.util.List;

import org.tiledzelda.model.elements.movableElements.statistics.characteristics.Characteristic;

/**
 * An general Characteristic of an Element, like Strength, Intelligence, etc.
 * 
 * @author Facundo Manuel Quiroga Nov 20, 2008
 */
public class Attribute extends Characteristic {

	public Attribute(String name, int experiencePointsModifierPerUse, List<Integer> experiencePointsRequiredPerLevel) {
		super(name, experiencePointsModifierPerUse, experiencePointsRequiredPerLevel);
	}

	public Attribute(String name, List<Integer> experiencePointsRequiredPerLevel) {
		this(name, 1, experiencePointsRequiredPerLevel);
	}

}
