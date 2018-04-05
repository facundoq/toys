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

package org.tiledzelda.main.resourcemanagement;

import java.awt.Graphics;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import org.tiledzelda.exceptions.GameLoadingError;
import org.tiledzelda.visualization.screen.FullScreenManager;

public class ImageManager {
	private static ImageManager imageManager;
	private Hashtable<String, BufferedImage> dictionary;

	private ImageManager() {
		dictionary = new Hashtable<String, BufferedImage>();
		Configuration configuration = Configuration.getInstance();
		String imageDirectoryPath = configuration.getValue("applicationPath") + configuration.getValue("imageDirectory");
		File imageDirectory = new File(imageDirectoryPath);
		File[] files = imageDirectory.listFiles();
		for (int i = 0; i < files.length; i++) {
			// TODO damn svn puts files in all dirs =/
			if (!files[i].isDirectory()) {
				if (!files[i].getName().equals(".svn")) {
					BufferedImage image = loadImage(files[i].getAbsolutePath());
					String imageName = files[i].getName();
					int pointPosition = imageName.lastIndexOf(".");
					imageName = imageName.substring(0, pointPosition);
					dictionary.put(imageName, image);
					//System.out.println(imageName);
				}
			}
		}

	}

	public BufferedImage loadImage(String fileName) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(fileName));
		} catch (IOException e) {
			throw new GameLoadingError("Cant load image: " + fileName);
		}
		BufferedImage image2 = FullScreenManager.getInstance().createCompatibleImage(image.getWidth(), image.getHeight(), Transparency.TRANSLUCENT);
		Graphics g = image2.getGraphics();
		g.drawImage(image, 0, 0, null);
		return image2;
	}

	public BufferedImage getValue(String key) {
		return (dictionary.get(key));
	}

	public void setValue(String key, BufferedImage value) {
		dictionary.put(key, value);
	}

	public Hashtable<String, BufferedImage> getAll() {
		return this.dictionary;
	}

	public static ImageManager getInstance() {
		if (imageManager == null) {
			imageManager = new ImageManager();
		}
		return imageManager;
	}

}
