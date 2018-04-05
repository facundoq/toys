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

package org.tiledzelda.model.elements.movableElements.statistics.characteristics.attributes;

import java.util.ArrayList;

import org.tiledzelda.model.elements.movableElements.statistics.characteristics.ExperienceKeeper;

/**
 * @author Facundo Manuel Quiroga Jul 22, 2009
 */
public class CommonAttributes {

	public static Attribute newDefault(String name) {
		return new Attribute(name, 1, ExperienceKeeper.commonExperiencePointsPerLevel());
	}

	public static Attribute newStrength() {
		return newDefault("Strength");
	}

	public static Attribute newConstitution() {
		return newDefault("Constitution");
	}

	public static Attribute newEndurance() {
		return newDefault("Endurance");
	}

	public static Attribute newDexterity() {
		return newDefault("Dexterity");
	}

	public static Attribute newSpeed() {
		return newDefault("Speed");
	}

	public static Attribute newWillpower() {
		return newDefault("Willpower");
	}

	public static Attribute newIntelligence() {
		return newDefault("Intelligence");
	}

	public static Attribute newConcentration() {
		return newDefault("Concentration");
	}

	public static ArrayList<Attribute> getAll() {
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		attributes.add(newStrength());
		attributes.add(newConstitution());
		attributes.add(newEndurance());
		attributes.add(newDexterity());
		attributes.add(newSpeed());
		attributes.add(newWillpower());
		attributes.add(newIntelligence());
		attributes.add(newConcentration());
		return attributes;
	}
}
