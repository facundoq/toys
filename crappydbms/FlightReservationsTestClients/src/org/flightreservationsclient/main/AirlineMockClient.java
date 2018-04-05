package org.flightreservationsclient.main;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import org.flightreservationsclient.main.operations.AddCustomerOperation;
import org.flightreservationsclient.main.operations.AddFlightOperation;
import org.flightreservationsclient.main.operations.GetCustomerByIDOperation;
import org.flightreservationsclient.main.operations.GetCustomerByNameOperation;
import org.flightreservationsclient.main.operations.GetFlightOperation;
import org.flightreservationsclient.main.operations.GetFlightReservationOperation;
import org.flightreservationsclient.main.operations.GetFlightsOperation;
import org.flightreservationsclient.main.operations.GetReservationAgencyByName;
import org.flightreservationsclient.main.operations.Operation;
import org.flightreservationsclient.main.utils.RandomChooser;
import org.flightreservationsserverinterface.FlightReservationsAirlineInterface;
import org.flightreservationsserverinterface.FlightReservationsLoginInterface;
import org.flightreservationsserverinterface.exceptions.ErrorInServerException;
import org.managerinterface.ManagerInterface;

/**
 * @author Facundo Quiroga Creation date: Feb 5, 2009 2:12:19 PM
 */
public class AirlineMockClient extends MockClient {

	private static final long sleepTimeBetweenOperations = 100;

	public AirlineMockClient(String serverName, String host, int port, int id) {
		super(serverName, host, port, id);
	}

	public void runTest() throws Exception {
		System.out.println("Airline Client " + this.id + " started");
		String user = "airline";
		String password = "airline";
		try {
			ManagerInterface managerInterface = this.obtainManagerInterface();
			;
			try {
				managerInterface.airlineStarted(this.id);
				FlightReservationsLoginInterface flightReservationsLoginInterface = this.obtainLoginInterface();
				FlightReservationsAirlineInterface flightReservationsAirlineInterface = flightReservationsLoginInterface.loginAsAirline(user, password);

				// TODO stress test
				while (managerInterface.shouldContinue()) {
					try {
						this.doTest(flightReservationsAirlineInterface);
					} catch (ErrorInServerException e) {

					}
					Thread.sleep(sleepTimeBetweenOperations);
				}
				managerInterface.airlineFinished(this.id);
				System.out.println("End client " + this.id);

			} catch (Exception e) {
				managerInterface.airlineAborted(this.id, "Exception " + e.getClass() + " message:" + e.getMessage());
			} finally {

			}
		} catch (AccessException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}

	private void doTest(FlightReservationsAirlineInterface flightReservationsAirlineInterface) throws ErrorInServerException, RemoteException {
		for (int i = 0; i < numberOfOperationsPerIteration; i++) {
			this.performOperation(flightReservationsAirlineInterface);
		}

	}

	protected void performOperation(FlightReservationsAirlineInterface flightReservationsAirlineInterface) throws ErrorInServerException, RemoteException {
		ArrayList<String> countries = StressTestUtils.getCountries();
		int minYear = StressTestUtils.getMinYear();
		int maxYear = StressTestUtils.getMaxYear();
		Operation[] operations = { new AddFlightOperation(countries, flightReservationsAirlineInterface, minYear, maxYear), new GetFlightOperation(flightReservationsAirlineInterface), new GetFlightsOperation(countries, flightReservationsAirlineInterface, minYear, maxYear), new GetCustomerByIDOperation(flightReservationsAirlineInterface), new GetCustomerByNameOperation(flightReservationsAirlineInterface), new GetFlightReservationOperation(flightReservationsAirlineInterface), new GetReservationAgencyByName(flightReservationsAirlineInterface), new AddCustomerOperation(flightReservationsAirlineInterface) };

		int[] probabilities = { 3, 15, 20, 12, 20, 20, 9, 1 };
		RandomChooser<Operation> operationsChooser = new RandomChooser<Operation>(operations, probabilities);
		operationsChooser.getObject().perform();

	}

	public static void main(String[] args) {
		if (args.length != 3) {
			throw new RuntimeException("Should have 3 arguments, received " + args.length);
		}
		String host = args[0];
		int port = new Integer(args[1]);
		int id = new Integer(args[2]);
		String serverName = "FlightReservationsSystem";
		AirlineMockClient airlineMockClient = new AirlineMockClient(serverName, host, port, id);
		try {
			airlineMockClient.runTest();
		} catch (Exception e) {
			System.err.println("Airline Client " + id + " exception:");
			e.printStackTrace();
		}
	}
}
