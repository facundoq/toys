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
import java.io.File;
import java.util.ArrayList;

import org.tiledzelda.exceptions.GameLoadingError;
import org.tiledzelda.main.resourcemanagement.ImageManager;
import org.tiledzelda.visualization.graphics.animations.Animation;
import org.tiledzelda.visualization.graphics.animations.AnimationFrame;

/**
 * Loads animations from disk and allows client to access to them
 * 
 * @author Facundo Manuel Quiroga Feb 11, 2009
 */
public class AnimationLoader {

	protected String animationsDirectoryPath;

	protected AnimationLoader(String animationsDirectoryPath) {
		this.animationsDirectoryPath = animationsDirectoryPath;
	}

	protected String getAnimationsDirectoryPath() {
		return animationsDirectoryPath;
	}

	public Animation getAnimation(String name) {
		ArrayList<File> animationDirectories = this.getDirectoriesPathToAnimation(name);
		// the animationDirectory is the last of the list
		File animationDirectory = animationDirectories.get(animationDirectories.size() - 1);
		AnimationDescriptorLoader animationDescriptorLoader = new AnimationDescriptorLoader(animationDirectories, this.getAnimationsDirectoryPath());
		AnimationDescriptor animationDescriptor = animationDescriptorLoader.getAnimationDescriptor();
		return this.generateAnimation(animationDescriptor, animationDirectory);
	}

	/**
	 * @param animationName of the animation to be loaded
	 * @return all the directories that must be traveled from the "animations" directory to reach the proper directory of the animation with the requested name
	 */
	protected ArrayList<File> getDirectoriesPathToAnimation(String animationName) {
		String[] paths = animationName.split("\\.");
		ArrayList<File> directories = new ArrayList<File>();
		String path = this.getAnimationsDirectoryPath();
		for (String folderName : paths) {
			//System.out.println(folderName);
			path = path + "/" + folderName;
			//System.out.println(path);
			directories.add(new File(path));
		}
		//System.out.println(directories);
		return directories;
	}

	protected File getAnimationDirectory(String name) {
		String relativeDirectory = name.replace(".", "/");
		String animationsPath = this.getAnimationsDirectoryPath();
		String path = animationsPath + "/" + relativeDirectory;
		return new File(path);
	}

	/**
	 * Get the directory named directoryName that is contained by currentDirectory
	 */
	protected File getNextDirectory(File currentDirectory, String directoryName) {
		for (File file : currentDirectory.listFiles()) {
			if (file.getName().equals(directoryName)) {
				return file;
			}
		}
		throw new GameLoadingError("Invalid name : directory " + directoryName + " does not exist inside " + currentDirectory.getName());
	}

	protected Animation generateAnimation(AnimationDescriptor animationDescriptor, File animationDirectory) {
		ArrayList<AnimationFrameDescriptor> animationFrameDescriptors = animationDescriptor.getAnimationFrameDescriptors();
		ArrayList<Image> frameImages = this.getFrameImages(animationFrameDescriptors, animationDirectory);
		ArrayList<AnimationFrame> animationFrames = new ArrayList<AnimationFrame>();
		for (int i = 0; i < animationFrameDescriptors.size(); i++) {
			AnimationFrameDescriptor animationFrameDescriptor = animationFrameDescriptors.get(i);
			Image image = frameImages.get(i);
			AnimationFrame animationFrame = new AnimationFrame(image, animationFrameDescriptor.getPercentage());
			animationFrames.add(animationFrame);
		}
		return new Animation(animationFrames, 100, animationDescriptor.loops());
	}

	/**
	 * @param animationFrameDescriptors
	 * @param animationDirectory
	 * @return
	 */
	private ArrayList<Image> getFrameImages(ArrayList<AnimationFrameDescriptor> animationFrameDescriptors, File animationDirectory) {
		ArrayList<Image> frameImages = new ArrayList<Image>();
		for (AnimationFrameDescriptor afd : animationFrameDescriptors) {
			String path = animationDirectory.getPath() + "/" + afd.getImageName();
			frameImages.add(ImageManager.getInstance().loadImage(path));
		}
		return frameImages;
	}

	protected File getAnimationsDirectory() {
		String path = this.getAnimationsDirectoryPath();
		File directory = new File(path);
		if (!directory.exists()) {
			throw new GameLoadingError("Animations path invalid, " + path + " does not exist");
		}
		if (!directory.isDirectory()) {
			throw new GameLoadingError("Animations path invalid, " + path + " is not a directory");
		}
		return directory;
	}

}
