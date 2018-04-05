package org.flightreservationsclient.main.operations;

import java.rmi.RemoteException;

import org.flightreservationsserverinterface.exceptions.ErrorInServerException;

/**
 * @author Facundo Quiroga
 * Creation date: Feb 8, 2009 5:28:57 PM
 */
public abstract class Operation {

	public abstract void perform() throws ErrorInServerException, RemoteException;

}
