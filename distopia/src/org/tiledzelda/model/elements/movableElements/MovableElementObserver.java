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

package org.tiledzelda.model.elements.movableElements;

import org.tiledzelda.events.actions.AttackAction;
import org.tiledzelda.events.actions.results.ActionResultAttack;
import org.tiledzelda.model.map.maputils.Direction;

/**
 * @author Facundo Manuel Quiroga Oct 30, 2008
 */
public interface MovableElementObserver {

	public void changeDirection(Direction direction);

	public void stoppedMoving();

	public void move(Direction direction, int speed);

	public void die();

	public void attack(Direction direction);

	public void attackedFrom(Direction direction,ActionResultAttack result);

	public void update(long timeElapsed);

	public void finishedAttack(ActionResultAttack result);
}
