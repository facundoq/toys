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

package org.tiledzelda.model.entities;

import org.tiledzelda.events.actions.results.ActionResultAttack;
import org.tiledzelda.exceptions.GameError;
import org.tiledzelda.main.GameObjects;
import org.tiledzelda.model.elements.Element;
import org.tiledzelda.model.map.maputils.Direction;
import org.tiledzelda.visualization.graphics.element.ElementGraphic;
import org.tiledzelda.visualization.graphics.element.NullElementGraphic;

/**
 * Flyweight/Null Pattern, represents the fact that there is no element in a map position Should makes things easier
 * 
 * @author Facundo Manuel Quiroga Oct 17, 2008
 */
public class NullElement extends Element {

	private static NullElement instance = null;

	public static NullElement getInstance() {
		if (NullElement.instance == null) {
			NullElement.instance = new NullElement();
		}
		return NullElement.instance;
	}

	protected NullElementType nullElementType;

	public NullElement() {
		super();
		this.setNullElementType(nullElementType);
		GameObjects.getInstance().getLimboTileMap().removeElement(this);
		this.nullElementGraphic = new NullElementGraphic();
	}

	private NullElementGraphic nullElementGraphic;

	public ActionResultAttack defendFromAttackBy(Element attacker, int attackStrength) {
		//TODO do nothing or throw exception?
		return ActionResultAttack.cannotBeAttacked;
	}

	@Override
	public ElementGraphic getGraphic() {
		return this.nullElementGraphic;
	}

	@Override
	public void update(long timeElapsed) {
		// do nothing!
	}

	@Override
	public ActionResultAttack attack(Direction direction) {
		throw new GameError("Null elements cannot attack");
	}

	public NullElementType getElementType() {
		return this.nullElementType;
	}

	protected void setNullElementType(NullElementType nullElementType) {
		this.nullElementType = nullElementType;
	}
}
