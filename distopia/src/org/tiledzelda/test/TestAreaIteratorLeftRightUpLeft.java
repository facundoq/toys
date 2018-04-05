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

package org.tiledzelda.test;

import java.util.Iterator;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.tiledzelda.model.map.maputils.Area;
import org.tiledzelda.model.map.maputils.Position;

/**
 * @author Facundo Manuel Quiroga 05/10/2008
 */
public class TestAreaIteratorLeftRightUpLeft extends TestCase {

	protected Iterator<Position> areaIterator;

	public void setUp() {
		Area area = new Area(Position.newPosition(2, 6), Position.newPosition(7, 11));
		areaIterator = area.iteratorLeftRightUpDown();
	}

	public void testIteration() {
		for (int j = 6; j <= 11; j++) {
			for (int i = 2; i <= 7; i++) {
				Position position = areaIterator.next();
				Assert.assertEquals("Positions should be the same", Position.newPosition(i, j), position);
			}
		}

	}

}
