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

package org.tiledzelda.visualization.gui.panels.listbased;

import java.util.ArrayList;

import org.tiledzelda.model.elements.movableElements.MovableElement;
import org.tiledzelda.model.elements.movableElements.statistics.characteristics.attributes.Attribute;

/**
 * @author Facundo Manuel Quiroga Jul 25, 2009
 */
public class AWTAttributesPanel extends AWTCharacteristicsPanel {

	/**
	 * @param title
	 * @param movableElement
	 */
	public AWTAttributesPanel(MovableElement movableElement) {
		super("Attributes:", movableElement);
	}

	@Override
	public ArrayList<Object> getObjects() {
		try {
			ArrayList<Attribute> attributes = this.getMovableElement().getElementType().getCharacteristics().getAttributes();
			return new ArrayList<Object>(attributes);

		} catch (NullPointerException e) {

		}
		return new ArrayList<Object>();
	}

}
