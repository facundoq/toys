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

package org.tiledzelda.input.ai.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.tiledzelda.events.actions.Action;
import org.tiledzelda.main.GameObjects;
import org.tiledzelda.model.elements.Element;
import org.tiledzelda.model.elements.movableElements.creeps.Creep;
import org.tiledzelda.model.map.maputils.Area;

/**
 * @author Facundo Manuel Quiroga 05/10/2008
 */
public abstract class ReactiveToTypesAI extends AIComponent {
	protected Collection<String> typeNames;

	public ReactiveToTypesAI(Collection<String> typeNames) {
		super();
		this.setTypeNames(new ArrayList<String>(typeNames));
	}

	/* (non-Javadoc)
	 * @see ai.AI#doThink()
	 */
	@Override
	public List<Action> doThink() {
		Creep creep = this.getCreep();
		List<Action> actions = new ArrayList<Action>();
		if (!creep.isMoving()) {
			Area areaOfDetection = Area.newAreaAround(creep.getPosition(), this.getAreaOfDetection());
			Collection<Element> elements = creep.getCurrentMap().elementsIn(areaOfDetection);
			elements.remove(creep);

			ArrayList<Element> possibleTargets = new ArrayList<Element>();
			for (Element element : elements) {
				if (this.getTypeNames().contains(element.getElementType().getName())) {
					possibleTargets.add(element);
				}
			}

			if (possibleTargets.size() > 0) {
				int selectedElementIndex = GameObjects.getInstance().getRandomNumberGenerator().randomNumber() % possibleTargets.size();
				Element selectedElement = possibleTargets.get(selectedElementIndex);
				actions.addAll(this.reactTo(selectedElement));
			}
		}
		return actions;
	}

	protected abstract List<Action> reactTo(Element selectedElement);

	protected Collection<String> getTypeNames() {
		return this.typeNames;
	}

	protected void setTypeNames(Collection<String> types) {
		this.typeNames = types;
	}
}
