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

package org.tiledzelda.events.actions.results;

/**
 * @author Facundo Manuel Quiroga Jul 26, 2009
 */
public class ActionResultAttack extends BaseActionResult {

	public static ActionResultAttack hit = new ActionResultAttack(Result.Hit);
	public static ActionResultAttack miss = new ActionResultAttack(Result.Miss);
	public static ActionResultAttack dodge = new ActionResultAttack(Result.Dodge);
	public static ActionResultAttack notExecuted = new ActionResultAttack(Result.NotExecuted);
	public static ActionResultAttack cannotBeAttacked = new ActionResultAttack(Result.CannotBeAttacked);
	public static ActionResultAttack invalidTarget = new ActionResultAttack(Result.InvalidTarget);
	public static ActionResultAttack couldNotPerform = new ActionResultAttack(Result.CouldNotPerform);

	public enum Result {
		Hit, Miss, Dodge, NotExecuted, CannotBeAttacked, InvalidTarget, CouldNotPerform
	};

	protected Result result;

	public ActionResultAttack(Result result) {
		this.setResult(result);
	}

	protected Result getResult() {
		return this.result;
	}

	protected void setResult(Result result) {
		this.result = result;
	}

	public String toString() {
		return this.getResult().toString();
	}
}
