package org.flightreservationsclient.main;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import org.flightreservationsclient.main.operations.AddCustomerOperation;
import org.flightreservationsclient.main.operations.BookFlightReservationOperation;
import org.flightreservationsclient.main.operations.CancelFlightReservationOperation;
import org.flightreservationsclient.main.operations.GetCustomerByIDOperation;
import org.flightreservationsclient.main.operations.GetCustomerByNameOperation;
import org.flightreservationsclient.main.operations.GetFlightOperation;
import org.flightreservationsclient.main.operations.GetFlightReservationOperation;
import org.flightreservationsclient.main.operations.GetFlightReservationsByCustomerRAOperation;
import org.flightreservationsclient.main.operations.GetFlightReservationsForFlightRAOperation;
import org.flightreservationsclient.main.operations.GetFlightsOperation;
import org.flightreservationsclient.main.operations.Operation;
import org.flightreservationsclient.main.operations.PayForFlightReservationOperation;
import org.flightreservationsclient.main.utils.RandomChooser;
import org.flightreservationsserverinterface.FlightReservationsAgencyInterface;
import org.flightreservationsserverinterface.FlightReservationsLoginInterface;
import org.flightreservationsserverinterface.exceptions.ErrorInServerException;
import org.managerinterface.ManagerInterface;

/**
 * @author Facundo Quiroga Creation date: Feb 5, 2009 2:11:58 PM
 */
public class AgencyMockClient extends MockClient {

	private static final long sleepTimeBetweenOperations = 100;
	protected String agencyName;
	protected String password;

	public AgencyMockClient(String serverName, String host, int port, String agencyName, String password, int id) {
		super(serverName, host, port, id);
		this.agencyName = agencyName;
		this.password = password;
	}

	public void runTest() throws Exception {
		System.out.println("Agency Client " + this.id + " started");
		try {
			ManagerInterface managerInterface = this.obtainManagerInterface();
			try {
				managerInterface.agencyStarted(this.id);
				FlightReservationsLoginInterface flightReservationsLoginInterface = this.obtainLoginInterface();
				FlightReservationsAgencyInterface flightReservationsAgencyInterface = flightReservationsLoginInterface.loginAsReservationAgency(this.agencyName, password);

				// TODO stress test
				while (managerInterface.shouldContinue()) {
					try {
						this.doTest(flightReservationsAgencyInterface);
					} catch (ErrorInServerException e) {

					}
					Thread.sleep(sleepTimeBetweenOperations);
				}
				System.out.println("End client " + this.id);
			} catch (Exception e) {
				managerInterface.agencyAborted(this.id, "Exception " + e.getClass() + " message:" + e.getMessage());
				throw e;
			}
			managerInterface.agencyFinished(this.id);
		} catch (AccessException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}

	private void doTest(FlightReservationsAgencyInterface flightReservationsAgencyInterface) throws ErrorInServerException, RemoteException {
		for (int i = 0; i < numberOfOperationsPerIteration; i++) {
			this.performOperation(flightReservationsAgencyInterface);
		}

	}

	protected void performOperation(FlightReservationsAgencyInterface flightReservationsAgencyInterface) throws ErrorInServerException, RemoteException {
		ArrayList<String> countries = StressTestUtils.getCountries();
		int minYear = StressTestUtils.getMinYear();
		int maxYear = StressTestUtils.getMaxYear();
		Operation[] operations = { new GetFlightOperation(flightReservationsAgencyInterface), new GetFlightsOperation(countries, flightReservationsAgencyInterface, minYear, maxYear), new GetCustomerByIDOperation(flightReservationsAgencyInterface), new GetCustomerByNameOperation(flightReservationsAgencyInterface), new GetFlightReservationOperation(flightReservationsAgencyInterface), new BookFlightReservationOperation(flightReservationsAgencyInterface), new CancelFlightReservationOperation(flightReservationsAgencyInterface), new PayForFlightReservationOperation(flightReservationsAgencyInterface), new GetFlightReservationsByCustomerRAOperation(flightReservationsAgencyInterface), new GetFlightReservationsForFlightRAOperation(flightReservationsAgencyInterface), new AddCustomerOperation(flightReservationsAgencyInterface) };

		int[] probabilities = { 14, 10, 10, 10, 10, 10, 3, 10, 10, 10, 3 };
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
		String agencyName = "ra" + id;
		String agencyPassword = "ra" + id;

		String serverName = "FlightReservationsSystem";
		AgencyMockClient agencyMockClient = new AgencyMockClient(serverName, host, port, agencyName, agencyPassword, id);
		try {
			agencyMockClient.runTest();
		} catch (Exception e) {
			System.err.println("Agency client " + id + " exception:");
			e.printStackTrace();
		}

	}

}
