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

import java.awt.Point;
import java.awt.event.KeyEvent;

import org.tiledzelda.events.actionProcessing.ActionPipeline;
import org.tiledzelda.events.actions.Action;
import org.tiledzelda.events.actions.AttackAction;
import org.tiledzelda.events.actions.JumpAction;
import org.tiledzelda.events.actions.RunAction;
import org.tiledzelda.events.actions.SneakAction;
import org.tiledzelda.events.actions.WalkAction;
import org.tiledzelda.main.GameCore;
import org.tiledzelda.model.elements.movableElements.player.Player;
import org.tiledzelda.model.map.maputils.Direction;
import org.tiledzelda.model.map.maputils.Position;

/**
 * Generic input adapter, gets key/mouse inputs and transforms them into messages to domain objects
 */
//TODO In the future, must partition into several different input adapters, and figure out where to configure it
public class InputAdapter {

	protected GameCore gameCore;

	protected Player player;

	protected GameInput moveLeft;

	protected GameInput moveRight;

	protected GameInput moveUp;

	protected GameInput moveDown;

	protected GameInput escape;

	protected GameInput run;

	protected GameInput sneak;

	protected GameInput jump;

	protected InputManager inputManager;
	protected MouseInputManager mouseInputManager;

	protected GameInput attack;

	protected MouseInput click;
	protected MouseInput click2;
	protected MouseInput click3;

	public InputAdapter(GameCore gameCore, Player player) {

		this.setGameCore(gameCore);
		this.setPlayer(player);

		moveLeft = new GameInput();
		moveRight = new GameInput();
		moveUp = new GameInput();
		moveDown = new GameInput();
		escape = new GameInput();
		run = new GameInput();
		sneak = new GameInput();
		jump = new GameInput();
		attack = new GameInput();

		inputManager = new InputManager(gameCore.getScreen().getRenderingSurface());

		//inputManager.setCursor(InputManager.INVISIBLE_CURSOR);

		inputManager.mapToKey(moveLeft, KeyEvent.VK_LEFT);
		inputManager.mapToKey(moveRight, KeyEvent.VK_RIGHT);
		inputManager.mapToKey(moveUp, KeyEvent.VK_UP);
		inputManager.mapToKey(moveDown, KeyEvent.VK_DOWN);
		inputManager.mapToKey(escape, KeyEvent.VK_ESCAPE);
		inputManager.mapToKey(run, KeyEvent.VK_V);
		inputManager.mapToKey(sneak, KeyEvent.VK_SHIFT);
		inputManager.mapToKey(jump, KeyEvent.VK_SPACE);
		inputManager.mapToKey(attack, KeyEvent.VK_CONTROL);

		mouseInputManager = new MouseInputManager(gameCore.getScreen().getRenderingSurface());
		this.click = new MouseInput();
		this.click2 = new MouseInput();
		this.click3 = new MouseInput();

		mouseInputManager.setLeftButton(click);
		mouseInputManager.setRightButton(click2);
		mouseInputManager.setMiddleButton(click3);

	}

	public void checkInput(long elapsedTime) {

		int horizontalDirection = 0;
		int verticalDirection = 0;
		if (moveDown.isPressed()) {
			verticalDirection++;
		}
		if (moveLeft.isPressed()) {
			horizontalDirection--;
		}
		if (moveRight.isPressed()) {
			horizontalDirection++;
		}
		if (moveUp.isPressed()) {
			verticalDirection--;
		}
		if (!(verticalDirection == 0 && horizontalDirection == 0)) {

			Direction direction = Direction.newDirection(horizontalDirection, verticalDirection);
			Action action;
			if (run.isPressed()) {
				action = new RunAction(this.getPlayer(), direction);
			} else if (sneak.isPressed()) {
				action = new SneakAction(this.getPlayer(), direction);
			} else if (jump.isPressed()) {
				action = new JumpAction(this.getPlayer(), direction);
			} else {
				action = new WalkAction(this.getPlayer(), direction);
			}
			ActionPipeline.getInstance().executeAction(action);

		}

		if (escape.isPressed()) {
			this.getGameCore().stop();
		}
		if (attack.isPressed()) {
			ActionPipeline.getInstance().executeAction(new AttackAction(this.getPlayer(), this.getPlayer().getDirection()));
		}

		Point point;
		if (this.click.checkPressed()) {
			point = this.click.getPoint();
			Position position = new ScreenPointToPositionTranslator().translate(GameCore.getInstance().getPlayer(), point);
			System.out.println("Click at " + position);
		} else if (this.click2.checkPressed()) {
			point = this.click2.getPoint();
			Position position = new ScreenPointToPositionTranslator().translate(GameCore.getInstance().getPlayer(), point);
			System.out.println("Click2 at " + position);
		} else if (this.click3.checkPressed()) {
			point = this.click3.getPoint();
			Position position = new ScreenPointToPositionTranslator().translate(GameCore.getInstance().getPlayer(), point);
			System.out.println("Click3 at " + position);
		}

	}

	private GameCore getGameCore() {
		return gameCore;
	}

	private void setGameCore(GameCore gameCore) {
		this.gameCore = gameCore;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

}
