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

package org.tiledzelda.model.elements.staticElements;

import org.tiledzelda.events.actions.results.ActionResultAttack;
import org.tiledzelda.model.elements.Element;
import org.tiledzelda.model.map.maputils.Direction;
import org.tiledzelda.visualization.graphics.element.StaticElementGraphic;

/**
 * @author Facundo Manuel Quiroga 08/4/2008
 */
public class StaticElement extends Element {

	private StaticElementGraphic graphic;

	protected StaticElementType elementType;

	public StaticElement(StaticElementType elementType, StaticElementGraphic graphic) {
		super();
		this.setElementType(elementType);
		this.setGraphic(graphic);
	}

	public StaticElementGraphic getGraphic() {
		return this.graphic;
	}

	public void setGraphic(StaticElementGraphic graphic) {
		this.graphic = graphic;
	}

	public void update(long elapsedTime) {
		this.getGraphic().update(elapsedTime);
	}

	@Override
	public ActionResultAttack attack(Direction direction) {
		// TODO ¿How do they attack?
		return ActionResultAttack.notExecuted;
	}

	@Override
	public ActionResultAttack defendFromAttackBy(Element attacker, int attackStrength) {
		// TODO By default they do nothing.. ¿pluggable behaviour?
		return ActionResultAttack.cannotBeAttacked;
	}

	public StaticElementType getElementType() {
		return this.elementType;
	}

	protected void setElementType(StaticElementType elementType) {
		this.elementType = elementType;
	}

}
