package org.flightreservationsclient.main.operations;

import java.rmi.RemoteException;
import java.util.Random;

import org.flightreservationsserverinterface.FlightReservationsAgencyInterface;
import org.flightreservationsserverinterface.dtos.CustomerDTO;
import org.flightreservationsserverinterface.dtos.FlightDTO;
import org.flightreservationsserverinterface.exceptions.ErrorInServerException;
import org.flightreservationsserverinterface.exceptions.FlightFullException;
import org.flightreservationsserverinterface.exceptions.NotFoundException;

/**
 * @author Facundo Quiroga
 * Creation date: Feb 8, 2009 7:37:25 PM
 */
public class BookFlightReservationOperation extends Operation {

	protected  int customerMaxID = 100;
	protected  int flightMaxID = 100;
	protected  int increase = 10;
	protected  double decreaseFactor = 1.5;

	
	public BookFlightReservationOperation(FlightReservationsAgencyInterface flightReservationsInterface) {
		super();
		this.flightReservationsInterface = flightReservationsInterface;
	}

	protected FlightReservationsAgencyInterface flightReservationsInterface;

	@Override
	public void perform() throws ErrorInServerException, RemoteException {
		int randomCustomerID = new Random().nextInt(customerMaxID);
		try {
			CustomerDTO customerDTO = flightReservationsInterface.getCustomerByID(randomCustomerID);
			customerMaxID = customerMaxID+increase;
			try{
				int randomFlightID = new Random().nextInt(flightMaxID);
				FlightDTO flightDTO = flightReservationsInterface.getFlight(randomFlightID);
				flightMaxID = flightMaxID+increase;
				try {
					flightReservationsInterface.bookPassenger(flightDTO, customerDTO, flightReservationsInterface.getCurrentReservationAgency(), "juan");
				} catch (FlightFullException e) {
					
				}
			} catch (NotFoundException e) {
				// Divide maxID by decreaseFactor
				int newMaxID =  (int) ( (double) flightMaxID / decreaseFactor); 
				flightMaxID = newMaxID;
			}

		} catch (NotFoundException e) {
			// Divide maxID by decreaseFactor
			int newMaxID =  (int) ( (double) customerMaxID / decreaseFactor); 
			customerMaxID = newMaxID;
		}
	}

}
