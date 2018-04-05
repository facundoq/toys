package org.flightreservationsserverinterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

import org.flightreservationsserverinterface.exceptions.ErrorInServerException;
import org.flightreservationsserverinterface.exceptions.InvalidLoginException;



/**
 * @author Facundo Quiroga
 * Creation date: Feb 3, 2009 10:59:20 PM
 */

public interface FlightReservationsLoginInterface extends Remote {
	
	

	/**
	 * @param username
	 * @param password
	 * @return
	 * @throws InvalidLoginException
	 * @throws ErrorInServerException
	 */
	FlightReservationsAirlineInterface loginAsAirline(String username, String password) throws InvalidLoginException, ErrorInServerException, RemoteException;

	/**
	 * @param reservationAgencyName
	 * @param password
	 * @return
	 * @throws InvalidLoginException
	 * @throws ErrorInServerException
	 */
	FlightReservationsAgencyInterface loginAsReservationAgency(String reservationAgencyName, String password) throws InvalidLoginException,
			ErrorInServerException, RemoteException;

}
