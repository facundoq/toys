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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.tiledzelda.exceptions.GameError;

/**
 * The FullScreenManager class manages initializing and displaying full screen graphics modes.
 */
public class FullScreenManager extends ScreenManager {

	private static FullScreenManager instance;

	/**
	 * Creates a new ScreenManager object.
	 */
	public static FullScreenManager getInstance() {
		if (instance == null) {
			instance = new FullScreenManager();
		}
		return instance;
	}

	GraphicsDevice device;

	private GraphicsEnvironment environment;

	private GraphicsConfiguration configuration;

	private DisplayMode currentDisplayMode;

	protected JFrame frame;

	protected JPanel mainPanel;

	protected JPanel sidePanel;

	protected JPanel renderingSurface;

	private FullScreenManager() {
		environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		device = environment.getDefaultScreenDevice();
		configuration = device.getDefaultConfiguration();
		currentDisplayMode = device.getDisplayMode();
		frame = new JFrame();

		mainPanel = new JPanel();
		mainPanel.setLayout(null);
		mainPanel.setBounds(new Rectangle(1024, 768));
		mainPanel.setPreferredSize(new Dimension(1024, 768));
		mainPanel.setBackground(Color.blue);
		frame.setContentPane(mainPanel);
		sidePanel = new JPanel();
		sidePanel.setSize(1024 - 768, 768);
		sidePanel.setBounds(768, 0, 1024 - 768, 768);
		sidePanel.setLayout(new FlowLayout());
		sidePanel.add(new JLabel("Hola!"));
		sidePanel.setBackground(Color.red);
		mainPanel.add(sidePanel);
		renderingSurface = new JPanel();
		renderingSurface.setBounds(0, 0, 768, 768);
		renderingSurface.setSize(768, 768);
		mainPanel.add(renderingSurface);
		renderingSurface.setOpaque(true);
	}

	/**
	 * Returns a list of compatible display modes for the default device on the system.
	 */
	public DisplayMode[] getCompatibleDisplayModes() {
		return device.getDisplayModes();
	}

	/**
	 * Enters full screen mode and changes the display mode. If the specified display mode is null or not compatible with this device, or if the display mode cannot be changed on this system, the
	 * current display mode is used.
	 * <p>
	 * The display uses a BufferStrategy with 2 buffers.
	 */
	public void setFullScreen(DisplayMode displayMode) {

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setUndecorated(true);
		//frame.setIgnoreRepaint(true);
		frame.setResizable(false);

		device.setFullScreenWindow(frame);

		if (displayMode != null && device.isDisplayChangeSupported()) {
			try {
				device.setDisplayMode(displayMode);
			} catch (IllegalArgumentException ex) {
			}
			// fix for mac os x
			frame.setSize(displayMode.getWidth(), displayMode.getHeight());
		}

		frame.createBufferStrategy(2);

	}

	/**
	 * Returns the window currently used in full screen mode. Returns null if the device is not in full screen mode.
	 */
	public Component getRenderingSurface() {
		// TODO find out difference
		//return this.renderingSurface;
		return this.frame;
		// return (JFrame) device.getFullScreenWindow();
	}

	/**
	 * Returns the first compatible mode in a list of modes. Returns null if no modes are compatible.
	 */
	public DisplayMode findFirstCompatibleMode(DisplayMode modes[]) {
		DisplayMode goodModes[] = device.getDisplayModes();
		for (int i = 0; i < modes.length; i++) {
			for (int j = 0; j < goodModes.length; j++) {
				if (modes[i].equals(goodModes[j])) {
					return modes[i];
				}
			}

		}
		throw new GameError("No display modes available");
	}

	/**
	 * Returns the current display mode.
	 */
	public DisplayMode getCurrentDisplayMode() {
		return device.getDisplayMode();
	}

	/**
	 * Gets the graphics context for the display. The ScreenManager uses double buffering, so applications must call update() to show any graphics drawn.
	 * <p>
	 * The application must dispose of the graphics object.
	 */
	public Graphics2D getGraphics() {
		/*if (frame != null) {
			BufferStrategy strategy = frame.getBufferStrategy();
			return (Graphics2D) strategy.getDrawGraphics();
		} else {
			return null;
		}*/
		return (Graphics2D) this.renderingSurface.getGraphics();
	}

	/**
	 * Updates the display.
	 */
	public void update() {
		if (frame != null) {
			BufferStrategy strategy = frame.getBufferStrategy();
			if (!strategy.contentsLost()) {
				strategy.show();
			}
		}
		// Sync the display on some systems.
		// (on Linux, this fixes event queue problems)
		// also makes it spend an extra 100ms so..		
		// Toolkit.getDefaultToolkit().sync();
	}

	/**
	 * Restores the screen's display mode.
	 */
	public void restoreScreen() {
		Window window = device.getFullScreenWindow();
		if (window != null) {
			window.dispose();
		}
		device.setFullScreenWindow(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see screen.ScreenManager#getDimension(java.awt.Dimension)
	 */
	@Override
	public Dimension getDimension() {
		int width = this.currentDisplayMode.getWidth();
		int height = this.currentDisplayMode.getHeight();
		return new Dimension(width, height);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see screen.ScreenManager#setDimension(java.awt.Dimension)
	 */
	@Override
	public void setDimension(Dimension d) {
		DisplayMode displayMode = new DisplayMode((int) d.getWidth(), (int) d.getHeight(), this.currentBitDepth(), this.currentRefreshRate());
		this.setFullScreen(displayMode);
	}

	private int currentBitDepth() {
		return this.currentDisplayMode.getBitDepth();
	}

	protected int currentRefreshRate() {
		return this.currentDisplayMode.getRefreshRate();
	}

	/**
	 * Creates an image compatible with the current display.
	 */
	public BufferedImage createCompatibleImage(int w, int h, int transparancy) {
		GraphicsConfiguration gc = frame.getGraphicsConfiguration();
		return gc.createCompatibleImage(w, h, transparancy);
	}

	public BufferedImage createCompatibleImage(int w, int h) {
		GraphicsConfiguration gc = frame.getGraphicsConfiguration();
		return gc.createCompatibleImage(w, h);
	}

	/**
	 * Returns the width of the window currently used in full screen mode. Returns 0 if the device is not in full screen mode.
	 */
	public int getWidth() {
		Window window = device.getFullScreenWindow();
		if (window != null) {
			return window.getWidth();
		} else {
			return 0;
		}
	}

	/**
	 * Returns the height of the window currently used in full screen mode. Returns 0 if the device is not in full screen mode.
	 */
	public int getHeight() {
		Window window = device.getFullScreenWindow();
		if (window != null) {
			return window.getHeight();
		} else {
			return 0;
		}
	}

	@Override
	public Panel getSidePanel() {
		return null;
	}
}
