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

package org.tiledzelda.main.resourcemanagement.graphics.animations;

import java.awt.Image;
import java.util.HashMap;

import org.tiledzelda.main.resourcemanagement.ImageManager;
import org.tiledzelda.visualization.graphics.animations.Animation;

/**
 * @author Facundo Manuel Quiroga Feb 10, 2009
 */
public class AnimationFactory {

	protected HashMap<String, AnimationCloner> animationCloners;
	protected AnimationLoader animationLoader;
	protected String animationsDirectoryPath;

	public AnimationFactory(String animationsDirectoryPath) {
		animationCloners = new HashMap<String, AnimationCloner>();
		this.animationsDirectoryPath = animationsDirectoryPath;
		animationLoader = new AnimationLoader(animationsDirectoryPath);

		ImageManager imageManager = ImageManager.getInstance();
		Image rock = imageManager.getValue("rock");
		Image tree = imageManager.getValue("tree");
		Animation rockAnimation = Animation.newAnimation(rock, 100);
		Animation treeAnimation = Animation.newAnimation(tree, 100);
		animationCloners.put("rock", new AnimationCloner(rockAnimation));
		animationCloners.put("tree", new AnimationCloner(treeAnimation));

	}

	public Animation getAnimation(String name) {
		AnimationCloner animationCloner = this.animationCloners.get(name);
		if (animationCloner == null) {
			Animation animation = this.animationLoader.getAnimation(name);
			animationCloner = new AnimationCloner(animation);
			animationCloners.put(name, animationCloner);
		}
		return animationCloner.getAnimation();
	}

}
