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
import java.util.HashMap;

import org.tiledzelda.exceptions.GameLoadingError;
import org.tiledzelda.main.GameFactories;
import org.tiledzelda.main.resourcemanagement.Configuration;
import org.tiledzelda.model.elements.factories.types.CreepDescription;

/**
 * @author Facundo Manuel Quiroga Mar 16, 2009
 */
public class CreepDescriptionFactory {
	protected HashMap<String, CreepDescription> creepDescriptions;

	public CreepDescriptionFactory(File creepDescriptionFolder) {
		this.creepDescriptions = this.loadCreepDescriptions(creepDescriptionFolder, "");
	}

	/**
	 * @param baseName of the CreepDescriptions inside the creepDescriptionDirectory
	 * @param creepDescriptionDirectory
	 * @return a HashMap of names/CreepDescription pairs
	 */
	protected HashMap<String, CreepDescription> loadCreepDescriptions(File creepDescriptionDirectory, String baseName) {
		HashMap<String, CreepDescription> creepDescriptions = new HashMap<String, CreepDescription>();
		for (File file : creepDescriptionDirectory.listFiles()) {
			String newBaseName = baseName;
			//add the point (.) and the directory name as part of the base name of all the descriptions inside the file
			if (!baseName.equals("")) {
				newBaseName = newBaseName + ".";
			}
			newBaseName = newBaseName + file.getName();
			// if file is a directory, recurse and load its descriptions
			if (file.isDirectory()) {
				creepDescriptions.putAll(this.loadCreepDescriptions(file, newBaseName));
			} else if (file.isFile()) {
				// if its a file and it ends with the proper suffix, load its description
				if (file.getName().endsWith("." + this.getCreepDescriptionFileSuffix())) {
					// remove the suffix from the name of the description
					int endIndex = newBaseName.length() - 1 - this.getCreepDescriptionFileSuffix().length();
					newBaseName = newBaseName.substring(0, endIndex);
					creepDescriptions.put(newBaseName, this.loadCreepDescription(file, newBaseName));
				}
			} else {
				GameFactories.getInstance().getFactoriesLogger().logWarning("Warning: While loading creep descriptions: file " + file.getPath() + " is not a file or a directory.");
			}
		}
		return creepDescriptions;
	}

	/**
	 * @return the suffix of the CreepDescription files
	 */
	protected String getCreepDescriptionFileSuffix() {
		return Configuration.getInstance().getValue("creepDescriptionFileExtension");
	}

	/**
	 * @param file: text file from which to load the creep's description
	 * @return
	 */
	protected CreepDescription loadCreepDescription(File file, String name) {
		return new CreepDescriptionLoader().load(file, name);
	}

	public CreepDescription getCreepDescription(String name) {
		CreepDescription creepDescription = this.getCreepDescriptions().get(name);
		if (creepDescription == null) {
			throw new GameLoadingError("Creep description named '" + name + "' not found.");
		}
		return creepDescription;
	}

	public HashMap<String, CreepDescription> getCreepDescriptions() {
		return this.creepDescriptions;
	}

	protected void setCreepDescriptions(HashMap<String, CreepDescription> creepDescriptions) {
		this.creepDescriptions = creepDescriptions;
	}

}
