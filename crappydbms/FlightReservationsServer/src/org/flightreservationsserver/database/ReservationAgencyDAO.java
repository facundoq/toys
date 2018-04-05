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
import org.flightreservationsserver.model.ReservationAgency;
import org.flightreservationsserver.model.exceptions.FlightReservationsServerError;
import org.flightreservationsserverinterface.exceptions.ReservationAgencyRepeatedNameException;


/**
 * @author Facundo Manuel Quiroga
 * Jan 26, 2009
 * 
 */
public class ReservationAgencyDAO extends DAO {

	static int nextID;
	public static synchronized  int getNextID() {
		int id = nextID;
		nextID++;
		return id;
	}
	
	public static StoredRelationSchema getRelationSchema() {
		return DBInitializer.relationSchemas[2];
	}
	public static StoredRelation getStoredRelation(){
		try {
			return getStoredRelation(relationNames[2]);
		} catch (InvalidStoredRelationNameException e) {
			throw new FlightReservationsServerError(e.getMessage());
		}
	}

	public static ReservationAgency addReservationAgency(ReservationAgency reservationAgency) throws TransactionAbortedException, ReservationAgencyRepeatedNameException {
		try {
			getReservationAgency(reservationAgency.getName());
			throw new ReservationAgencyRepeatedNameException();
		} catch (NotFoundException e) {
			reservationAgency.setId(getNextID());
			StoredTuple tuple = reservationAgencyToTuple(reservationAgency);
			getStoredRelation().addTuple(tuple, getTransaction());
			return reservationAgency;
		}
		
	}

	private static StoredTuple reservationAgencyToTuple(ReservationAgency reservationAgency) {
		ArrayList<Field> fields = new ArrayList<Field>();
		fields.add(IntegerField.valueOf(reservationAgency.getId()));
		fields.add(StringField.valueOf(reservationAgency.getName()));
		fields.add(StringField.valueOf(reservationAgency.getPassword()));		
		return new StoredTuple(fields,getRelationSchema());
	}

	public static ReservationAgency getReservationAgency(int id) throws NotFoundException, TransactionAbortedException {
		Predicate predicate = EqualsPredicateFactory.newPredicate("id",IntegerField.valueOf(id));
		SelectionOperator selectionOperator = new SelectionOperator(getStoredRelation(),predicate);
		ArrayList<Tuple> reservationAgencies = selectionOperator.iterateAndReturnTuples(getTransaction());
		
		if (reservationAgencies.size() > 1){
			throw new FlightReservationsServerError("Cannot have two reservation agences with same id");
		}
		if (reservationAgencies.size()== 0){
			throw new NotFoundException("Could not find reservation agences  with id "+ id);
		}
		return tupleToReservationAgency(reservationAgencies.get(0)); 
	}

	private static ReservationAgency tupleToReservationAgency(Tuple tuple) {
		try {
			int id = ((IntegerField) tuple.getFieldNamed("id")).getValue();		
			String name = ((StringField) tuple.getFieldNamed("name")).getValue();
			String password = ((StringField) tuple.getFieldNamed("password")).getValue();
			return new ReservationAgency(id,name,password);
			} catch (InvalidAttributeNameException e) {
				throw new FlightReservationsServerError("invalid attribute name: "+e.getMessage());
			}
	}

	/**
	 * @param name
	 * @return
	 * @throws TransactionAbortedException 
	 * @throws NotFoundException 
	 */
	public static ReservationAgency getReservationAgency(String name) throws TransactionAbortedException, NotFoundException {
		Predicate predicate = EqualsPredicateFactory.newPredicate("name",StringField.valueOf(name));
		SelectionOperator selectionOperator = new SelectionOperator(getStoredRelation(),predicate);
		ArrayList<Tuple> reservationAgencies = selectionOperator.iterateAndReturnTuples(getTransaction());
		
		if (reservationAgencies.size() > 1){
			throw new FlightReservationsServerError("Cannot have two RA with same name");
		}
		if (reservationAgencies.size()== 0){
			throw new NotFoundException("Could not find RA  with name "+ name);
		}
		return tupleToReservationAgency(reservationAgencies.get(0)); 
	}

}
