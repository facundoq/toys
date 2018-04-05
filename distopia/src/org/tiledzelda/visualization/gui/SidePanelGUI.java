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

package org.tiledzelda.visualization.gui;

import java.awt.Label;
import java.awt.Panel;

import javax.swing.BoxLayout;

import org.tiledzelda.main.GameObjects;
import org.tiledzelda.model.elements.movableElements.MovableElement;
import org.tiledzelda.model.map.maputils.Direction;
import org.tiledzelda.model.map.maputils.Position;
import org.tiledzelda.visualization.gui.panels.labelbased.AttributesPanel;
import org.tiledzelda.visualization.gui.panels.labelbased.SkillsPanel;
import org.tiledzelda.visualization.gui.panels.labelbased.StatisticsPanel;
import org.tiledzelda.visualization.gui.panels.listbased.AWTAttributesPanel;
import org.tiledzelda.visualization.gui.panels.listbased.AWTSkillsPanel;

/**
 * @author Facundo Manuel Quiroga Nov 18, 2008
 */
public class SidePanelGUI {
	private static final String attackTitle = "Attack time:";
	protected final String fpsTitle = "fps: ";
	protected final String directionTitle = "direction: ";
	protected final String positionTitle = "position: ";

	protected Panel sidePanel;
	protected Label fps;
	protected Label direction;
	protected Label position;
	protected AttributesPanel attributesPanel;
	protected SkillsPanel skillsPanel;
	protected StatisticsPanel statisticsPanel;

	protected AWTAttributesPanel awtAttributesPanel;
	protected AWTSkillsPanel awtSkillsPanel;

	protected MovableElement player;
	private Label attackTime;

	static int defaultHeight = 20;
	static int defaultWidth = 150;

	public SidePanelGUI(Panel sidePanel, MovableElement player) {
		sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
		int currentY = 0;
		this.player = player;
		this.setSidePanel(sidePanel);

		this.fps = this.addLabel(this.fpsTitle, 0, currentY, defaultWidth, defaultHeight);
		currentY += defaultHeight;
		this.position = this.addLabel(this.positionTitle, 0, currentY, defaultWidth, defaultHeight);
		currentY += defaultHeight;
		this.direction = this.addLabel(this.directionTitle, 0, currentY, defaultWidth, defaultHeight);
		currentY += defaultHeight;
		this.attackTime = this.addLabel(this.attackTitle, 0, currentY, defaultWidth, defaultHeight);
		currentY += defaultHeight;
		
		// AttributesPanel
		currentY += 10;
		attributesPanel = new AttributesPanel(player);
		attributesPanel.setBounds(0, currentY, AttributesPanel.defaultWidth, attributesPanel.getUsedHeight());
		sidePanel.add(attributesPanel);
		currentY += attributesPanel.getUsedHeight();

		// SkillsPanel
		currentY += 10;
		skillsPanel = new SkillsPanel(player);
		skillsPanel.setBounds(0, currentY, SkillsPanel.defaultWidth, skillsPanel.getUsedHeight());
		sidePanel.add(skillsPanel);
		currentY += skillsPanel.getUsedHeight();

		// StatisticsPanel
		currentY += 10;
		statisticsPanel = new StatisticsPanel("Statistics", player);
		statisticsPanel.setBounds(0, currentY, StatisticsPanel.defaultWidth, statisticsPanel.getUsedHeight());
		sidePanel.add(statisticsPanel);
		currentY += statisticsPanel.getUsedHeight();

		/*
		// AWTAttributesPanel
		currentY += 30;
		awtAttributesPanel = new AWTAttributesPanel(player);
		awtAttributesPanel.setBounds(0, currentY, AttributesPanel.defaultWidth, 200+awtAttributesPanel.getUsedHeight());
		sidePanel.add(awtAttributesPanel);
		currentY += awtAttributesPanel.getUsedHeight();

		// AWTSkillsPanel
		currentY += 30;
		awtSkillsPanel = new AWTSkillsPanel(player);
		awtSkillsPanel.setBounds(0, currentY, SkillsPanel.defaultWidth, 200+awtSkillsPanel.getUsedHeight());
		sidePanel.add(awtSkillsPanel);
		currentY += awtSkillsPanel.getUsedHeight();*/
	}

	public Label addLabel(String name, int x, int y, int width, int height) {
		Label label = new Label(name);
		label.setBounds(x, y, width, height);
		this.getSidePanel().add(label);
		return label;
	}

	public void update() {
		int fps = GameObjects.getInstance().getFPSCounter().getFPS();
		Position position = this.player.getPosition();
		Direction direction = this.player.getDirection();
		this.fps.setText(this.fpsTitle + fps);
		this.position.setText(this.positionTitle + position);
		this.direction.setText(this.directionTitle + direction);
		this.attackTime.setText(attackTitle +( GameObjects.getInstance().getTimeKeeper().getCurrTime()-this.player.lastAttack));
		
		attributesPanel.update();
		skillsPanel.update();
		statisticsPanel.update();
		//awtAttributesPanel.update();
		//awtSkillsPanel.update();
	}

	public Panel getSidePanel() {
		return this.sidePanel;
	}

	public void setSidePanel(Panel sidePanel) {
		this.sidePanel = sidePanel;
	}

}
