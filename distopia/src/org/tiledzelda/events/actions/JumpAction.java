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

import org.tiledzelda.events.actions.results.ActionResult;
import org.tiledzelda.events.actions.results.ActionResultNone;
import org.tiledzelda.model.elements.movableElements.MovableElement;
import org.tiledzelda.model.map.maputils.Direction;

/**
 * @author Facundo Manuel Quiroga 08/10/2008
 */
public class JumpAction extends Action {

	MovableElement target;
	Direction direction;

	public JumpAction(MovableElement target, Direction direction) {
		this.setTarget(target);
		this.setDirection(direction);
	}

	@Override
	public ActionResult execute() {
		this.getTarget().jump(this.getDirection());
		return new ActionResultNone();
	}

	public MovableElement getTarget() {
		return target;
	}

	public void setTarget(MovableElement target) {
		this.target = target;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public String toString() {
		return "Jump " + this.getTarget().toString() + " to " + this.getDirection().toString();
	}

}
