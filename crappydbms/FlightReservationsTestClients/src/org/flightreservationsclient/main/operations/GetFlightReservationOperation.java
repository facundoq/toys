package org.flightreservationsclient.main.operations;

import java.rmi.RemoteException;
import java.util.Random;

import org.flightreservationsserverinterface.FlightReservationsInterface;
import org.flightreservationsserverinterface.exceptions.ErrorInServerException;
import org.flightreservationsserverinterface.exceptions.NotFoundException;

/**
 * @author Facundo Quiroga
 * Creation date: Feb 8, 2009 6:40:45 PM
 */
public class GetFlightReservationOperation extends Operation {

	protected  int maxID = 100;
	protected  int increase = 10;
	protected  double decreaseFactor = 1.5;
	
	public GetFlightReservationOperation(FlightReservationsInterface flightReservationsInterface) {
		super();
		this.flightReservationsInterface = flightReservationsInterface;
	}

	protected FlightReservationsInterface flightReservationsInterface;

	@Override
	public void perform() throws ErrorInServerException, RemoteException {
		int randomID = new Random().nextInt(maxID);
		try {
			flightReservationsInterface.getFlightReservation(randomID);
			maxID = maxID+increase;
		} catch (NotFoundException e) {
			// Divide maxID by decreaseFactor
			int newMaxID =  (int) ( (double) maxID / decreaseFactor); 
			maxID = newMaxID;
		}
	}

}
