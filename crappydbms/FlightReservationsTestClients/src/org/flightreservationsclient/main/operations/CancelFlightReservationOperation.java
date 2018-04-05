package org.flightreservationsclient.main.operations;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Random;

import org.flightreservationsserverinterface.FlightReservationsAgencyInterface;
import org.flightreservationsserverinterface.dtos.CustomerDTO;
import org.flightreservationsserverinterface.dtos.FlightReservationDTO;
import org.flightreservationsserverinterface.exceptions.ErrorInServerException;
import org.flightreservationsserverinterface.exceptions.FlightReservationAlreadyCanceledException;
import org.flightreservationsserverinterface.exceptions.NotFoundException;

/**
 * @author Facundo Quiroga
 * Creation date: Feb 8, 2009 7:44:44 PM
 */
public class CancelFlightReservationOperation extends Operation {

	protected  int maxID = 100;
	protected  int increase = 10;
	protected  double decreaseFactor = 1.5;
	
	public CancelFlightReservationOperation(FlightReservationsAgencyInterface flightReservationsInterface) {
		super();
		this.flightReservationsInterface = flightReservationsInterface;
	}

	protected FlightReservationsAgencyInterface flightReservationsInterface;

	@Override
	public void perform() throws ErrorInServerException, RemoteException {
		int randomID = new Random().nextInt(maxID);
		try {
			CustomerDTO customerDTO = flightReservationsInterface.getCustomerByID(randomID);
			ArrayList<FlightReservationDTO> flightReservationDTOs = flightReservationsInterface.getFlightReservationsBy(customerDTO);
			maxID = maxID+increase;
			for (FlightReservationDTO flightReservationDTO : flightReservationDTOs) {
				int chanceToCancelAnyway = new Random().nextInt(100);
				if (!flightReservationDTO.isCancelled() || chanceToCancelAnyway <10 ) {
					try {
						flightReservationsInterface.cancelReservation(flightReservationDTO);
						break;
					} catch (FlightReservationAlreadyCanceledException e) {
					}
				}
			}
			
		} catch (NotFoundException e) {
			// Divide maxID by decreaseFactor
			int newMaxID =  (int) ( (double) maxID / decreaseFactor); 
			maxID = newMaxID;
		}
	}

}
