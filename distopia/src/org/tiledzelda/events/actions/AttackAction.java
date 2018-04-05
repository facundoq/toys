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

package org.tiledzelda.events.actions;

import org.tiledzelda.events.actions.results.ActionResultAttack;
import org.tiledzelda.model.elements.Element;
import org.tiledzelda.model.map.maputils.Direction;

/**
 * @author Facundo Manuel Quiroga 13/10/2008
 */
public class AttackAction extends Action {
	private Element attacker;
	private Direction direction;

	public AttackAction(Element attacker, Direction direction) {
		this.setAttacker(attacker);
		this.setDirection(direction);
	}

	/* (non-Javadoc)
	 * @see actions.Action#execute()
	 */
	@Override
	public ActionResultAttack execute() {
		return this.getAttacker().attack(this.getDirection());
	}

	public Element getAttacker() {
		return this.attacker;
	}

	public void setAttacker(Element attacker) {
		this.attacker = attacker;
	}

	public Direction getDirection() {
		return this.direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

}
