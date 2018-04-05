/* CrappyDBMS, a simple relational DBMS written in Java.
    Copyright (C) 2008  Facundo Manuel Quiroga <facundoq@gmail.com>
 	
 	This file is part of CrappyDBMS.

    CrappyDBMS is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    CrappyDBMS is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with CrappyDBMS.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.crappydbms.logging;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

/**
 * @author Facundo Manuel Quiroga
 * Jan 9, 2009
 * 
 */
public class CrappyDBMSTestLogger extends Logger {
	
	static ConsoleHandler consoleHandler= null;
	static StreamHandler streamHandler= null;

	static FileHandler fileHandler = null;
	static Logger globalLogger;
	public static Logger getLogger(String name) {
		Logger logger = Logger.getLogger(name);
		logger.setLevel(Level.ALL);
		try {
			if (globalLogger == null){
				globalLogger = Logger.getLogger("org.crappydbms.test");
				initializeHandlers();
				globalLogger.addHandler(streamHandler);
				globalLogger.addHandler(fileHandler);
				globalLogger.addHandler(consoleHandler);
				globalLogger.setLevel(Level.ALL);
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return logger;
	}

	private static void initializeHandlers() throws SecurityException, IOException {
		if (fileHandler == null) {
			fileHandler = new FileHandler("testLog.txt");
			fileHandler.setFormatter(new SimpleFormatter());
			fileHandler.setLevel(Level.ALL);
		}
		if (streamHandler == null) {
			streamHandler = new StreamHandler(System.err,new SimpleFormatter());
			streamHandler.setLevel(Level.ALL);
		}
		if (consoleHandler == null) {
			consoleHandler  = new ConsoleHandler();
			consoleHandler.setFormatter(new SimpleFormatter());
			consoleHandler.setLevel(Level.ALL);
		}
	}

	protected CrappyDBMSTestLogger(String name, String resourceBundleName) {
		super(name, resourceBundleName);
	}

}
