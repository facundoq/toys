package org.flightreservationsclient.main.operations;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Random;

import org.flightreservationsserverinterface.FlightReservationsInterface;
import org.flightreservationsserverinterface.dtos.CustomerDTO;
import org.flightreservationsserverinterface.exceptions.ErrorInServerException;


/**
 * @author Facundo Quiroga Creation date: Feb 8, 2009 5:30:36 PM
 */
public class GetCustomerByNameOperation extends Operation {
	
	public static volatile int maxID = 100;
	protected  int increase = 30;
	protected  double decreaseFactor = 1.5;

	public GetCustomerByNameOperation(FlightReservationsInterface flightReservationsInterface) {
		super();
		this.flightReservationsInterface = flightReservationsInterface;
	}

	protected FlightReservationsInterface flightReservationsInterface;

	@Override
	public void perform() throws ErrorInServerException, RemoteException {
		int randomID = new Random().nextInt(maxID);

		ArrayList<CustomerDTO> customers = flightReservationsInterface.getCustomersByName("customer" + randomID);
		maxID = maxID + increase;
		if (customers.size() == 0){
			// Divide maxID by decreaseFactor
			int newMaxID = (int) ((double) maxID / decreaseFactor);
			maxID = newMaxID;
		}
	}
}
