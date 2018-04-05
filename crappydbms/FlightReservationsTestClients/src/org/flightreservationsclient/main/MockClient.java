package org.flightreservationsclient.main;

import java.io.File;
import java.io.IOException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.flightreservationsclient.main.utils.RandomDateGenerator;
import org.flightreservationsserverinterface.FlightReservationsLoginInterface;
import org.managerinterface.ManagerInterface;

/**
 * @author Facundo Quiroga
 * Creation date: Feb 5, 2009 8:45:47 PM
 */
public abstract class MockClient {
	
	protected static final int numberOfOperationsPerIteration = 200;
	
	protected static int maxYear = StressTestUtils.getMaxYear();
	protected static int minYear = StressTestUtils.getMinYear();
	
	protected static RandomDateGenerator randomDateGenerator = new RandomDateGenerator(minYear,maxYear);
	protected String serverName;
	protected String host;
	protected int port;

	protected int id;

	
	public MockClient(String serverName, String host, int port, int id) {
		super();
		this.serverName = serverName;
		this.host = host;
		this.port = port;
		this.id = id;
		String currentPath = null;
		try {
			currentPath = new File(".").getCanonicalPath();
			String policyFileName = "client.policy";
			String policyFile = "file://"+currentPath+"/"+policyFileName;
			System.setProperty("java.security.policy", policyFile );
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		this.setSecurityManager();
	}
	protected void setSecurityManager(){
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
	}

	public Registry locateRegistry() throws RemoteException{
		Registry registry = LocateRegistry.getRegistry(this.host,this.port);
		return registry;
	}
	public FlightReservationsLoginInterface obtainLoginInterface() throws AccessException, RemoteException, NotBoundException{
		return (FlightReservationsLoginInterface) this.locateRegistry().lookup(this.serverName);
	}
	
	public ManagerInterface obtainManagerInterface() throws AccessException, RemoteException, NotBoundException{
		Registry registry = LocateRegistry.getRegistry();
		return (ManagerInterface) registry.lookup(this.getManagerServerName());
	}
	private String getManagerServerName() {
		return "ManagerServer";
	}
	
	
	
}