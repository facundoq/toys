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

package org.flightreservationsclient.main;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

import org.flightreservationsserverinterface.FlightReservationsAgencyInterface;
import org.flightreservationsserverinterface.FlightReservationsAirlineInterface;
import org.flightreservationsserverinterface.FlightReservationsLoginInterface;
import org.flightreservationsserverinterface.dtos.CustomerDTO;
import org.flightreservationsserverinterface.dtos.FlightDTO;
import org.flightreservationsserverinterface.dtos.FlightReservationDTO;
import org.flightreservationsserverinterface.dtos.ReservationAgencyDTO;
import org.flightreservationsserverinterface.exceptions.ErrorInServerException;
import org.flightreservationsserverinterface.exceptions.FlightFullException;
import org.flightreservationsserverinterface.exceptions.FlightReservationAlreadyCanceledException;
import org.flightreservationsserverinterface.exceptions.FlightReservationAlreadyPaidException;
import org.flightreservationsserverinterface.exceptions.NotFoundException;
import org.flightreservationsserverinterface.exceptions.ReservationAgencyRepeatedNameException;

/**
 * @author Facundo Quiroga Creation date: Feb 3, 2009 3:44:25 PM
 */
public class TestCorrectnessClient {
	static {
		String currentPath = null;
		try {
			currentPath = new File(".").getCanonicalPath();
			String policyFileName = "client.policy";
			String policyFile = "file://"+currentPath+"/"+policyFileName;
			System.setProperty("java.security.policy", policyFile );
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String args[]) {

		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		try {
			String name = "FlightReservationsSystem";
			Registry registry = LocateRegistry.getRegistry();
			FlightReservationsLoginInterface flightReservationsLoginInterface = (FlightReservationsLoginInterface) registry.lookup(name);
			FlightReservationsAirlineInterface flightReservationsAirlineInterface = flightReservationsLoginInterface.loginAsAirline("airline", "airline");
			testCustomers(flightReservationsAirlineInterface);
			testReservationAgencies(flightReservationsAirlineInterface);
			testFlights(flightReservationsAirlineInterface);

			FlightReservationsAgencyInterface flightReservationsAgencyInterface = flightReservationsLoginInterface.loginAsReservationAgency("ra1", "ra1");
			testFlightReservations(flightReservationsAgencyInterface);
			
			System.out.println("End client");
		} catch (Exception e) {
			System.err.println("Client exception:");
			e.printStackTrace();
		}
	}

	private static void testFlightReservations(FlightReservationsAgencyInterface flightReservationsAgencyInterface) throws NotFoundException, ErrorInServerException, FlightFullException, RemoteException {
		FlightDTO flightDTO= flightReservationsAgencyInterface.getFlights("china", "japan", "2009/02/02", "2009/02/02").get(0);
		ReservationAgencyDTO reservationAgencyDTO = flightReservationsAgencyInterface.getCurrentReservationAgency();
		CustomerDTO customerDTO = flightReservationsAgencyInterface.getCustomerByDNI(1).get(0);
		flightReservationsAgencyInterface.bookPassenger(flightDTO, customerDTO, reservationAgencyDTO, "juan");
		flightReservationsAgencyInterface.bookPassenger(flightDTO, customerDTO, reservationAgencyDTO, "juan");
		ArrayList<FlightReservationDTO> flightReservationDTOs = flightReservationsAgencyInterface.getFlightReservationsBy(customerDTO);
		assert(flightReservationDTOs.size() == 2);
		verifyFlightReservation(flightReservationDTOs.get(0),flightDTO,reservationAgencyDTO,customerDTO);
		verifyFlightReservation(flightReservationDTOs.get(1),flightDTO,reservationAgencyDTO,customerDTO);
		FlightReservationDTO flightReservationDTO1 = flightReservationsAgencyInterface.getFlightReservation((flightReservationDTOs.get(0).getId()));
		FlightReservationDTO flightReservationDTO2 = flightReservationsAgencyInterface.getFlightReservation((flightReservationDTOs.get(1).getId()));
		 flightReservationDTOs  = flightReservationsAgencyInterface.getFlightReservationsFor(flightDTO);
		 assert(flightReservationDTOs.size() == 2);
		verifyFlightReservation(flightReservationDTOs.get(0),flightDTO,reservationAgencyDTO,customerDTO);
		verifyFlightReservation(flightReservationDTOs.get(1),flightDTO,reservationAgencyDTO,customerDTO);
		
		testFlightReservationPaymentAndCancelling(flightReservationsAgencyInterface, flightReservationDTO1, flightReservationDTO2);
	}

	private static void testFlightReservationPaymentAndCancelling(FlightReservationsAgencyInterface flightReservationsAgencyInterface, FlightReservationDTO flightReservationDTO1, FlightReservationDTO flightReservationDTO2) throws ErrorInServerException, NotFoundException, RemoteException {
		try {
			flightReservationsAgencyInterface.reservationPaid(flightReservationDTO1);
		} catch (FlightReservationAlreadyPaidException e1) {
			assert(false);
		} catch (FlightReservationAlreadyCanceledException e1) {
			assert(false);
		}
		
		try {
			flightReservationsAgencyInterface.reservationPaid(flightReservationDTO1);
			assert(false);
		} catch (FlightReservationAlreadyPaidException e1) {
		} catch (FlightReservationAlreadyCanceledException e1) {
			assert(false);
		}
		
		try {
			flightReservationsAgencyInterface.cancelReservation(flightReservationDTO2);
		} catch (FlightReservationAlreadyCanceledException e) {
			assert(false);
		}
		try {
			flightReservationsAgencyInterface.cancelReservation(flightReservationDTO2);
			assert(false);
		} catch (FlightReservationAlreadyCanceledException e) {}
	}

	private static void verifyFlightReservation(FlightReservationDTO flightReservationDTO, FlightDTO flightDTO, ReservationAgencyDTO reservationAgencyDTO, CustomerDTO customerDTO) {
		assert(flightReservationDTO.getFlight().getId() == flightDTO.getId());
		assert(flightReservationDTO.getReservationAgency().getId() == reservationAgencyDTO.getId());
		assert(flightReservationDTO.getCustomer().getId() == customerDTO.getId());
		assert(flightReservationDTO.getPassengerName().equals("juan"));
	}

	private static void testFlights(FlightReservationsAirlineInterface flightReservationsAirlineInterface) throws ErrorInServerException, RemoteException, NotFoundException {
		flightReservationsAirlineInterface.addFlight("china", "japan", "2009/02/02", 30);
		ArrayList<FlightDTO> flights = flightReservationsAirlineInterface.getFlights("china", "japan", "2009/02/02", "2009/02/02");
		assert (flights.size() == 1);
		verifyFlight(flights.get(0));
		FlightDTO flightDTO = flightReservationsAirlineInterface.getFlight(flights.get(0).getId());
		verifyFlight(flightDTO);
	}

	private static void verifyFlight(FlightDTO flightDTO) {
		assert (flightDTO.getSource().equals("china"));
		assert (flightDTO.getDestination().equals("japan"));
		assert (flightDTO.getDate().equals("2009/02/02"));
		assert (flightDTO.getOccupiedSeats() == 0);
		assert (flightDTO.getMaximumSeats() == 30);

	}

	private static void testReservationAgencies(FlightReservationsAirlineInterface flightReservationsAirlineInterface) throws ErrorInServerException, RemoteException, NotFoundException {
		try {
			flightReservationsAirlineInterface.addReservationAgency("ra1", "ra1");
		} catch (ReservationAgencyRepeatedNameException e) {
			assert(false);
		}
		ReservationAgencyDTO reservationAgencyDTO = flightReservationsAirlineInterface.getReservationAgency("ra1");
		verifyReservationAgency(reservationAgencyDTO);
		reservationAgencyDTO = flightReservationsAirlineInterface.getReservationAgency(reservationAgencyDTO.getId());
		verifyReservationAgency(reservationAgencyDTO);
	}

	private static void verifyReservationAgency(ReservationAgencyDTO reservationAgencyDTO) {
		assert (reservationAgencyDTO.getName().equals("ra1"));
		assert (reservationAgencyDTO.getPassword().equals("ra1"));
	}

	private static void testCustomers(FlightReservationsAirlineInterface flightReservationsAirlineInterface) throws ErrorInServerException, RemoteException, NotFoundException {
		flightReservationsAirlineInterface.addCustomer(1, "jose", 1);
		ArrayList<CustomerDTO> customerDTOs = flightReservationsAirlineInterface.getCustomersByName("jose");
		assert (customerDTOs.size() == 1);
		verifyCustomer(customerDTOs.get(0));
		CustomerDTO customerDTO = flightReservationsAirlineInterface.getCustomerByID(customerDTOs.get(0).getId());
		verifyCustomer(customerDTO);
		customerDTOs = flightReservationsAirlineInterface.getCustomerByDNI(1);
		assert (customerDTOs.size() == 1);
		verifyCustomer(customerDTOs.get(0));
	}

	private static void verifyCustomer(CustomerDTO customerDTO) {
		assert (customerDTO.getDni() == 1);
		assert (customerDTO.getCreditCardNumber() == 1);
		assert (customerDTO.getName().equals("jose"));
	}

}
