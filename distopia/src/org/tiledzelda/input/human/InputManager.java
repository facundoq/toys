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

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * The InputManager manages input of key and mouse events. Events are mapped to GameActions.
 */
public class InputManager implements KeyListener {
	/**
	 * An invisible cursor.
	 */

	// key codes are defined in java.awt.KeyEvent.
	// most of the codes (except for some rare ones like
	// "alt graph") are less than 600.
	private static final int NUM_KEY_CODES = 600;

	private GameInput[] keyActions = new GameInput[NUM_KEY_CODES];

	private Component comp;

	/**
	 * Creates a new InputManager that listens to input from the specified component.
	 */
	public InputManager(Component component) {
		this.comp = component;

		// register key  listener
		component.addKeyListener(this);

		// allow input of the TAB key and other keys normally
		// used for focus traversal
		component.setFocusTraversalKeysEnabled(false);
	}

	/**
	 * Sets the cursor on this InputManager's input component.
	 */
	public void setCursor(Cursor cursor) {
		comp.setCursor(cursor);
	}

	/**
	 * Maps a GameAction to a specific key. The key codes are defined in java.awt.KeyEvent. If the key already has a GameAction mapped to it, the new GameAction overwrites it.
	 */
	public void mapToKey(GameInput gameInput, int keyCode) {
		keyActions[keyCode] = gameInput;
	}

	/**
	 * Clears all mapped keys and mouse actions to this GameAction.
	 */
	public void clearMap(GameInput gameInput) {
		for (int i = 0; i < keyActions.length; i++) {
			if (keyActions[i] == gameInput) {
				keyActions[i] = null;
			}
		}

		gameInput.reset();
	}

	/**
	 * Gets a List of names of the keys and mouse actions mapped to this GameAction. Each entry in the List is a String.
	 */
	public List<String> getMaps(GameInput gameCode) {
		ArrayList<String> list = new ArrayList<String>();

		for (int i = 0; i < keyActions.length; i++) {
			if (keyActions[i] == gameCode) {
				list.add(getKeyName(i));
			}
		}
		return list;
	}

	/**
	 * Resets all GameActions so they appear like they haven't been pressed.
	 */
	public void resetAllGameActions() {
		for (int i = 0; i < keyActions.length; i++) {
			if (keyActions[i] != null) {
				keyActions[i].reset();
			}
		}
	}

	/**
	 * Gets the name of a key code.
	 */
	public static String getKeyName(int keyCode) {
		return KeyEvent.getKeyText(keyCode);
	}

	private GameInput getKeyAction(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode < keyActions.length) {
			return keyActions[keyCode];
		} else {
			return null;
		}
	}

	// from the KeyListener interface
	public void keyPressed(KeyEvent e) {
		GameInput gameInput = getKeyAction(e);
		if (gameInput != null) {
			gameInput.press();
		}
		// make sure the key isn't processed for anything else
		e.consume();
	}

	// from the KeyListener interface
	public void keyReleased(KeyEvent e) {
		GameInput gameInput = getKeyAction(e);
		if (gameInput != null) {
			gameInput.release();
		}
		// make sure the key isn't processed for anything else
		e.consume();
	}

	// from the KeyListener interface
	public void keyTyped(KeyEvent e) {
		// make sure the key isn't processed for anything else
		e.consume();
	}

	/*
	 private void mouseHelper(int codeNeg, int codePos,
	 int amount)
	 {
	 GameAction gameAction;
	 if (amount < 0) {
	 gameAction = mouseActions[codeNeg];
	 }
	 else {
	 gameAction = mouseActions[codePos];
	 }
	 if (gameAction != null) {
	 gameAction.press(Math.abs(amount));
	 gameAction.release();
	 }
	 }

	 */
	public void mouseMoved(MouseEvent e) {
		// TODO implement mouse input

	}

}
