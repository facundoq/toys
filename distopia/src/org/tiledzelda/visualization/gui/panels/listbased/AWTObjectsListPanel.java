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

package org.tiledzelda.visualization.gui.panels.listbased;

import java.awt.Label;
import java.awt.List;
import java.awt.Panel;
import java.util.ArrayList;

/**
 * @author Facundo Manuel Quiroga Jul 25, 2009
 */
public abstract class AWTObjectsListPanel extends Panel {

	protected int usedHeight;

	protected static int defaultWidth = 150;
	protected static int defaultHeight = 20;
	protected String title;

	protected List list;

	public AWTObjectsListPanel(String title) {
		this.setTitle(title);
		this.addLabel(title, 0, 20);
		this.list = new List(8, false);

		this.list.setBounds(0, 40, defaultWidth, 200);
		this.add(list);
		this.setUpList();
		this.setUsedHeight(defaultHeight + this.list.getHeight());
	}

	protected void setUpList() {
		this.clearList();
		for (Object object : this.getObjects()) {
			this.list.add(this.printObjectLabel(object));
		}
	}

	/**
	 * 
	 */
	protected void clearList() {
		this.list.removeAll();
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Label addLabel(String name, int x, int y, int width, int height) {
		Label label = new Label(name);
		label.setBounds(x, y, width, height);
		this.add(label);
		return label;
	}

	public Label addLabel(String name, int x, int y) {
		return this.addLabel(name, x, y, defaultWidth, defaultHeight);
	}

	public int getUsedHeight() {
		return this.usedHeight;
	}

	protected void setUsedHeight(int usedHeight) {
		this.usedHeight = usedHeight;
	}

	public void update() {
		this.setUpList();
	}

	public String printObjectLabel(Object object) {
		return object.toString();
	}

	public abstract ArrayList<Object> getObjects();

}
