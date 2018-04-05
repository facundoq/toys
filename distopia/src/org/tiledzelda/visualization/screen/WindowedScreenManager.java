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

package org.tiledzelda.visualization.screen;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.image.BufferStrategy;

/**
 * @author Facundo Manuel Quiroga Nov 16, 2008
 */

public class WindowedScreenManager extends ScreenManager {

	protected static WindowedScreenManager instance = null;

	/**
	 * @return
	 */
	public static WindowedScreenManager getInstance() {
		if (WindowedScreenManager.instance == null) {
			WindowedScreenManager.instance = new WindowedScreenManager();
		}
		return WindowedScreenManager.instance;
	}

	protected GraphicsEnvironment environment;

	protected GraphicsDevice device;

	protected GraphicsConfiguration configuration;

	protected DisplayMode currentDisplayMode;

	protected DisplayMode originalDisplayMode;

	protected Canvas drawingSurface;

	protected Panel sidePanel;

	protected Frame frame;

	protected Panel mainPanel;

	protected WindowedScreenManager() {
		super();
		this.environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		this.device = environment.getDefaultScreenDevice();
		this.configuration = device.getDefaultConfiguration();
		this.currentDisplayMode = device.getDisplayMode();
		this.originalDisplayMode = device.getDisplayMode();

		this.device.getFullScreenWindow();

		/*BufferCapabilities bf = configuration.getBufferCapabilities();
		System.out.println(" fs required: "+bf.isFullScreenRequired());
		System.out.println(" page flipping: "+bf.isPageFlipping());
		System.out.println(" multi buffer: "+bf.isMultiBufferAvailable());
		System.out.println(" flip contents: "+bf.getFlipContents());*/
		frame = new Frame("TiledZelda");
		frame.setSize(1024, 768);

		frame.setResizable(false);

		//frame.setMaximizedBounds(environment.getMaximumWindowBounds());
		//frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);

		mainPanel = new Panel();

		mainPanel.setLayout(null);
		// mainPanel.setBounds(new Rectangle(1024, 768));
		// mainPanel.setPreferredSize(new Dimension(1024, 768));

		frame.add(mainPanel);

		frame.setIgnoreRepaint(true);
		this.drawingSurface = new Canvas();
		drawingSurface.setIgnoreRepaint(true);
		drawingSurface.setBounds(new Rectangle(768, 768));
		drawingSurface.setSize(768, 768);
		mainPanel.add(this.drawingSurface);
		mainPanel.setBackground(Color.red);
		frame.setVisible(true);
		this.drawingSurface.createBufferStrategy(3);

		sidePanel = new Panel();
		sidePanel.setSize(1024 - 768, 768);
		sidePanel.setBackground(Color.gray);
		sidePanel.setBounds(768, 0, 1024 - 768, 768);
		mainPanel.add(sidePanel);

	}

	@Override
	public DisplayMode getCurrentDisplayMode() {
		return this.currentDisplayMode;
	}

	@Override
	public Dimension getDimension() {
		int width = this.currentDisplayMode.getWidth();
		int height = this.currentDisplayMode.getHeight();
		return new Dimension(width, height);
	}

	@Override
	public Graphics2D getGraphics() {
		/*
		 * if (mainPanel != null) { BufferStrategy strategy =
		 * mainPanel.getBufferStrategy(); return (Graphics2D)
		 * strategy.getDrawGraphics(); } else { return null; }
		 */
		// return (Graphics2D) mainPanel.getGraphics();
		return (Graphics2D) this.drawingSurface.getBufferStrategy().getDrawGraphics();
	}

	@Override
	public Component getRenderingSurface() {
		return this.drawingSurface;
	}

	@Override
	public void restoreScreen() {
		// device.setDisplayMode(originalDisplayMode);

	}

	@Override
	public void setDimension(Dimension d) {
		// this.device.setDisplayMode(new DisplayMode(d.width,d.height,32,60));
		this.frame.setBounds(new Rectangle(d));
	}

	@Override
	public void update() {
		if (drawingSurface != null) {
			BufferStrategy strategy = drawingSurface.getBufferStrategy();
			if (!strategy.contentsLost()) {
				strategy.show();
			}
		}

	}

	@Override
	public Panel getSidePanel() {
		return this.sidePanel;
	}

}
