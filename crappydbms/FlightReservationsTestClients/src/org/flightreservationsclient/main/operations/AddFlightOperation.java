package org.flightreservationsclient.main.operations;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Random;

import org.flightreservationsclient.main.utils.RandomDateGenerator;
import org.flightreservationsserverinterface.FlightReservationsAirlineInterface;
import org.flightreservationsserverinterface.exceptions.ErrorInServerException;

/**
 * @author Facundo Quiroga
 * Creation date: Feb 8, 2009 5:29:29 PM
 */
public class AddFlightOperation extends Operation {
	


	public AddFlightOperation(ArrayList<String> countries, FlightReservationsAirlineInterface flightReservationsAirlineInterface, int minYear, int maxYear) {
		super();
		this.countries = countries;
		this.flightReservationsAirlineInterface = flightReservationsAirlineInterface;
		this.maxYear = maxYear;
		this.minYear = minYear;
	}

	protected ArrayList<String> countries;
	protected FlightReservationsAirlineInterface flightReservationsAirlineInterface;
	protected int minYear, maxYear;


	@Override
	public void perform() throws ErrorInServerException, RemoteException {
		RandomDateGenerator randomDateGenerator = new RandomDateGenerator(this.minYear,this.maxYear);
		String date = randomDateGenerator.generateRandomDate();
		int sourceCountryIndex = new Random().nextInt(this.countries.size());
		String source = this.countries.get(sourceCountryIndex);
		int destinationCountryIndex = (sourceCountryIndex+1) % this.countries.size();
		String destination = this.countries.get(destinationCountryIndex);
		int maximumSeats = new Random().nextInt(20)+20;
		flightReservationsAirlineInterface.addFlight(source, destination, date, maximumSeats);
	}

}
