/**
 * 
 */
package org.flightreservationsserver.model;

import java.io.IOException;
import java.util.ArrayList;

import org.crappydbms.exceptions.CrappyDBMSException;
import org.crappydbms.transactions.TransactionAbortedException;
import org.flightreservationsserver.database.CustomerDAO;
import org.flightreservationsserver.database.DAO;
import org.flightreservationsserver.database.FlightDAO;
import org.flightreservationsserver.database.FlightReservationDAO;
import org.flightreservationsserver.database.NotFoundException;
import org.flightreservationsserver.database.ReservationAgencyDAO;
import org.flightreservationsserver.model.exceptions.FlightFullException;
import org.flightreservationsserver.model.exceptions.FlightReservationAlreadyCanceledException;
import org.flightreservationsserver.model.exceptions.FlightReservationsServerException;
import org.flightreservationsserverinterface.exceptions.FlightReservationAlreadyPaidException;
import org.flightreservationsserverinterface.exceptions.ReservationAgencyRepeatedNameException;

/**
 * @author Facundo Manuel Quiroga Jan 24, 2009
 */
public class FlightReservationsSystem {

	public FlightReservationsSystem() throws FlightReservationsServerException, IOException {

		DAO.initialize();

	}

	// FLIGHTS
	public Flight addFlight(String source, String destination, String date, int maximumSeats) throws TransactionAbortedException {
		Flight flight = new Flight(-1, date, source, destination, maximumSeats);
		return FlightDAO.addFlight(flight);
	}

	public Flight getFlight(int id) throws TransactionAbortedException, NotFoundException {
		return FlightDAO.getFlight(id);
	}

	public ArrayList<Flight> getFlights(String source, String destination, String fromDate, String toDate) throws TransactionAbortedException {
		return FlightDAO.getFlights(source, destination, fromDate, toDate);
	}

	// FLIGHT RESERVATIONS
	public FlightReservation getFlightReservation(int id) throws NotFoundException, TransactionAbortedException {
		return FlightReservationDAO.getFlightReservation(id);
	}

	public ArrayList<FlightReservation> getFlightReservationsFor(Flight flight, ReservationAgency reservationAgency) throws TransactionAbortedException {
		return FlightReservationDAO.getFlightReservationsFor(flight, reservationAgency);
	}

	public ArrayList<FlightReservation> getFlightReservationsBy(Customer customer, ReservationAgency reservationAgency) throws TransactionAbortedException {
		return FlightReservationDAO.getFlightReservationsBy(customer, reservationAgency);
	}

	public FlightReservation bookPassenger(Flight flight, Customer customer, ReservationAgency reservation, String passengerName) throws TransactionAbortedException, FlightFullException {
		FlightReservation flightReservation;
		flightReservation = flight.bookPassenger(customer, reservation, passengerName);
		FlightDAO.saveFlight(flight);
		return FlightReservationDAO.addFlightReservation(flightReservation);
	}

	public void cancelReservation(FlightReservation flightReservation) throws FlightReservationAlreadyCanceledException, TransactionAbortedException {
		flightReservation.cancel();
		FlightDAO.saveFlight(flightReservation.getFlight());
		FlightReservationDAO.saveFlightReservation(flightReservation);
	}
	
	public void reservationPaid(FlightReservation flightReservation) throws FlightReservationAlreadyCanceledException, TransactionAbortedException, FlightReservationAlreadyPaidException {
		flightReservation.paid();
		FlightReservationDAO.saveFlightReservation(flightReservation);
	}

	// CUSTOMERS
	public Customer getCustomerByID(int id) throws NotFoundException, TransactionAbortedException {
		return CustomerDAO.getCustomerByID(id);
	}

	public ArrayList<Customer> getCustomerByDNI(int dni) throws TransactionAbortedException {
		return CustomerDAO.getCustomerByDNI(dni);
	}

	public ArrayList<Customer> getCustomersByName(String name) throws TransactionAbortedException {
		return CustomerDAO.getCustomersByName(name);
	}

	public Customer addCustomer(int dni, String name, int creditCardNumber) throws TransactionAbortedException {
		Customer customer = new Customer(-1, name, dni, creditCardNumber);
		return CustomerDAO.addCustomer(customer);
	}

	// RESERVATION AGENCIES
	public ReservationAgency addReservationAgency(String name, String password) throws TransactionAbortedException, ReservationAgencyRepeatedNameException {
		ReservationAgency reservationAgency = new ReservationAgency(-1, name, password);
		return ReservationAgencyDAO.addReservationAgency(reservationAgency);
	}

	public ReservationAgency getReservationAgency(int id) throws NotFoundException, TransactionAbortedException {
		return ReservationAgencyDAO.getReservationAgency(id);
	}

	public ReservationAgency getReservationAgency(String name) throws NotFoundException, TransactionAbortedException {
		return ReservationAgencyDAO.getReservationAgency(name);
	}

	public void shutdown() throws CrappyDBMSException {
		DAO.database.shutDown();

	}

}
