package org.flightreservationsserverinterface;

import java.rmi.RemoteException;
import java.util.ArrayList;

import org.flightreservationsserverinterface.dtos.CustomerDTO;
import org.flightreservationsserverinterface.dtos.FlightDTO;
import org.flightreservationsserverinterface.dtos.FlightReservationDTO;
import org.flightreservationsserverinterface.dtos.ReservationAgencyDTO;
import org.flightreservationsserverinterface.exceptions.ErrorInServerException;
import org.flightreservationsserverinterface.exceptions.FlightReservationAlreadyCanceledException;
import org.flightreservationsserverinterface.exceptions.FlightReservationAlreadyPaidException;
import org.flightreservationsserverinterface.exceptions.InvalidReservationAgencyException;
import org.flightreservationsserverinterface.exceptions.NotFoundException;
import org.flightreservationsserverinterface.exceptions.ReservationAgencyRepeatedNameException;



/**
 * @author Facundo Quiroga
 * Creation date: Feb 3, 2009 11:21:59 PM
 */
public interface FlightReservationsAirlineInterface extends FlightReservationsInterface {
	
	public void addReservationAgency(String name, String password) throws ErrorInServerException, RemoteException, ReservationAgencyRepeatedNameException;

	public ReservationAgencyDTO getReservationAgency(int id) throws NotFoundException, ErrorInServerException, RemoteException;
	public ReservationAgencyDTO getReservationAgency(String name) throws ErrorInServerException, NotFoundException, RemoteException;
	public void addFlight(String source, String destination, String date,int maximumSeats) throws ErrorInServerException, RemoteException;
	public ArrayList<FlightReservationDTO> getFlightReservationsFor(FlightDTO flightDTO, ReservationAgencyDTO reservationAgencyDTO) throws NotFoundException, ErrorInServerException, RemoteException;

	public ArrayList<FlightReservationDTO> getFlightReservationsBy(CustomerDTO customerDTO, ReservationAgencyDTO reservationAgencyDTO) throws NotFoundException, ErrorInServerException, RemoteException;
	

}
