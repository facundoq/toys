/**
 * 
 */
package org.flightreservationsserverinterface.dtos;

/**
 * @author Facundo Manuel Quiroga
 * Jan 26, 2009
 * 
 */
public class FlightDTO extends DTO {
	

	protected int id;
	protected String date;
	protected String source;
	protected String destination;
	protected int maximumSeats;
	protected int occupiedSeats;
	protected boolean full;
	
	public FlightDTO(int id, String date, String source, String destination, int maximumSeats, int occupiedSeats, boolean full) {
		super();
		this.id = id;
		this.date = date;
		this.source = source;
		this.destination = destination;
		this.maximumSeats = maximumSeats;
		this.occupiedSeats = occupiedSeats;
		this.full = full;
	}
	
	public int getId() {
		return this.id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDate() {
		return this.date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getSource() {
		return this.source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getDestination() {
		return this.destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public int getMaximumSeats() {
		return this.maximumSeats;
	}
	public void setMaximumSeats(int maximumSeats) {
		this.maximumSeats = maximumSeats;
	}
	public int getOccupiedSeats() {
		return this.occupiedSeats;
	}
	public void setOccupiedSeats(int occupiedSeats) {
		this.occupiedSeats = occupiedSeats;
	}
	public boolean isFull() {
		return this.full;
	}
	public void setFull(boolean full) {
		this.full = full;
	}
	
	
}
