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

package org.tiledzelda.input.human;

/**
    
*/
public class GameInput {

	/**
	 * A GameInput is used to listen to a key event. The InputManager takes care of changing it's state accordingly. The isPressed() method returns true only after the key is first pressed, and not
	 * again until the key is released and pressed again.
	 */

	private static final int STATE_RELEASED = 0;
	private static final int STATE_PRESSED = 1;

	private int state;

	/**
	 * Create a new GameAction .
	 */
	public GameInput() {
		reset();
	}

	/**
	 * Resets this GameAction so that it appears like it hasn't been pressed.
	 */
	public void reset() {
		state = STATE_RELEASED;
	}

	/**
	 * Signals that the key was pressed.
	 */
	public void press() {
		state = STATE_PRESSED;
	}

	/**
	 * Signals that the key was released
	 */
	public void release() {
		state = STATE_RELEASED;
	}

	/**
	 * Returns whether the key was pressed or not since last checked.
	 */
	public boolean isPressed() {
		return (state == STATE_PRESSED);
	}

}
