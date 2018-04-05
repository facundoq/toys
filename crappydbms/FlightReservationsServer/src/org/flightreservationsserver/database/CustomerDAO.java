/**
 * 
 */
package org.flightreservationsserver.database;

import java.util.ArrayList;

import org.crappydbms.datadictionary.InvalidStoredRelationNameException;
import org.crappydbms.exceptions.InvalidAttributeNameException;
import org.crappydbms.queries.operators.selection.SelectionOperator;
import org.crappydbms.queries.predicates.Predicate;
import org.crappydbms.queries.predicates.simple.factories.EqualsPredicateFactory;
import org.crappydbms.relations.StoredRelation;
import org.crappydbms.relations.fields.Field;
import org.crappydbms.relations.fields.IntegerField;
import org.crappydbms.relations.fields.StringField;
import org.crappydbms.relations.schemas.StoredRelationSchema;
import org.crappydbms.relations.tuples.StoredTuple;
import org.crappydbms.relations.tuples.Tuple;
import org.crappydbms.transactions.TransactionAbortedException;
import org.flightreservationsserver.model.Customer;
import org.flightreservationsserver.model.exceptions.FlightReservationsServerError;


/**
 * @author Facundo Manuel Quiroga
 * Jan 24, 2009
 * 
 */
public class CustomerDAO 	extends DAO {
	
	static int nextID;
	public static synchronized  int getNextID() {
		int id = nextID;
		nextID++;
		return id;
	}
	
	public static StoredRelationSchema  getRelationSchema(){
		return DBInitializer.relationSchemas[3];
	}
	public static StoredRelation getStoredRelation(){
		try {
			return getStoredRelation(relationNames[3]);
		} catch (InvalidStoredRelationNameException e) {
			throw new FlightReservationsServerError(e.getMessage());
		}
	}

	/**
	 * @param id
	 * @return
	 * @throws TransactionAbortedException 
	 * @throws NotFoundException 
	 */
	public static Customer getCustomerByID(int id) throws TransactionAbortedException, NotFoundException {
		Predicate predicate = EqualsPredicateFactory.newPredicate("id", IntegerField.valueOf(id));
		ArrayList<Customer> customers =getCustomers(predicate);
		if (customers.size() > 1){
			throw new FlightReservationsServerError("Cannot have two customers with same id");
		}
		if (customers.size()== 0){
			throw new NotFoundException("Could not find Customer  with id "+ id);
		}
		return customers.get(0); 
	}

	/**
	 * @param dni
	 * @return
	 * @throws TransactionAbortedException 
	 */
	public static ArrayList<Customer> getCustomerByDNI(int dni) throws TransactionAbortedException {
		Predicate predicate = EqualsPredicateFactory.newPredicate("dni", IntegerField.valueOf(dni));
		return getCustomers(predicate);
	}

	/**
	 * @param customer
	 * @throws TransactionAbortedException 
	 */
	public static Customer addCustomer(Customer customer) throws TransactionAbortedException {
		customer.setId(getNextID());
		StoredTuple tuple = customerToTuple(customer);
		getStoredRelation().addTuple(tuple, getTransaction());
		return customer;
		
	}
	
	/**
	 * @param name
	 * @return
	 * @throws TransactionAbortedException 
	 */
	public static ArrayList<Customer> getCustomersByName(String name) throws TransactionAbortedException {
		Predicate predicate = EqualsPredicateFactory.newPredicate("name", StringField.valueOf(name));
		return getCustomers(predicate);
	}
	
	public static ArrayList<Customer> getCustomers(Predicate predicate) throws TransactionAbortedException{
		SelectionOperator selectionOperator = new SelectionOperator(getStoredRelation(),predicate);
		ArrayList<Tuple> tuples =selectionOperator.iterateAndReturnTuples(getTransaction());
		ArrayList<Customer> customers = new ArrayList<Customer>();	
		for (Tuple tuple : tuples){
			customers.add(tupleToCustomer(tuple));
		}
		return customers;
	}
	
	private static Customer tupleToCustomer(Tuple tuple) {
		try {
			int id = ((IntegerField) tuple.getFieldNamed("id")).getValue();		
			String name = ((StringField) tuple.getFieldNamed("name")).getValue();
			int dni = ((IntegerField) tuple.getFieldNamed("dni")).getValue();
			int creditCardNumber= ((IntegerField) tuple.getFieldNamed("creditCardNumber")).getValue();
			return new Customer(id,name,dni,creditCardNumber);
			} catch (InvalidAttributeNameException e) {
				throw new FlightReservationsServerError("invalid attribute name: "+e.getMessage());
			}
	}
	
	private static StoredTuple customerToTuple(Customer customer) {
		ArrayList<Field> fields = new ArrayList<Field>();
		fields.add(IntegerField.valueOf(customer.getId()));
		fields.add(StringField.valueOf(customer.getName()));
		fields.add(IntegerField.valueOf(customer.getDni()));
		fields.add(IntegerField.valueOf(customer.getCreditCardNumber()));		
		return new StoredTuple(fields,getRelationSchema());
	}

}
