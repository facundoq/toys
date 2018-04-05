package org.flightreservationsserverinterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import org.flightreservationsserverinterface.dtos.CustomerDTO;
import org.flightreservationsserverinterface.dtos.FlightDTO;
import org.flightreservationsserverinterface.dtos.FlightReservationDTO;
import org.flightreservationsserverinterface.dtos.ReservationAgencyDTO;
import org.flightreservationsserverinterface.exceptions.ErrorInServerException;
import org.flightreservationsserverinterface.exceptions.FlightFullException;
import org.flightreservationsserverinterface.exceptions.FlightReservationAlreadyCanceledException;
import org.flightreservationsserverinterface.exceptions.FlightReservationAlreadyPaidException;
import org.flightreservationsserverinterface.exceptions.InvalidReservationAgencyException;
import org.flightreservationsserverinterface.exceptions.NotFoundException;


/**
 * @author Facundo Quiroga
 * Creation date: Feb 3, 2009 11:20:35 PM
 */

public interface FlightReservationsInterface extends Remote {

	public FlightDTO getFlight(int id) throws ErrorInServerException, NotFoundException, RemoteException;
	
	public ArrayList<FlightDTO> getFlights(String source, String destination, String fromDate, String toDate) throws ErrorInServerException, RemoteException;

	public FlightReservationDTO getFlightReservation(int id) throws ErrorInServerException, NotFoundException, RemoteException;

	public void bookPassenger(FlightDTO flightDTO, CustomerDTO customerDTO, ReservationAgencyDTO reservationAgencyDTO, String passengerName) throws NotFoundException, ErrorInServerException, FlightFullException, RemoteException;
	
	public void cancelReservation(FlightReservationDTO flightReservation) throws FlightReservationAlreadyCanceledException, NotFoundException, ErrorInServerException, RemoteException;
	
	public void reservationPaid(FlightReservationDTO flightReservationDTO) throws FlightReservationAlreadyPaidException, ErrorInServerException, NotFoundException, FlightReservationAlreadyCanceledException, RemoteException;
	
	public CustomerDTO getCustomerByID(int id) throws NotFoundException, ErrorInServerException, RemoteException;

	public ArrayList<CustomerDTO> getCustomerByDNI(int dni) throws ErrorInServerException, RemoteException;

	public ArrayList<CustomerDTO> getCustomersByName(String name) throws ErrorInServerException, RemoteException;

	public void addCustomer(int dni, String name, int creditCardNumber) throws ErrorInServerException, RemoteException;
}
