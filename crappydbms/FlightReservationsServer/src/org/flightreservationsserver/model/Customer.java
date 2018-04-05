/**
 * 
 */
package org.flightreservationsserver.model;

/**
 * @author Facundo Manuel Quiroga
 * Jan 23, 2009
 * 
 */
public class Customer {
	
	public Customer(int id, String name, int dni, int creditCardNumber) {
		super();
		this.id = id;
		this.name = name;
		this.dni = dni;
		this.creditCardNumber = creditCardNumber;
	}
	
	protected int id;
	protected String name;
	protected int dni;
	protected int creditCardNumber;
	
	public int getId() {
		return this.id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return this.name;
	}
	protected void setName(String name) {
		this.name = name;
	}
	public int getDni() {
		return this.dni;
	}
	protected void setDni(int dni) {
		this.dni = dni;
	}
	public int getCreditCardNumber() {
		return this.creditCardNumber;
	}
	protected void setCreditCardNumber(int creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}
	
}
