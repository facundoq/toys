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
package org.crappydbms.queries.operators.join;

import org.crappydbms.exceptions.CrappyDBMSError;


/**
 * @author Facundo Manuel Quiroga
 * Dec 9, 2008
 * 
 */
public class RelationSchemasHaveCommonAttributesException extends CrappyDBMSError {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4894628749549162260L;

	/**
	 * @param string
	 */
	public RelationSchemasHaveCommonAttributesException(String message) {
		super(message);
	}

}
