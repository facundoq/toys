package org.flightreservationsclient.main;

import java.io.File;
import java.io.IOException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import org.flightreservationsserverinterface.FlightReservationsAirlineInterface;
import org.flightreservationsserverinterface.FlightReservationsLoginInterface;
import org.flightreservationsserverinterface.exceptions.ErrorInServerException;
import org.flightreservationsserverinterface.exceptions.InvalidLoginException;
import org.flightreservationsserverinterface.exceptions.ReservationAgencyRepeatedNameException;



/**
 * @author Facundo Quiroga
 * Creation date: Feb 5, 2009 2:26:24 PM
 */
public class StressTestRunner  {

	public enum OSType {
		APPLE, LINUX, SUN, UNKNOWN, WINDOWS
	}
	
	public static OSType calculateOS() {
		String osName = System.getProperty("os.name").toLowerCase();
		if (osName.startsWith("mac os x")) {
			return OSType.APPLE;
		}
		if (osName.startsWith("windows")) {
			return OSType.WINDOWS;
		}
		if (osName.startsWith("linux")) {
			return OSType.LINUX;
		}
		if (osName.startsWith("sun")) {
			return OSType.SUN;
		}
		return OSType.UNKNOWN;
	}

	/**
	 * "Cross-Platform" way to get the prefix for the file URL of the codebase
	 * @return
	 */
	public static String getCodebasePrefix() {
		if (calculateOS() == OSType.WINDOWS) {
			return "file:/";
		} else {
			return "file://";
		}
	}

	
	static {
		String currentPath = null;
		try {
			currentPath = new File(".").getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String policyFileName = "server.policy";
		String policyFile = "file://"+currentPath+"/"+policyFileName;
		System.setProperty("java.security.policy", policyFile );
		
		String interfaceCodebaseJarName = "stmi.jar";
		String interfaceCodebase = getCodebasePrefix()+currentPath+"/"+interfaceCodebaseJarName;
		String codebase = interfaceCodebase ;
		codebase = new File(interfaceCodebaseJarName).toURI().toString();
		System.out.println("Codebase URL: "+codebase);
		System.setProperty("java.rmi.server.codebase", codebase);
		//System.out.println(System.getProperty("java.rmi.server.codebase"));
	}

	static String managerServerName =  "ManagerServer";
	
	static String flightReservationsServerName =  "FlightReservationsSystem";
	static String flightReservationsRegistryHost =  "localhost";
	static int flightReservationsRegistryPort =  1099;
	
	static String agencyJarPath = "agency.jar";
	static String airlineJarPath = "airline.jar";
	
	static int numberOfAgencies = 10;
	static int numberOfAirlines = 10;

	static int runFor = 30000;
	
	public static void main(String args[]) {

		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		try {
			parseArguments(args);
			printOptions();
			createReservationAgencies();
			StressTestManagerServer stressTestManagerServer = startStressTestManagerServer();
			startAgenciesAndAirlines();
			
			System.out.print("Sleeping...");
			Thread.sleep(runFor);
			System.out.println("done");

			System.out.println("Clients started:");
			System.out.println("Agency: "+stressTestManagerServer.getStartedAgencies().size());
			System.out.println("Airline: "+stressTestManagerServer.getStartedAirlines().size());		
			
			stopClients(stressTestManagerServer);
			
			System.out.println("Clients stopped");
			
			UnicastRemoteObject.unexportObject(stressTestManagerServer, true);
			
			printAbortedAirlinesAndAgenciesReasons(stressTestManagerServer);
			
			System.out.println("Finished");
		} catch (Exception e) {
			System.err.println("StressTestRunner exception:");
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	

	private static void printAbortedAirlinesAndAgenciesReasons(StressTestManagerServer stressTestManagerServer) {
		ArrayList<Integer> abortedAgenciesIDs = stressTestManagerServer.getAbortedAgencies();
		ArrayList<String> abortedAgenciesReasons = stressTestManagerServer.getReasonsForAbortingAgencies();
		if (abortedAgenciesIDs.size() > 0){
			System.out.println("Aborted agencies:");
			for (int i = 0; i<abortedAgenciesIDs.size() ;i++){
				int id = abortedAgenciesIDs.get(i);
				String reason = abortedAgenciesReasons.get(i);
				System.out.println("Agency "+id+" aborted, reason: "+reason);
			}
		}
		ArrayList<Integer> abortedAirlinesIDs = stressTestManagerServer.getAbortedAirlines();
		ArrayList<String> abortedAirlinesReasons = stressTestManagerServer.getReasonsForAbortingAirlines();
		if (abortedAirlinesIDs.size() > 0){
			System.out.println("Aborted agencies:");
			for (int i = 0; i<abortedAirlinesIDs.size() ;i++){
				int id = abortedAirlinesIDs.get(i);
				String reason = abortedAirlinesReasons.get(i);
				System.out.println("Airline "+id+" aborted, reason: "+reason);
			}
		}
	}

	protected static void stopClients(StressTestManagerServer stressTestManagerServer) throws InterruptedException {
		System.out.println("Stopping clients...");
		stressTestManagerServer.stop();
		
		int terminatedAgencies = 0;
		int terminatedAirlines = 0;
		int numberOfAgencies = stressTestManagerServer.getStartedAgencies().size();
		int numberOfAirlines = stressTestManagerServer.getStartedAirlines().size();
		while (terminatedAgencies<numberOfAgencies|| terminatedAirlines < numberOfAirlines){
			int abortedAgencies = stressTestManagerServer.getAbortedAgencies().size();
			int finishedAgencies = stressTestManagerServer.getFinishedAgencies().size();
			 terminatedAgencies =  abortedAgencies+finishedAgencies ;
			
			int abortedAirlines = stressTestManagerServer.getAbortedAirlines().size();
			int finishedAirlines = stressTestManagerServer.getFinishedAirlines().size();
			 terminatedAirlines =  abortedAirlines+finishedAirlines ;
			Thread.sleep(3000);
			System.out.println("Agencies (finished+aborted): ("+finishedAgencies+"+"+abortedAgencies+")/"+numberOfAgencies);
			System.out.println("Airlines (finished+aborted): ("+ finishedAirlines+"+"+abortedAirlines+")/"+numberOfAirlines);
		}
	}

	protected static void startAgenciesAndAirlines() throws IOException {
		// Start MockClients			
		String baseArguments = flightReservationsRegistryHost;
		baseArguments += " "+Integer.valueOf(flightReservationsRegistryPort).toString();
		String baseRunCommand = "java -jar";
		startAgencyClients(baseArguments, baseRunCommand);
		startAirlineClients(baseArguments,baseRunCommand);
	}

	private static void startAirlineClients(String baseArguments,String baseRunCommand) throws IOException {
		System.out.println("Starting airline clients...");

		String airlineRunCommand = baseRunCommand+" "+airlineJarPath;
		for (int i = 0 ; i < numberOfAirlines; i++){
			String airlineArguments = baseArguments +" "+i;
			String runCommand = airlineRunCommand+" "+airlineArguments;
			System.out.println(runCommand);
			Runtime.getRuntime().exec(runCommand);
		}
		System.out.println("done");
	}

	private static void startAgencyClients(String baseArguments, String baseRunCommand) throws IOException {
		System.out.println("Starting agency clients...");

		String agencyRunCommand = baseRunCommand+" "+agencyJarPath;
		for (int i = 0 ; i < numberOfAgencies; i++){
			String agencyArguments = baseArguments +" "+i;
			String runCommand =agencyRunCommand+ " "+agencyArguments;
			System.out.println(runCommand);
			Runtime.getRuntime().exec(runCommand);
		}
		System.out.println("done.");
	}

	protected static StressTestManagerServer startStressTestManagerServer() throws RemoteException, AccessException {
		Registry registry = LocateRegistry.getRegistry();
		System.out.print("Starting StressTestManagerServer...");
		StressTestManagerServer stressTestManagerServer = new StressTestManagerServer();
		registry.rebind(managerServerName,stressTestManagerServer );
		System.out.println("done.");
		return stressTestManagerServer;
	}

	private static void createReservationAgencies() throws RemoteException, NotBoundException, InvalidLoginException, ErrorInServerException {
		System.out.print("Creating ReservationAgencies....");
		Registry registry = LocateRegistry.getRegistry(flightReservationsRegistryHost,flightReservationsRegistryPort);
		try {
			FlightReservationsLoginInterface flightReservationsLoginInterface = (FlightReservationsLoginInterface) registry.lookup(flightReservationsServerName);
			FlightReservationsAirlineInterface flightReservationsAirlineInterface = flightReservationsLoginInterface.loginAsAirline("airline", "airline");
			for (int i = 0; i<numberOfAgencies; i++){
				try {
					flightReservationsAirlineInterface.addReservationAgency("ra"+i,"ra"+i);
				} catch (ReservationAgencyRepeatedNameException e) {
					// name is repeated; leave it at that
				}
			}
			System.out.println("done.");
		} catch (NotBoundException e) {
			System.err.println("Server named "+flightReservationsServerName+" does not exist or is not bound");
			throw e;
		} catch (InvalidLoginException e) {
			System.err.println("Invalid user name or password for airline login");
			throw e;
		} catch (ErrorInServerException e) {
			System.err.println("Error in server "+e.getMessage());
			throw e;
		}
	}

	private static void parseArguments(String[] args) {
		
		if (args.length > 0){
			flightReservationsRegistryHost = args[0];
		}
		if (args.length > 1){
			flightReservationsRegistryPort = new Integer(args[1]);
		}
		if (args.length > 2){
			numberOfAirlines = new Integer(args[2]);
		}
		if (args.length > 3){
			numberOfAgencies= new Integer(args[3]);
		}
		if (args.length > 4){
			runFor = new Integer(args[4]);
		}
	}
	
	private static void printOptions() {
		System.out.println("Test Options:");
		System.out.println("FlightReservation registry host:port ="+ flightReservationsRegistryHost+":"+flightReservationsRegistryPort);
		System.out.println("Number of Airlines: "+numberOfAirlines);
		System.out.println("Number of Agencies: "+numberOfAgencies);
		System.out.println("Run For: "+runFor);
		System.out.println("");
	}
}
