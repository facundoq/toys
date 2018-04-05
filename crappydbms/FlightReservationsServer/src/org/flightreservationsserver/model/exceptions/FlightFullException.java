/**
 * 
 */
package org.flightreservationsserver.model.exceptions;

/**
 * @author Facundo Manuel Quiroga
 * Jan 31, 2009
 * 
 */
public class FlightFullException extends FlightReservationsServerException {

	/**
	 * @param string
	 */
	public FlightFullException(String message) {
		super(message);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -1343339616124318315L;

}
