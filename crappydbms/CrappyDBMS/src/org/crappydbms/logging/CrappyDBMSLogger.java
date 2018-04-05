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
 * @author Facundo Manuel Quiroga Nov 16, 2008
 * 
 */
public class CrappyDBMSLogger extends Logger {
	static ConsoleHandler consoleHandler= null;
	static StreamHandler streamHandler= null;

	static FileHandler fileHandler = null;
	static Logger globalLogger;
	
	public static Logger getLogger(String name) {
		Logger logger = Logger.getLogger(name);
		try {
			if (globalLogger == null){
				globalLogger = Logger.getLogger("org.crappydbms");
				CrappyDBMSLogger.initializeHandlers();
				//globalLogger.addHandler(streamHandler);
				//globalLogger.addHandler(fileHandler);
				//globalLogger.addHandler(consoleHandler);
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
		if (CrappyDBMSLogger.fileHandler == null) {
			CrappyDBMSLogger.fileHandler = new FileHandler("log.txt");
			fileHandler.setFormatter(new SimpleFormatter());
			fileHandler.setLevel(Level.ALL);
		}
		if (CrappyDBMSLogger.streamHandler == null) {
			CrappyDBMSLogger.streamHandler = new StreamHandler(System.err,new SimpleFormatter());
			streamHandler.setLevel(Level.ALL);
		}
		if (CrappyDBMSLogger.consoleHandler == null) {
			CrappyDBMSLogger.consoleHandler  = new ConsoleHandler();
			consoleHandler.setFormatter(new SimpleFormatter());
			consoleHandler.setLevel(Level.ALL);
		}
	}

	protected CrappyDBMSLogger(String name, String resourceBundleName) {
		super(name, resourceBundleName);
	}
}