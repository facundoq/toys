package org.flightreservationsserverinterface.exceptions;

/**
 * @author Facundo Quiroga
 * Creation date: Feb 3, 2009 11:25:48 PM
 */
public class ErrorInServerException extends FlightReservationServerException {

	public ErrorInServerException(String string) {
		super(string);
	}

	public ErrorInServerException() {
		super();
	}

}
