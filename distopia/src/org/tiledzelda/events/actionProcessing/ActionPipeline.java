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

package org.tiledzelda.events.actionProcessing;

import java.util.LinkedList;
import java.util.Queue;

import org.tiledzelda.events.actions.Action;
import org.tiledzelda.events.actions.results.ActionResult;

/**
 * Holds the Actions of Elements of the game as they enqueue them, and executes them all sequentially when asked to process the queue
 * 
 * @author Facundo Manuel Quiroga
 */
public class ActionPipeline {
	// TODO DECIDE WHAT TO DO WITH THIS CLASS
	private static ActionPipeline instance;

	public static ActionPipeline getInstance() {
		if (ActionPipeline.instance == null) {
			ActionPipeline.instance = new ActionPipeline();
		}
		return ActionPipeline.instance;
	}

	private Queue<Action> actions;

	private ActionPipeline() {
		this.setActions(new LinkedList<Action>());
	}

	public void executeAction(Action action) {
		//this.getActions().add(action);
		action.execute();
	}

	public void processQueue() {
		Queue<Action> actions = this.getActions();
		while (!actions.isEmpty()) {
			Action action = actions.remove();
			ActionResult execute = action.execute();
			
		}
	}

	public Queue<Action> getActions() {
		return actions;
	}

	public void setActions(Queue<Action> actions) {
		this.actions = actions;
	}
}
