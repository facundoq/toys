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

package org.tiledzelda.visualization.gui.panels.labelbased;

import java.util.ArrayList;

import org.tiledzelda.model.elements.movableElements.MovableElement;
import org.tiledzelda.model.elements.movableElements.statistics.characteristics.Characteristic;
import org.tiledzelda.model.elements.movableElements.statistics.characteristics.attributes.Attribute;

/**
 * An AWT Panel that shows the attributes of a MovableElement
 * 
 * @author Facundo Manuel Quiroga Jul 22, 2009
 */

public class AttributesPanel extends CharacteristicsPanel {

	public AttributesPanel(MovableElement movableElement) {
		super("Attributes:", movableElement);
		this.setUsedHeight(200);
	}

	@Override
	public ArrayList<Characteristic> getCharacteristics() {
		try {
			ArrayList<Attribute> attributes = this.getMovableElement().getElementType().getCharacteristics().getAttributes();
			return new ArrayList<Characteristic>(attributes);

		} catch (NullPointerException e) {

		}
		return new ArrayList<Characteristic>();
	}

}
