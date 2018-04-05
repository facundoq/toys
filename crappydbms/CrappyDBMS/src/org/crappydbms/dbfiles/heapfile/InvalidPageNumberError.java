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
package org.crappydbms.dbfiles.heapfile;

import org.crappydbms.exceptions.CrappyDBMSError;

/**
 * @author Facundo Manuel Quiroga
 * Nov 22, 2008
 * 
 */
public class InvalidPageNumberError extends CrappyDBMSError {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1777479085932504714L;

	public InvalidPageNumberError(String string) {
		super(string);
	}

}
