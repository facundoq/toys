package org.flightreservationsserverinterface.exceptions;

/**
 * @author Facundo Quiroga
 * Creation date: Feb 3, 2009 11:23:39 PM
 */
public class FlightReservationServerException extends Exception {

	public FlightReservationServerException(String string) {
		super(string);
	}

	public FlightReservationServerException() {
		super();
	}

}
