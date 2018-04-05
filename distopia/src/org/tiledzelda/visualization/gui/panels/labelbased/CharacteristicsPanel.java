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

/**
 * @author Facundo Manuel Quiroga Jul 22, 2009
 */
public abstract class CharacteristicsPanel extends ObjectsListPanel {

	protected MovableElement movableElement;

	public CharacteristicsPanel(String title, MovableElement movableElement) {
		super(title);
		this.setMovableElement(movableElement);
		this.setUsedHeight(150);
	}

	/**
	 * @return the movableElement
	 */
	public MovableElement getMovableElement() {
		return this.movableElement;
	}

	/**
	 * @param movableElement the movableElement to set
	 */
	public void setMovableElement(MovableElement movableElement) {
		this.movableElement = movableElement;
	}

	public abstract ArrayList<Characteristic> getCharacteristics();

	/* (non-Javadoc)
	 * @see org.tiledzelda.visualization.gui.ObjectsListPanel#getObjects()
	 */
	@Override
	public ArrayList<Object> getObjects() {
		ArrayList<Characteristic> characteristics = this.getCharacteristics();
		return new ArrayList<Object>(characteristics);
	}

	/* (non-Javadoc)
	 * @see org.tiledzelda.visualization.gui.ObjectsListPanel#printObjectLabel(java.lang.Object)
	 */
	@Override
	public String printObjectLabel(Object object) {
		Characteristic characteristic = (Characteristic) object;
		return formatLabel(characteristic);
	}

	public String formatLabel(Characteristic characteristic) {
		int level = characteristic.getCurrentLevel();
		int currentExp = characteristic.getExperiencePointsOfCurrentLevel();
		int requiredExp = characteristic.getExperiencePointsRequiredForCurrentLevel();
		String label = characteristic.getName() + ": " + level + " (" + currentExp + "/" + requiredExp + ")";
		return label;
	}

}
