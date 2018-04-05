/**
 * 
 */
package org.flightreservationsserver.model;

import org.flightreservationsserver.model.exceptions.FlightReservationAlreadyCanceledException;
import org.flightreservationsserverinterface.exceptions.FlightReservationAlreadyPaidException;

/**
 * @author Facundo Manuel Quiroga
 * Jan 23, 2009
 * 
 */
public class FlightReservation {

	protected int id;
	protected Customer customer;
	protected Flight flight;
	protected ReservationAgency reservationAgency;
	protected boolean paid;
	protected boolean cancelled;
	protected String passengerName;

	
	public FlightReservation(int id,Flight flight, Customer customer,  ReservationAgency reservationAgency,String passengerName) {
		super();
		this.id = id;
		this.customer = customer;
		this.flight = flight;
		this.reservationAgency = reservationAgency;
		this.paid = false;
		this.passengerName = passengerName;
		this.cancelled = false;
	}

	
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Customer getCustomer() {
		return this.customer;
	}

	protected void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Flight getFlight() {
		return this.flight;
	}

	protected void setFlight(Flight flight) {
		this.flight = flight;
	}

	public ReservationAgency getReservationAgency() {
		return this.reservationAgency;
	}

	protected void setReservationAgency(ReservationAgency reservationAgency) {
		this.reservationAgency = reservationAgency;
	}

	public boolean isPaid() {
		return this.paid;
	}

	public void setPaid(boolean paid) {
		this.paid = paid;
	}


	public boolean isCancelled() {
		return this.cancelled;
	}


	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}


	public String getPassengerName() {
		return this.passengerName;
	}


	protected void setPassengerName(String passengerName) {
		this.passengerName = passengerName;
	}
	
	public void cancel() throws FlightReservationAlreadyCanceledException{
		if (this.cancelled){
			throw new FlightReservationAlreadyCanceledException();
		}
		this.setCancelled(true);
		this.getFlight().cancelReservation(this);
	}


	public void paid() throws FlightReservationAlreadyPaidException, FlightReservationAlreadyCanceledException {
		if (this.isCancelled()){
			throw new FlightReservationAlreadyCanceledException();
		}
		if (this.isPaid()){
			throw new FlightReservationAlreadyPaidException();
		}
		this.setPaid(true);
		
	}
	

}
