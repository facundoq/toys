package org.flightreservationsclient.main.operations;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Random;

import org.flightreservationsclient.main.utils.RandomDateGenerator;
import org.flightreservationsserverinterface.FlightReservationsInterface;
import org.flightreservationsserverinterface.exceptions.ErrorInServerException;

/**
 * @author Facundo Quiroga
 * Creation date: Feb 8, 2009 5:29:56 PM
 */
public class GetFlightsOperation extends Operation {

	public GetFlightsOperation(ArrayList<String> countries, FlightReservationsInterface flightReservationsInterface, int minYear, int maxYear) {
		super();
		this.countries = countries;
		this.flightReservationsInterface = flightReservationsInterface;
		this.minYear = minYear;
		this.maxYear = maxYear;
	}

	protected ArrayList<String> countries;
	protected FlightReservationsInterface flightReservationsInterface;
	protected int minYear, maxYear;

	@Override
	public void perform() throws ErrorInServerException, RemoteException {
		RandomDateGenerator randomDateGenerator = new RandomDateGenerator(this.minYear,this.maxYear);
		
		String date1 = randomDateGenerator.generateRandomDate();
		String date2 = randomDateGenerator.generateRandomDate();
		String fromDate, toDate;
		if (date1.compareTo(date2) > 0){
			fromDate = date2;
			toDate = date1;
		}else{
			fromDate = date1;
			toDate = date2;
		}
		int sourceCountryIndex = new Random().nextInt(this.countries.size());
		String source = this.countries.get(sourceCountryIndex);
		int destinationCountryIndex = (sourceCountryIndex+1) % this.countries.size();
		String destination = this.countries.get(destinationCountryIndex);
		flightReservationsInterface.getFlights(source, destination, fromDate, toDate);
	}

}
