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

package org.tiledzelda.model.elements.factories.types;

import java.util.ArrayList;

import org.tiledzelda.exceptions.GameLoadingError;
import org.tiledzelda.input.ai.AI;
import org.tiledzelda.input.ai.components.AIComponent;
import org.tiledzelda.input.ai.components.EscapingFromTypesAI;
import org.tiledzelda.input.ai.components.FollowingTypesAI;

/**
 * Describes a creep's AI. Can create it with "createAI".. TODO implemented quick'n'dirty.
 * 
 * @author Facundo Manuel Quiroga Mar 10, 2009
 */
public class CreepAIDescription {
	static public final String following = "following";
	static public final String escaping = "escaping";

	protected String type;
	protected ArrayList<String> typesReactiveTo;

	public CreepAIDescription(String type, ArrayList<String> typesReactiveTo) {
		super();
		this.setType(type);
		this.setTypesReactiveTo(new ArrayList<String>(typesReactiveTo));
	}

	protected String getType() {
		return this.type;
	}

	protected void setType(String type) {
		this.type = type;
	}

	protected ArrayList<String> getTypesReactiveTo() {
		return this.typesReactiveTo;
	}

	protected void setTypesReactiveTo(ArrayList<String> typesReactiveTo) {
		this.typesReactiveTo = typesReactiveTo;
	}

	public AI createCreepAI() {
		AIComponent aiComponent;
		if (this.getType().equals(CreepAIDescription.following)) {
			aiComponent = new FollowingTypesAI(this.getTypesReactiveTo());
		} else if (this.getType().equals(CreepAIDescription.escaping)) {
			aiComponent = new EscapingFromTypesAI(this.getTypesReactiveTo());
		} else {
			throw new GameLoadingError("Type of AI, " + this.getType() + "is invalid");
		}
		return new AI(aiComponent);
	}
}
