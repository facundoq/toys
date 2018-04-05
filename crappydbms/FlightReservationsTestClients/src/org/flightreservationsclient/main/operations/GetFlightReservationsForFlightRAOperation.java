package org.flightreservationsclient.main.operations;

import java.rmi.RemoteException;
import java.util.Random;

import org.flightreservationsserverinterface.FlightReservationsAgencyInterface;
import org.flightreservationsserverinterface.dtos.FlightDTO;
import org.flightreservationsserverinterface.exceptions.ErrorInServerException;
import org.flightreservationsserverinterface.exceptions.NotFoundException;

/**
 * @author Facundo Quiroga
 * Creation date: Feb 8, 2009 7:36:23 PM
 */
public class GetFlightReservationsForFlightRAOperation extends Operation {

	protected  int maxID = 100;
	protected  int increase = 10;
	protected  double decreaseFactor = 1.5;
	
	public GetFlightReservationsForFlightRAOperation(FlightReservationsAgencyInterface flightReservationsInterface) {
		super();
		this.flightReservationsInterface = flightReservationsInterface;
	}

	protected FlightReservationsAgencyInterface flightReservationsInterface;

	@Override
	public void perform() throws ErrorInServerException, RemoteException {
		int randomID = new Random().nextInt(maxID);
		try {
			FlightDTO flightDTO = flightReservationsInterface.getFlight(randomID);
			flightReservationsInterface.getFlightReservationsFor(flightDTO);
			maxID = maxID+increase;
		} catch (NotFoundException e) {
			// Divide maxID by decreaseFactor
			int newMaxID =  (int) ( (double) maxID / decreaseFactor); 
			maxID = newMaxID;
		}
	}

}
