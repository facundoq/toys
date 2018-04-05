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

package org.tiledzelda.main.resourcemanagement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

public class Configuration {
	private Hashtable<String, String> dictionary = new Hashtable<String, String>();

	private static Configuration configuration;

	public static Configuration getInstance() {
		if (configuration == null) {
			configuration = new Configuration();
		}

		return configuration;
	}

	private Configuration() {
		String userDir = System.getProperty("user.dir") + "/";
		dictionary.put("applicationPath", userDir);
		try {
			BufferedReader br = new BufferedReader(new FileReader(userDir + "configuration/config.ini"));
			String line;
			while ((line = br.readLine()) != null) {
				if (line.length() > 0 && !line.startsWith("//")) {
					String[] words = line.split("=");
					if (words.length == 2) {
						dictionary.put(words[0], words[1]);
					}
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO cannot continue if config file is not found; add logging!
			e.printStackTrace();
		} catch (IOException e) {
			// TODO cannot continue if there is an error reading the config file; add logging!
			e.printStackTrace();
		} finally {

		}
	}

	// USAGE
	/* public void loadValues(String nameConfiguracion){
	 	
	 	 	           		
	   }*/

	public String getValue(String key) {
		return (dictionary.get(key));
	}

	public int getValueAsInt(String key) {
		return (new Integer(this.getValue(key))).intValue();
	}

	public void setValue(String key, String value) {
		dictionary.put(key, value);
	}

	public Hashtable<String, String> getAll() {
		return this.dictionary;
	}

	/**
	 * @return
	 */
	public String getRootDirectory() {
		return "";
	}

	public File getRootDirectoryFile() {
		return new File(this.getRootDirectory());
	}
}