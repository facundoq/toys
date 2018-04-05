/**
 * 
 */
package org.flightreservationsserver.database;

import java.util.ArrayList;

import org.crappydbms.datadictionary.InvalidStoredRelationNameException;
import org.crappydbms.exceptions.InvalidAttributeNameException;
import org.crappydbms.queries.operations.Modification;
import org.crappydbms.queries.operations.ModifyOperation;
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
import org.flightreservationsserver.model.Flight;
import org.flightreservationsserver.model.FlightReservation;
import org.flightreservationsserver.model.ReservationAgency;
import org.flightreservationsserver.model.exceptions.FlightReservationsServerError;


/**
 * @author Facundo Manuel Quiroga
 * Jan 26, 2009
 * 
 */
public class FlightReservationDAO extends DAO {
	
	static int nextID;
	public static synchronized  int getNextID() {
		int id = nextID;
		nextID++;
		return id;
	}
	
	protected static StoredRelationSchema  getRelationSchema(){
		return DBInitializer.relationSchemas[1];
	}
	public static StoredRelation getStoredRelation(){
		try {
			return getStoredRelation(relationNames[1]);
		} catch (InvalidStoredRelationNameException e) {
			throw new FlightReservationsServerError(e.getMessage());
		}
	}
	
	/**
	 * @param flightReservation
	 * @throws TransactionAbortedException 
	 */
	public static FlightReservation addFlightReservation(FlightReservation flightReservation) throws TransactionAbortedException {
		flightReservation.setId(getNextID());
		StoredTuple tuple = flightReservationToTuple(flightReservation);
		StoredRelation storedRelation = getStoredRelation();
		storedRelation.addTuple(tuple, getTransaction());
		return flightReservation;
	}

	private static StoredTuple flightReservationToTuple(FlightReservation flightReservation) {
		ArrayList<Field> fields = new ArrayList<Field>();
		fields.add(IntegerField.valueOf(flightReservation.getId()));
		fields.add(IntegerField.valueOf(flightReservation.getCustomer().getId()));
		fields.add(IntegerField.valueOf(flightReservation.getReservationAgency().getId()));
		fields.add(IntegerField.valueOf(flightReservation.getFlight().getId()));
		fields.add(StringField.valueOf(flightReservation.getPassengerName()));
		int isPaid = flightReservation.isPaid() ? 1: 0; 
		fields.add(IntegerField.valueOf(isPaid));
		int isCancelled= flightReservation.isCancelled() ? 1: 0; 
		fields.add(IntegerField.valueOf(isCancelled));		
		return new StoredTuple(fields,getRelationSchema());
	}

	
	protected static FlightReservation  tupleToFlightReservation(Tuple flightReservationTuple) throws TransactionAbortedException {
		try {
		int id = ((IntegerField) flightReservationTuple.getFieldNamed("id")).getValue();
		int customerID = ((IntegerField) flightReservationTuple.getFieldNamed("customerID")).getValue();
		int reservationAgencyID = ((IntegerField) flightReservationTuple.getFieldNamed("reservationAgencyID")).getValue();
		int flightID = ((IntegerField) flightReservationTuple.getFieldNamed("flightID")).getValue();
		Customer customer = CustomerDAO.getCustomerByID(customerID);
		Flight flight = FlightDAO.getFlight(flightID);
		ReservationAgency reservationAgency = ReservationAgencyDAO.getReservationAgency(reservationAgencyID);
		String passengerName = ((StringField) flightReservationTuple.getFieldNamed("passengerName")).getValue();
		boolean paid = ((IntegerField) flightReservationTuple.getFieldNamed("paid")).getValue() == 1;
		boolean cancelled = ((IntegerField) flightReservationTuple.getFieldNamed("cancelled")).getValue() == 1;
		FlightReservation flightReservation =new FlightReservation(id,flight, customer, reservationAgency, passengerName );
		flightReservation.setPaid(paid);
		flightReservation.setCancelled(cancelled);
		
		return flightReservation;
		
		} catch (InvalidAttributeNameException e) {
			throw new FlightReservationsServerError("invalid attribute name: "+e.getMessage());
		} catch (NotFoundException e) {
			throw new FlightReservationsServerError("NotFoundException"+e.getMessage());
		}
		
	}
	/**
	 * @param id
	 * @return
	 * @throws TransactionAbortedException 
	 * @throws NotFoundException 
	 */
	public static FlightReservation getFlightReservation(int id) throws TransactionAbortedException, NotFoundException {
		Predicate predicate = EqualsPredicateFactory.newPredicate("id", IntegerField.valueOf(id));
		ArrayList<FlightReservation> flightReservations = getFlightReservations(predicate);
		if (flightReservations.size() > 1){
			throw new FlightReservationsServerError("Cannot have two flight reservations with same id");
		}
		if (flightReservations.size()== 0){
			throw new NotFoundException("Could not find FlightReservation with id "+ id);
		}
		return flightReservations.get(0);
	}

	/**
	 * @param flight
	 * @param reservationAgency 
	 * @return
	 * @throws TransactionAbortedException 
	 */
	public static ArrayList<FlightReservation> getFlightReservationsFor(Flight flight, ReservationAgency reservationAgency) throws TransactionAbortedException {
		Predicate predicate = EqualsPredicateFactory.newPredicate("flightID", IntegerField.valueOf(flight.getId()));
		predicate = predicate.and(EqualsPredicateFactory.newPredicate("reservationAgencyID",IntegerField.valueOf(reservationAgency.getId())));
		return getFlightReservations(predicate);
	}

	/**
	 * @param customer
	 * @param reservationAgency
	 * @return
	 * @throws TransactionAbortedException 
	 */
	public static ArrayList<FlightReservation> getFlightReservationsBy(Customer customer, ReservationAgency reservationAgency) throws TransactionAbortedException {
		Predicate predicate = EqualsPredicateFactory.newPredicate("customerID", IntegerField.valueOf(customer.getId()));
		predicate = predicate.and(EqualsPredicateFactory.newPredicate("reservationAgencyID",IntegerField.valueOf(reservationAgency.getId())));
		return getFlightReservations(predicate);
	}

	protected static ArrayList<FlightReservation> getFlightReservations(Predicate predicate) throws TransactionAbortedException{
		SelectionOperator selectionOperator = new SelectionOperator(getStoredRelation(),predicate);
		ArrayList<Tuple> tuples =selectionOperator.iterateAndReturnTuples(getTransaction());
		ArrayList<FlightReservation> flightReservations = new ArrayList<FlightReservation>();	
		for (Tuple tuple : tuples){
			flightReservations.add(tupleToFlightReservation(tuple));
		}
		return flightReservations;
	}
	/**
	 * @param flightReservation
	 * @throws TransactionAbortedException 
	 */
	public static void saveFlightReservation(FlightReservation flightReservation) throws TransactionAbortedException {
		ArrayList<Modification> modifications = getModifications(flightReservationToTuple(flightReservation));
		Predicate predicate = EqualsPredicateFactory.newPredicate("id", IntegerField.valueOf(flightReservation.getId()));
		ModifyOperation modifyOperation = new ModifyOperation(getStoredRelation(),predicate,modifications);
		try {
			modifyOperation.perform(getTransaction());
		} catch (InvalidAttributeNameException e) {
			throw new FlightReservationsServerError("InvalidAttributeNameException  "+e.getMessage());
		}
	}
	
	


}
