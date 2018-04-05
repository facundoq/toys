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

package org.tiledzelda.visualization.graphics.element.movable;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Iterator;

import org.tiledzelda.events.actions.results.ActionResultAttack;
import org.tiledzelda.main.GameObjects;
import org.tiledzelda.main.Main;
import org.tiledzelda.model.elements.movableElements.MovableElement;
import org.tiledzelda.model.elements.movableElements.MovableElementObserver;
import org.tiledzelda.model.elements.movableElements.player.AttributeState;
import org.tiledzelda.model.elements.movableElements.player.HealthState;
import org.tiledzelda.model.elements.movableElements.player.Player;
import org.tiledzelda.model.map.maputils.Direction;
import org.tiledzelda.model.map.maputils.Distance;
import org.tiledzelda.visualization.graphics.element.ElementGraphic;
import org.tiledzelda.visualization.graphics.element.ScreenPoint;

/**
 * As ElementGraphic, represents a movable element's avatar, strictly at pixel
 * level, containing the graphics of the element when heading in any direction,
 * and the animations of the element when walking in any direction
 * 
 * @author Facundo Manuel Quiroga
 */

public class MovableElementGraphic extends ElementGraphic implements
		MovableElementObserver {

	public static class Message {
		public Message(String string, long i) {
			message = string;
			ttl = i;

		}

		String message;
		long ttl;
	}

	private MovableElement movableElement;

	private Direction direction;

	private MovableSprite idle;
	private MovableSprite walking;
	public ArrayList<Message> notices;
	protected GraphicMovementState graphicMovementState;
	static int FontSize = 12;

	public MovableElementGraphic(MovableSprite idle, MovableSprite walking) {
		super();
		this.setIdle(idle);
		this.setWalking(walking);
		this.doStopMoving();
		notices = new ArrayList<Message>();
	}

	public void doMove(Direction direction,
			int speed) {
		this.getWalking().setSpeed(speed);
		this.setGraphicMovementState(new GraphicMovingMovementState(this, this
				.getWalking(), direction, speed));
	}

	public void doStopMoving() {
		this.setGraphicMovementState(new GraphicIdleMovementState(this, this
				.getIdle()));
	}

	public void doChangeDirection(Direction direction) {
		this.setDirection(direction);
		this.getIdle().changeDirection(direction);
		this.getWalking().changeDirection(direction);
	}

	public int getXCorrection() {
		return this.getGraphicMovementState().getXDifference();
	}

	public int getYCorrection() {
		return this.getGraphicMovementState().getYDifference();
	}

	/**
	 * Draws an element's graphic according to it's current sprite Also draws
	 * its current state.
	 * 
	 * @param g
	 *            Graphics context where to draw
	 * @param originalScreenX
	 *            X Position on the screen to draw
	 * @param originalScreenY
	 *            Y Position on the screen to draw
	 */
	public void draw(Graphics2D g,
			int originalScreenX,
			int originalScreenY) {
		super.draw(g, originalScreenX, originalScreenY);
		Image image = this.getCurrentImage();
		ScreenPoint point = this.getDrawingScreenPoint(	originalScreenX,
														originalScreenY, image);
		this.drawHealthState(g, point);
		drawNotices(g, point);
	}

	private void drawNotices(Graphics2D g,
			ScreenPoint point) {
		long time = GameObjects.getInstance().getTimeKeeper()
				.getElapsedTimeSinceLastUpdate();
		int j = 0;
		for (Message s : notices) {
			g.setFont(new Font("Arial", 1, FontSize));
			g.setColor(Color.GREEN);
			g.drawString(s.message, point.x, point.y + (FontSize * (j + 2)));
			s.ttl -= time;
			j++;
		}
		Iterator<Message> i = notices.iterator();
		while (i.hasNext()) {
			if (i.next().ttl < 0) {
				i.remove();
			}
		}
		if (notices.size()>5){
			notices.clear();
		}

	}

	private void drawHealthState(Graphics2D g,
			ScreenPoint point) {
		HealthState state = this.getMovableElement().getHealthState();
		AttributeState health = state.getHealth();
		AttributeState stamina = state.getStamina();

		if (state != null) {
			g.setFont(new Font("Arial", 1, FontSize));
			g.setColor(Color.RED);
			g.drawString(health.getCurrentValue() + "/"
					+ health.getMaximumValue().getValue(), point.x, point.y);
			g.setColor(Color.YELLOW);
			g.drawString(stamina.getCurrentValue() + "/"
					+ stamina.getMaximumValue().getValue(), point.x, point.y
					+ FontSize);
		}
	}

	/**
	 * Updates the sprite position, if necessary
	 */
	public void update(long timeElapsed) {
		this.getGraphicMovementState().update(timeElapsed);
	}

	@Override
	public Image getCurrentImage() {
		return this.getCurrentSprite().getImage();
	}

	// SPRITES
	protected MovableSprite getCurrentSprite() {
		return this.getGraphicMovementState().getMovableSprite();
	}

	protected MovableSprite getIdle() {
		return idle;
	}

	protected void setIdle(MovableSprite idle) {
		this.idle = idle;
	}

	protected MovableSprite getWalking() {
		return walking;
	}

	protected void setWalking(MovableSprite walking) {
		this.walking = walking;
	}

	// DIRECTION
	protected Direction getDirection() {
		return direction;
	}

	protected void setDirection(Direction direction) {
		this.direction = direction;
	}

	protected MovableElement getMovableElement() {
		return this.movableElement;
	}

	public void setMovableElement(MovableElement movableElement) {
		this.movableElement = movableElement;
	}

	public void attack(Direction direction) {
		
		double d = distanceToPlayer().norm();
		

		if (d < 8) {
			double volume = 0.1 / ((Math.pow(d, 4)) + 1);
			//System.out.println("attack:"+volume);
			Main.g.soundManager.play("attack.wav", (float) volume);
			
		}


	}

	public void attackedFrom(Direction direction,
			ActionResultAttack result) {
		// TODO Defending Sprites Needed!

	}

	public void die() {
		// Do nothing; maybe, sometime, ghost sprite and/or dying animation

	}

	public void changeDirection(Direction direction) {
		this.getGraphicMovementState().changeDirection(direction);
	}

	/**
	 * Move one block in a direction with a speed if im facing the same
	 * direction i am going to move, i just move speed as in ms/tile, ej
	 * speed=118ms, blockSize=59 118 ms ____ 1 tile 59 pixels ___ 1 tile 59
	 * pixels ___ 118 ms 1/2 pixels ___ 1 ms
	 */
	public void move(Direction direction,
			int speed) {
		this.getGraphicMovementState().move(direction, speed);
	}

	public void stoppedMoving() {
		this.getGraphicMovementState().stoppedMoving();
	}

	public Distance distanceToPlayer() {
		Player p = Main.g.getPlayer();
		return movableElement.getPosition().distanceTo(p.getPosition());
	}

	public void finishedAttack(ActionResultAttack result) {
		double d = distanceToPlayer().norm();

		if (d < 8) {
			if (result == ActionResultAttack.hit) {
				
			double volume = 0.4 / ((Math.pow(d, 4)) + 1);
			
			Main.g.soundManager.play("hit.wav", (float) volume);
			}
		}
		
	}

	protected GraphicMovementState getGraphicMovementState() {
		return this.graphicMovementState;
	}

	protected void setGraphicMovementState(GraphicMovementState graphicMovementState) {
		this.graphicMovementState = graphicMovementState;
	}

	public MovableElementGraphic clone() {
		MovableSprite idle = this.getIdle().clone();
		MovableSprite walking = this.getWalking().clone();
		return new MovableElementGraphic(idle, walking);
	}

}
