
package org.flightreservationsserverinterface;

import java.rmi.RemoteException;
import java.util.ArrayList;

import org.flightreservationsserverinterface.dtos.CustomerDTO;
import org.flightreservationsserverinterface.dtos.FlightDTO;
import org.flightreservationsserverinterface.dtos.FlightReservationDTO;
import org.flightreservationsserverinterface.dtos.ReservationAgencyDTO;
import org.flightreservationsserverinterface.exceptions.ErrorInServerException;
import org.flightreservationsserverinterface.exceptions.NotFoundException;


/**
 * @author Facundo Quiroga
 * Creation date: Feb 3, 2009 10:30:58 PM
 */
public interface FlightReservationsAgencyInterface extends FlightReservationsInterface {
	
	public ReservationAgencyDTO getCurrentReservationAgency() throws  RemoteException;

	public ArrayList<FlightReservationDTO> getFlightReservationsFor(FlightDTO flightDTO) throws NotFoundException, ErrorInServerException, RemoteException;

	public ArrayList<FlightReservationDTO> getFlightReservationsBy(CustomerDTO customerDTO) throws NotFoundException, ErrorInServerException, RemoteException;


}
