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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.tiledzelda.exceptions.GameLoadingError;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author Facundo Manuel Quiroga Feb 12, 2009
 */
public class AnimationEntityResolver implements EntityResolver {
	protected File animationSchemaFile;

	public AnimationEntityResolver(File animationSchemaFile) {
		this.animationSchemaFile = animationSchemaFile;
	}

	public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
		System.out.println("resolving");
		if (systemId.equals("animation.xsd")) {
			FileInputStream fis = new FileInputStream(this.animationSchemaFile);
			return new InputSource(fis);
		} else {
			throw new GameLoadingError("Only valid id is animation.xsd");
		}

	}

}
