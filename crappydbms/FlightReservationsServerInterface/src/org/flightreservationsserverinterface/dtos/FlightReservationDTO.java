/**
 * 
 */
package org.flightreservationsserverinterface.dtos;

/**
 * @author Facundo Manuel Quiroga
 * Jan 26, 2009
 * 
 */
public class FlightReservationDTO extends DTO {
	

	FlightDTO flight;
	CustomerDTO customer;
	ReservationAgencyDTO reservationAgency;
	protected int id;
	protected boolean paid;
	protected boolean cancelled;
	protected String passengerName;
	
	public FlightReservationDTO(int id, FlightDTO flight, CustomerDTO customer, ReservationAgencyDTO reservationAgency,  boolean paid,
			boolean cancelled, String passengerName) {
		super();
		this.flight = flight;
		this.customer = customer;
		this.reservationAgency = reservationAgency;
		this.id = id;
		this.paid = paid;
		this.cancelled = cancelled;
		this.passengerName = passengerName;
	}
	
	public FlightDTO getFlight() {
		return this.flight;
	}
	public void setFlight(FlightDTO flight) {
		this.flight = flight;
	}
	public CustomerDTO getCustomer() {
		return this.customer;
	}
	public void setCustomer(CustomerDTO customer) {
		this.customer = customer;
	}
	public ReservationAgencyDTO getReservationAgency() {
		return this.reservationAgency;
	}
	public void setReservationAgency(ReservationAgencyDTO reservationAgency) {
		this.reservationAgency = reservationAgency;
	}
	public int getId() {
		return this.id;
	}
	public void setId(int id) {
		this.id = id;
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
	public void setPassengerName(String passengerName) {
		this.passengerName = passengerName;
	}
	
}