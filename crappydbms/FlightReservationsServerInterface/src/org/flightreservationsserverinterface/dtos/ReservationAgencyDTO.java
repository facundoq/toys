/**
 * 
 */
package org.flightreservationsserverinterface.dtos;

/**
 * @author Facundo Manuel Quiroga
 * Jan 26, 2009
 * 
 */
public class ReservationAgencyDTO extends DTO {
	
	public ReservationAgencyDTO(int id, String name,String password) {
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
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
