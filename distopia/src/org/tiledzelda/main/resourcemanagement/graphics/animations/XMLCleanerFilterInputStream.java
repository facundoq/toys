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

import java.io.FileInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Ignores whitespace and newline characters from inputStream
 * 
 * @author Facundo Manuel Quiroga Feb 12, 2009
 */
public class XMLCleanerFilterInputStream extends FilterInputStream {

	ArrayList<Integer> invalidCharacters;

	protected XMLCleanerFilterInputStream(FileInputStream in) {
		super(in);
		this.invalidCharacters = new ArrayList<Integer>();
		invalidCharacters.add((int) Character.LINE_SEPARATOR);
		invalidCharacters.add(10);
		invalidCharacters.add(9);
	}

	public int read() throws IOException {

		int b = in.read();
		while (invalidCharacters.contains(b)) {
			b = in.read();
		}
		return b;
	}

	public int read(byte[] data, int offset, int length) throws IOException {

		int result = in.read(data, offset, length);
		for (int i = offset; i < offset + result; i++) {
			if (invalidCharacters.contains(data[i])) {
				data[i] = 32;
			}
		}
		return result;
	}

}
