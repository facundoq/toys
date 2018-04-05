package org.flightreservationsclient.main.operations;

import java.rmi.RemoteException;
import java.util.Random;

import org.flightreservationsserverinterface.FlightReservationsAgencyInterface;
import org.flightreservationsserverinterface.dtos.CustomerDTO;
import org.flightreservationsserverinterface.exceptions.ErrorInServerException;
import org.flightreservationsserverinterface.exceptions.NotFoundException;

/**
 * @author Facundo Quiroga
 * Creation date: Feb 8, 2009 6:56:01 PM
 */
public class GetFlightReservationsByCustomerRAOperation extends Operation {

	protected  int maxID = 100;
	protected  int increase = 10;
	protected  double decreaseFactor = 1.5;
	
	public GetFlightReservationsByCustomerRAOperation(FlightReservationsAgencyInterface flightReservationsInterface) {
		super();
		this.flightReservationsInterface = flightReservationsInterface;
	}

	protected FlightReservationsAgencyInterface flightReservationsInterface;

	@Override
	public void perform() throws ErrorInServerException, RemoteException {
		int randomID = new Random().nextInt(maxID);
		try {
			CustomerDTO customerDTO = flightReservationsInterface.getCustomerByID(randomID);
			flightReservationsInterface.getFlightReservationsBy(customerDTO);
			maxID = maxID+increase;
		} catch (NotFoundException e) {
			// Divide maxID by decreaseFactor
			int newMaxID =  (int) ( (double) maxID / decreaseFactor); 
			maxID = newMaxID;
		}
	}
}
