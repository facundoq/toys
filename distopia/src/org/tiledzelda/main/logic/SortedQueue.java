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

package org.tiledzelda.main.logic;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author Facundo Manuel Quiroga Dec 26, 2008
 */
public class SortedQueue<K, V> {
	//TODO drop the generic, make more specific

	SortedMap<K, V> elements;

	public SortedQueue() {
		elements = new TreeMap<K, V>();
	}

	/**
	 * @return
	 */
	public K topKey() {
		return elements.firstKey();
	}

	public V topValue() {
		return elements.get(elements.firstKey());
	}

	/**
	 * @return
	 */
	public boolean isEmpty() {
		return elements.isEmpty();
	}

	public V popValue() {
		V value = this.topValue();
		this.elements.remove(this.topKey());
		return value;
	}

	public K popKey() {
		K key = this.topKey();
		this.elements.remove(key);
		return key;
	}

	public void add(K key, V value) {
		this.elements.put(key, value);

	}

}
