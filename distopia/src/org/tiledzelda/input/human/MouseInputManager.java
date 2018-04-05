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
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 * @author Facundo Manuel Quiroga Mar 16, 2009
 */
public class MouseInputManager implements MouseListener, MouseMotionListener, MouseWheelListener {

	public static final Cursor INVISIBLE_CURSOR = Toolkit.getDefaultToolkit().createCustomCursor(Toolkit.getDefaultToolkit().getImage(""), new Point(0, 0), "invisible");

	// mouse codes
	protected static final int MOUSE_MOVE_LEFT = 0;

	protected static final int MOUSE_MOVE_RIGHT = 1;

	protected static final int MOUSE_MOVE_UP = 2;

	protected static final int MOUSE_MOVE_DOWN = 3;

	protected static final int MOUSE_WHEEL_UP = 4;

	protected static final int MOUSE_WHEEL_DOWN = 5;

	protected static final int MOUSE_BUTTON_1 = 6;

	protected static final int MOUSE_BUTTON_2 = 7;

	protected static final int MOUSE_BUTTON_3 = 8;

	protected static final int NUM_MOUSE_CODES = 9;

	protected MouseInput[] mouseActions;

	protected Point mouseLocation;

	protected Component component;

	protected MouseInput leftButton;
	protected MouseInput rightButton;
	protected MouseInput middleButton;

	public MouseInputManager(Component component) {
		this.component = component;
		mouseLocation = new Point();
		mouseActions = new MouseInput[NUM_MOUSE_CODES];
		// register key and mouse listeners
		component.addMouseListener(this);
		component.addMouseMotionListener(this);
		component.addMouseWheelListener(this);

		// allow input of the TAB key and other keys normally
		// used for focus traversal
		component.setFocusTraversalKeysEnabled(false);
	}

	/**
	 * Gets the name of a mouse code.
	 */
	public static String getMouseName(int mouseCode) {
		switch (mouseCode) {
			case MOUSE_MOVE_LEFT:
				return "Mouse Left";
			case MOUSE_MOVE_RIGHT:
				return "Mouse Right";
			case MOUSE_MOVE_UP:
				return "Mouse Up";
			case MOUSE_MOVE_DOWN:
				return "Mouse Down";
			case MOUSE_WHEEL_UP:
				return "Mouse Wheel Up";
			case MOUSE_WHEEL_DOWN:
				return "Mouse Wheel Down";
			case MOUSE_BUTTON_1:
				return "Mouse Button 1";
			case MOUSE_BUTTON_2:
				return "Mouse Button 2";
			case MOUSE_BUTTON_3:
				return "Mouse Button 3";
			default:
				return "Unknown mouse code " + mouseCode;
		}
	}

	/**
	 * Gets the x position of the mouse.
	 */
	public int getMouseX() {
		return mouseLocation.x;
	}

	/**
	 * Gets the y position of the mouse.
	 */
	public int getMouseY() {
		return mouseLocation.y;
	}

	/**
	 * Gets the mouse code for the button specified in this MouseEvent.
	 */
	public static int getMouseButtonCode(MouseEvent e) {
		switch (e.getButton()) {
			case MouseEvent.BUTTON1:
				return MOUSE_BUTTON_1;
			case MouseEvent.BUTTON2:
				return MOUSE_BUTTON_2;
			case MouseEvent.BUTTON3:
				return MOUSE_BUTTON_3;
			default:
				return -1;
		}
	}

	protected MouseInput getMouseButtonAction(MouseEvent e) {
		int mouseCode = getMouseButtonCode(e);
		if (mouseCode != -1) {
			return mouseActions[mouseCode];
		} else {
			return null;
		}
	}

	//from the MouseListener interface
	public void mousePressed(MouseEvent e) {
	}

	// from the MouseListener interface
	public void mouseReleased(MouseEvent e) {

	}

	// from the MouseListener interface
	public void mouseClicked(MouseEvent e) {

		Point point = e.getPoint();
		if (e.getButton() == MouseEvent.BUTTON1) {
			this.getLeftButton().pressed(point);
		} else if (e.getButton() == MouseEvent.BUTTON2) {
			this.getRightButton().pressed(point);
		} else if (e.getButton() == MouseEvent.BUTTON3) {
			this.getMiddleButton().pressed(point);
		}
		e.consume();

	}

	// from the MouseListener interface
	public void mouseEntered(MouseEvent e) {
		mouseMoved(e);
	}

	// from the MouseListener interface
	public void mouseExited(MouseEvent e) {
		mouseMoved(e);
	}

	// from the MouseMotionListener interface
	public void mouseDragged(MouseEvent e) {
		mouseMoved(e);
	}

	// from the MouseWheelListener interface
	public void mouseWheelMoved(MouseWheelEvent e) {
		// mouseHelper(MOUSE_WHEEL_UP, MOUSE_WHEEL_DOWN, e.getWheelRotation());
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	public void mouseMoved(MouseEvent e) {
		mouseLocation.setLocation(e.getX(), e.getY());
		e.consume();
	}

	protected MouseInput getLeftButton() {
		return this.leftButton;
	}

	protected void setLeftButton(MouseInput leftButton) {
		this.leftButton = leftButton;
	}

	protected MouseInput getRightButton() {
		return this.rightButton;
	}

	protected void setRightButton(MouseInput rightButton) {
		this.rightButton = rightButton;
	}

	protected MouseInput getMiddleButton() {
		return this.middleButton;
	}

	protected void setMiddleButton(MouseInput middleButton) {
		this.middleButton = middleButton;
	}

}
