/**
 * 
 */
package org.flightreservationsserver.model;

/**
 * @author Facundo Manuel Quiroga
 * Jan 23, 2009
 * 
 */
public class ReservationAgency {
	
	public ReservationAgency(int id, String name,String password) {
		super();
		this.id = id;
		this.name = name;
		this.password = password;
	}

	protected int id;
	protected String name;
	protected String password;
	
	public int getId() {
		return this.id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getPassword() {
		return this.password;
	}
	protected void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return this.name;
	}
	protected void setName(String name) {
		this.name = name;
	}
	
	
}
