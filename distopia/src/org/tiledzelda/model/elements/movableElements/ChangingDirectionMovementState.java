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

import org.tiledzelda.model.map.maputils.Direction;

/**
 * @author Facundo Manuel Quiroga 07/10/2008
 */
public class ChangingDirectionMovementState extends MovementState {

	protected long changingDirectionBlockedTime;

	public ChangingDirectionMovementState(MovableElement movableElement, int changingDirectionBlockedTime) {
		super(movableElement);
		this.setChangingDirectionBlockedTime(changingDirectionBlockedTime);
	}

	/* (non-Javadoc)
	 * @see elements.MovementState#move(map.Direction, int)
	 */
	@Override
	public void move(Direction direction, int speed) {

	}

	/* (non-Javadoc)
	 * @see elements.MovementState#update(long)
	 */
	@Override
	public void update(long timeElapsed) {
		/*
		* it's 10 and not 0 to make it 0 on average, because you cant depend on
		* getting here every ms, so most of the time you caught it on -5, -10,
		* -20 and so on. the value 10 was obtained empirically; of course, it
		* would depend on the machine the game is played
		*/
		long newBlockedTime = this.getChangingDirectionBlockedTime() - timeElapsed;
		if (newBlockedTime <= 10) {
			this.getMovableElement().setMovementState(new IdleMovementState(this.getMovableElement()));
		} else {
			this.setChangingDirectionBlockedTime(newBlockedTime);
		}
	}

	public long getChangingDirectionBlockedTime() {
		return this.changingDirectionBlockedTime;
	}

	public void setChangingDirectionBlockedTime(long changingDirectionBlockedTime) {
		this.changingDirectionBlockedTime = changingDirectionBlockedTime;
	}

	@Override
	public boolean isIdle() {
		return false;
	}

}
