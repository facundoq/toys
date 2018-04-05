package org.flightreservationsclient.main;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import org.managerinterface.ManagerInterface;

/**
 * @author Facundo Quiroga
 * Creation date: Feb 5, 2009 10:42:39 PM
 */
public class StressTestManagerServer extends UnicastRemoteObject implements ManagerInterface{

	protected ArrayList<Integer> startedAgencies;
	protected ArrayList<Integer> abortedAgencies;
	protected ArrayList<Integer> finishedAgencies;
	
	protected ArrayList<Integer> startedAirlines;
	protected ArrayList<Integer> abortedAirlines;
	protected ArrayList<Integer> finishedAirlines;
	
	protected ArrayList<String> reasonsForAbortingAirlines;
	protected ArrayList<String> reasonsForAbortingAgencies;
	protected volatile boolean shouldContinue;
	
	protected StressTestManagerServer() throws RemoteException {
		super();
		this.shouldContinue = true;
		
		this.startedAgencies = new ArrayList<Integer>();
		this.abortedAgencies = new ArrayList<Integer>();
		this.finishedAgencies = new ArrayList<Integer>();
		
		this.startedAirlines = new ArrayList<Integer>();
		this.abortedAirlines = new ArrayList<Integer>();
		this.finishedAirlines = new ArrayList<Integer>();
		
		this.reasonsForAbortingAirlines = new ArrayList<String>();
		this.reasonsForAbortingAgencies = new ArrayList<String>();
		
	}

	@Override
	public void agencyAborted(int id, String reason) throws RemoteException {
		synchronized (this.abortedAgencies){
			this.abortedAgencies.add(id);
			this.reasonsForAbortingAgencies.add(reason);
		}
	}

	@Override
	public void agencyFinished(int id) throws RemoteException {
		synchronized (this.finishedAgencies){
			this.finishedAgencies.add(id);
		}
	}

	@Override
	public void agencyStarted(int id) throws RemoteException {
		synchronized (this.startedAgencies){
			this.startedAgencies.add(id);
		}
	}

	@Override
	public void airlineAborted(int id, String reason) throws RemoteException {
		synchronized (this.abortedAirlines){
			this.abortedAirlines.add(id);
			this.reasonsForAbortingAirlines.add(reason);
		}
	}

	@Override
	public void airlineFinished(int id) throws RemoteException {
		synchronized (this.finishedAirlines){
			this.finishedAirlines.add(id);
		}
	}

	@Override
	public void airlineStarted(int id) throws RemoteException {
		synchronized (this.startedAirlines){
			this.startedAirlines.add(id);
		}
	}

	@Override
	public boolean shouldContinue() throws RemoteException {
		return shouldContinue;
	}

	public ArrayList<Integer> getStartedAgencies() {
		synchronized (this.startedAgencies){
			return new ArrayList<Integer>(this.startedAgencies);
		}
	}

	public ArrayList<Integer> getAbortedAgencies() {
		synchronized (this.abortedAgencies){
		return new ArrayList<Integer>(this.abortedAgencies);
		}
	}

	public ArrayList<Integer> getFinishedAgencies() {
		synchronized (this.finishedAgencies){
			return new ArrayList<Integer>(this.finishedAgencies);
		}
	}

	public ArrayList<Integer> getStartedAirlines() {
		synchronized (this.startedAirlines){
			return new ArrayList<Integer>(this.startedAirlines);
		}
	}

	public ArrayList<Integer> getAbortedAirlines() {
		synchronized (this.abortedAirlines){
			return new ArrayList<Integer>(this.abortedAirlines);
		}
	}

	public ArrayList<Integer> getFinishedAirlines() {
		synchronized (this.finishedAirlines){
			return new ArrayList<Integer>(this.finishedAirlines);
		}
	}
	
	public void stop(){
		this.shouldContinue = false;
	}

	protected ArrayList<String> getReasonsForAbortingAirlines() {
		synchronized (this.reasonsForAbortingAirlines){
			return new ArrayList<String>(this.reasonsForAbortingAirlines);
		}
	}

	protected void setReasonsForAbortingAirlines(ArrayList<String> reasonsForAbortingAirlines) {
		this.reasonsForAbortingAirlines = reasonsForAbortingAirlines;
	}

	protected ArrayList<String> getReasonsForAbortingAgencies() {
		synchronized (this.reasonsForAbortingAgencies){
			return new ArrayList<String>(this.reasonsForAbortingAgencies);
		}
	}

	protected void setReasonsForAbortingAgencies(ArrayList<String> reasonsForAbortingAgencies) {
		this.reasonsForAbortingAgencies = reasonsForAbortingAgencies;
	}
	
	

}
