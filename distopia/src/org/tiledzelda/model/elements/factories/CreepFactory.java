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
import org.tiledzelda.exceptions.InvalidPositionException;
import org.tiledzelda.exceptions.OcuppiedPositionException;
import org.tiledzelda.main.resourcemanagement.Configuration;
import org.tiledzelda.model.elements.factories.types.CreepDescription;
import org.tiledzelda.model.elements.movableElements.creeps.Creep;
import org.tiledzelda.model.map.MapTeleporter;
import org.tiledzelda.model.map.TileMap;
import org.tiledzelda.model.map.maputils.Position;
import org.tiledzelda.util.RandomChooser;

/**
 * @author Facundo Manuel Quiroga Oct 22, 2008
 */
public class CreepFactory extends MovableElementFactory {
	protected HashMap<String, CreepDescription> creepDescriptions;
	protected CreepDescriptionFactory creepDescriptionFactory = null;

	public CreepFactory() {
		this.setCreepDescriptions(new HashMap<String, CreepDescription>());
		CreepDescriptionFactory creepDescriptionFactory = this.getCreepDescriptionLoader();
		//System.out.println(creepDescriptionFactory.getCreepDescriptions());
		this.getCreepDescriptions().putAll(creepDescriptionFactory.getCreepDescriptions());
	}

	public Creep createCreep(String name, TileMap tileMap, Position position) throws InvalidPositionException, OcuppiedPositionException {
		Creep creep = this.createCreep(name);
		MapTeleporter.getInstance().teleportTo(creep, tileMap, position);
		return creep;
	}

	protected Creep createCreep(String name) {
		CreepDescription creepDescription = this.getCreepDescriptions().get(name);
		if (creepDescription == null) {
			throw new GameLoadingError("Invalid creep " + name);
		}
		return creepDescription.createCreep();
	}

	protected CreepDescriptionFactory getCreepDescriptionLoader() {
		if (this.creepDescriptionFactory == null) {
			//TODO sort out how to get the root directory 
			String creepDescriptionsDirectoryName = Configuration.getInstance().getValue("creepDescriptionsDirectory");
			String creepDescriptionsDirectory = Configuration.getInstance().getRootDirectory() + "" + creepDescriptionsDirectoryName;
			File creepDescriptionsDirectoryFile = new File(creepDescriptionsDirectory);
			this.creepDescriptionFactory = new CreepDescriptionFactory(creepDescriptionsDirectoryFile);
		}
		return this.creepDescriptionFactory;
	}

	protected void setCreepDescriptionLoader(CreepDescriptionFactory creepDescriptionFactory) {
		this.creepDescriptionFactory = creepDescriptionFactory;
	}

	protected HashMap<String, CreepDescription> getCreepDescriptions() {
		return this.creepDescriptions;
	}

	protected void setCreepDescriptions(HashMap<String, CreepDescription> creepDescriptions) {
		this.creepDescriptions = creepDescriptions;
	}

	/**
	 * @return a random creep picked from all the available descriptions
	 */
	public Creep createRandomCreep() {
		int descriptionsQuantity = this.getCreepDescriptions().size();
		CreepDescription[] descriptions = this.getCreepDescriptions().values().toArray(new CreepDescription[descriptionsQuantity]);
		int[] probabilities = this.getUniformProbabilities(descriptionsQuantity);
		RandomChooser<CreepDescription> randomChooser = new RandomChooser<CreepDescription>(descriptions, probabilities);
		return randomChooser.getObject().createCreep();
	}

	public Creep createRandomCreep(TileMap tileMap, Position position) throws InvalidPositionException, OcuppiedPositionException {
		Creep creep = this.createRandomCreep();
		MapTeleporter.getInstance().teleportTo(creep, tileMap, position);
		return creep;
	}

	/**
	 * @param descriptionsQuantity
	 * @return
	 */
	private int[] getUniformProbabilities(int descriptionsQuantity) {
		int[] probabilities = new int[descriptionsQuantity];
		if (descriptionsQuantity > 0) {
			int probability = 100 / descriptionsQuantity;
			int remaining = 100 % descriptionsQuantity;
			int positionsThatGetOneMore = remaining - 1;
			for (int i = 0; i < probabilities.length; i++) {
				probabilities[i] = probability;
				if (i <= positionsThatGetOneMore) {
					probabilities[i]++;
				}
			}
		}
		return probabilities;
	}

}
