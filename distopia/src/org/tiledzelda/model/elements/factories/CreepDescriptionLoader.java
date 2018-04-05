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

package org.tiledzelda.model.elements.factories;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.Profile.Section;
import org.tiledzelda.exceptions.GameLoadingError;
import org.tiledzelda.model.elements.factories.types.CreepAIDescription;
import org.tiledzelda.model.elements.factories.types.CreepDescription;
import org.tiledzelda.model.elements.factories.types.CreepElementGraphicDescription;
import org.tiledzelda.model.elements.factories.types.CreepElementTypeDescription;

/**
 * @author Facundo Manuel Quiroga Mar 16, 2009
 */
public class CreepDescriptionLoader {

	public CreepDescription load(File file, String name) {
		try {
			Ini iniFile = new Ini(new FileInputStream(file));
			CreepAIDescription creepAIDescription = this.getCreepAIDescription(iniFile.get("AI"));
			CreepElementGraphicDescription creepElementGraphicDescription = this.getCreepElementGraphicDescription(iniFile.get("ElementGraphic"));
			CreepElementTypeDescription creepElementTypeDescription = this.getCreepElementTypeDescription(iniFile.get("ElementType"), name);
			return new CreepDescription(creepElementTypeDescription, creepElementGraphicDescription, creepAIDescription);

		} catch (NumberFormatException e) {
			throw new GameLoadingError("Error loading CreepDescriptionFile " + file.getPath() + ". Message: " + e.getMessage());
		} catch (InvalidFileFormatException e) {
			throw new GameLoadingError("InvalidIniFormatException when opening creepDescriptionFile file" + file.getPath() + ". Message: " + e.getMessage());
		} catch (FileNotFoundException e) {
			throw new GameLoadingError("FileNotFoundException when opening creepDescriptionFile file" + file.getPath() + ". Message: " + e.getMessage());
		} catch (IOException e) {
			throw new GameLoadingError("IOException when opening creepDescriptionFile file" + file.getPath() + ". Message: " + e.getMessage());
		} catch (GameLoadingError e) {
			throw new GameLoadingError("Error when opening creepDescriptionFile file" + file.getPath() + ". Message: " + e.getMessage());
		}

	}

	/**
	 * @param section
	 * @param name2
	 * @return
	 */
	protected CreepElementTypeDescription getCreepElementTypeDescription(Section section, String name) {
		int damage = this.getValueAsInt(section, "damage");
		int damageReduction = this.getValueAsInt(section, "damageReduction");
		int staminaHitCost = this.getValueAsInt(section, "staminaHitCost");
		int baseSpeed = this.getValueAsInt(section, "baseSpeed");
		int chanceToHit = this.getValueAsInt(section, "chanceToHit");
		int chanceToDodge = this.getValueAsInt(section, "chanceToDodge");
		int maxHealth = this.getValueAsInt(section, "maxHealth");
		int maxStamina = this.getValueAsInt(section, "maxStamina");
		int healthRestorationRate = this.getValueAsInt(section, "healthRestorationRate");
		int staminaRestorationRate = this.getValueAsInt(section, "staminaRestorationRate");

		return new CreepElementTypeDescription(name, damage, damageReduction, staminaHitCost, baseSpeed, chanceToHit, chanceToDodge, maxHealth, maxStamina, healthRestorationRate, staminaRestorationRate);

	}

	protected int getValueAsInt(Section section, String name) {
		String value = section.get(name);
		try {
			return new Integer(value);
		} catch (NumberFormatException e) {
			throw new GameLoadingError("Key " + name + " in section " + section.getName() + " is not an integer; value = " + value);
		}
	}

	protected boolean getValueAsBoolean(Section section, String name) {
		String value = section.get(name);
		if (value.equals("true")) {
			return true;
		} else if (value.equals("false")) {
			return false;
		} else {
			throw new GameLoadingError("Key " + name + " in section " + section.getName() + " should have a boolean value (true|false), has " + value);
		}
	}

	/**
	 * @param section
	 * @return
	 */
	private CreepElementGraphicDescription getCreepElementGraphicDescription(Section section) {
		String graphic = section.get("graphic");
		boolean graphicIsSimple = this.getValueAsBoolean(section, "graphicIsSimple");
		return new CreepElementGraphicDescription(graphic, graphicIsSimple);
	}

	/**
	 * @param section
	 * @return
	 */
	private CreepAIDescription getCreepAIDescription(Section section) {
		String type = section.get("type");
		String typesReactiveTo = section.get("typesReactiveTo");
		String[] types = typesReactiveTo.split(",");
		List<String> list = Arrays.asList(types);
		ArrayList<String> typeNames = new ArrayList<String>(list);

		return new CreepAIDescription(type, typeNames);
	}

}
