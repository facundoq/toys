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

package org.tiledzelda.visualization.gui.panels.labelbased;

import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.Panel;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Facundo Manuel Quiroga Jul 25, 2009
 */
public abstract class ObjectsListPanel extends Panel {

	protected int usedHeight;

	public static int defaultWidth = 200;
	protected static int defaultHeight = 20;
	protected String title;

	protected ArrayList<Label> labels;

	public ObjectsListPanel(String title) {
		this.setTitle(title);
		this.setLayout(new FlowLayout());
		this.addLabel(title, 0, 0);

		this.setLabels(new ArrayList<Label>());
		//this.setupLabels();
	}

	public void setupLabels() {
		this.removeLabels();
		int currentY = defaultHeight * 2;
		for (int i = 0; i < this.getObjects().size(); i++) {
			Label label = this.addLabel("asdasd", 0, currentY);
			this.getLabels().add(label);
			currentY += defaultHeight;
		}
		this.setUsedHeight(currentY);
		this.updateLabels();
	}

	public void removeLabels() {
		for (Label label : this.getLabels()) {
			this.remove(label);
		}
		this.setLabels(new ArrayList<Label>());
	}

	public Label addLabel(String name, int x, int y, int width, int height) {
		Label label = new Label(name);
		//label.setSize(width, height);
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
		ArrayList<Label> labels = this.getLabels();
		List<Object> objects = this.getObjects();
		if (labels.size() != objects.size()) {
			this.setupLabels();
		} else {
			updateLabels();
		}
	}

	public void updateLabels() {
		ArrayList<Label> labels = this.getLabels();
		List<Object> objects = this.getObjects();
		for (int i = 0; i < labels.size(); i++) {
			labels.get(i).setText(this.printObjectLabel(objects.get(i)));
		}
	}

	public String printObjectLabel(Object object) {
		return object.toString();
	}

	public abstract ArrayList<Object> getObjects();

	public ArrayList<Label> getLabels() {
		return this.labels;
	}

	public void setLabels(ArrayList<Label> labels) {
		this.labels = labels;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
