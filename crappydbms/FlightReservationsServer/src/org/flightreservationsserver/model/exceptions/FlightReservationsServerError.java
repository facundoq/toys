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

package org.flightreservationsserver.model.exceptions;

/**
 * @author Facundo Quiroga
 * Creation date: Jan 28, 2009 6:54:06 PM
 */
public class FlightReservationsServerError extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3785938565731112264L;

	public FlightReservationsServerError(String message) {
		super(message);
	}

}