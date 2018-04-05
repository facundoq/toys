/**
 * 
 */
package org.flightreservationsserver.main;

import java.io.File;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import org.flightreservationsserver.externalinterface.FlightReservationsLoginServer;
import org.flightreservationsserver.model.FlightReservationsSystem;
import org.flightreservationsserverinterface.FlightReservationsLoginInterface;

/**
 * @author Facundo Manuel Quiroga
 * Feb 2, 2009
 * 
 */
public class Main {
	
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
		//String policyFile = "file:///mnt/cosas/sharedhome/EclipseWorkspace/FlightReservationsServer/server.policy";
		String currentPath = null;
		try {
			currentPath = new File(".").getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String policyFileName = "server.policy";
		String policyFile = getCodebasePrefix() + currentPath + "/" + policyFileName;
		System.setProperty("java.security.policy", policyFile);

		// The codebase HAS to be a file url, it cannot be a normal path. 
		String interfaceCodebaseJarName = "frsi.jar";
		String interfaceCodebase = "file://" + currentPath + "/" + interfaceCodebaseJarName;
		String codebase = interfaceCodebase;
		codebase = new File(interfaceCodebaseJarName).toURI().toString();
		System.out.println("Codebase URL: "+codebase);
		System.setProperty("java.rmi.server.codebase", codebase);
	}

	public static void main(String[] args) {

		try {
			String name = "FlightReservationsSystem";
			FlightReservationsSystem flightReservationsSystem = new FlightReservationsSystem();
			if (System.getSecurityManager() == null) {
				System.setSecurityManager(new SecurityManager());
			}
			FlightReservationsLoginServer loginServer = new FlightReservationsLoginServer(flightReservationsSystem);
			FlightReservationsLoginInterface stub = (FlightReservationsLoginInterface) UnicastRemoteObject.exportObject(loginServer, 0);
			Registry registry = LocateRegistry.getRegistry();
			registry.rebind(name, stub);
			System.out.println("Server started");
		} catch (Exception e) {
			System.err.println("Unknown exception in server, trace:");
			e.printStackTrace();
		}

	}
}
