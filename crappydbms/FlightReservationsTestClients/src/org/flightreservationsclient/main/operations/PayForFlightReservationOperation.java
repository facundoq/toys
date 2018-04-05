package org.flightreservationsclient.main.operations;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Random;

import org.flightreservationsserverinterface.FlightReservationsAgencyInterface;
import org.flightreservationsserverinterface.dtos.CustomerDTO;
import org.flightreservationsserverinterface.dtos.FlightReservationDTO;
import org.flightreservationsserverinterface.exceptions.ErrorInServerException;
import org.flightreservationsserverinterface.exceptions.FlightReservationAlreadyCanceledException;
import org.flightreservationsserverinterface.exceptions.FlightReservationAlreadyPaidException;
import org.flightreservationsserverinterface.exceptions.NotFoundException;

/**
 * @author Facundo Quiroga
 * Creation date: Feb 8, 2009 7:53:05 PM
 */
public class PayForFlightReservationOperation extends Operation {


	protected  int maxID = 100;
	protected  int increase = 10;
	protected  double decreaseFactor = 1.5;
	
	public PayForFlightReservationOperation(FlightReservationsAgencyInterface flightReservationsInterface) {
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
				int chanceToPayAnyway = new Random().nextInt(100);
				boolean notPaidOrCancelled = !flightReservationDTO.isPaid() && !flightReservationDTO.isPaid();
				if ( notPaidOrCancelled  || chanceToPayAnyway <10 ) {
					try {
						flightReservationsInterface.reservationPaid(flightReservationDTO);
						break;
					} catch (FlightReservationAlreadyCanceledException e) {
					} catch (FlightReservationAlreadyPaidException e) {
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
