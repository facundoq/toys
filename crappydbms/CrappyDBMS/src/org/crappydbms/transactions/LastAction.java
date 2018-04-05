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
package org.crappydbms.transactions;

/**
 * @author Facundo Manuel Quiroga
 * Jan 21, 2009
 * 
 */
public class LastAction {

	protected String description;
	protected long timestamp;
	/**
	 *  Create an action with a description and the System.currentTimeMillis as the timestamp
	 */
	public LastAction(String description) {
		this(description,System.currentTimeMillis());
	}
	
	/**
	 *  Create an action with a description and a timestamp
	 */
	public LastAction(String description, long timestamp) {
		this.setTimestamp(timestamp);
		this.setDescription(description);
	}
	
	public long getTimestamp() {
		return this.timestamp;
	}
	protected void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getDescription() {
		return this.description;
	}

	protected void setDescription(String description) {
		this.description = description;
	}

}
