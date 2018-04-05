package org.managerinterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Facundo Quiroga
 * Creation date: Feb 5, 2009 10:34:52 PM
 */
public interface ManagerInterface extends Remote{
	
	public void agencyStarted(int id) throws RemoteException;
	
	public void agencyFinished(int id) throws RemoteException;
	
	public void agencyAborted(int id, String reason) throws RemoteException;
	
	
	public void airlineStarted(int id) throws RemoteException;
	
	public void airlineFinished(int id) throws RemoteException;
	
	void airlineAborted(int id, String reason) throws RemoteException;
	
	public boolean shouldContinue() throws RemoteException;

	
	
}
