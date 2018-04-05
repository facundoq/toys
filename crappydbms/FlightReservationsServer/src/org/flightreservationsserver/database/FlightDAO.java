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
import org.crappydbms.queries.predicates.composite.NotPredicate;
import org.crappydbms.queries.predicates.simple.factories.EqualsPredicateFactory;
import org.crappydbms.queries.predicates.simple.factories.GreaterPredicateFactory;
import org.crappydbms.queries.predicates.simple.factories.SmallerPredicateFactory;
import org.crappydbms.relations.RelationIterator;
import org.crappydbms.relations.StoredRelation;
import org.crappydbms.relations.fields.Field;
import org.crappydbms.relations.fields.IntegerField;
import org.crappydbms.relations.fields.StringField;
import org.crappydbms.relations.tuples.StoredTuple;
import org.crappydbms.relations.tuples.Tuple;
import org.crappydbms.transactions.TransactionAbortedException;
import org.flightreservationsserver.model.Flight;
import org.flightreservationsserver.model.exceptions.FlightReservationsServerError;


/**
 * @author Facundo Manuel Quiroga
 * Jan 24, 2009
 * 
 */
public class FlightDAO extends DAO {
	// TODO get id
	static int nextID;
	public static synchronized  int getNextID() {
		int id = nextID;
		nextID++;
		return id;
	}
	
	public static StoredRelation getStoredRelation(){
		try {
			return getStoredRelation(relationNames[0]);
		} catch (InvalidStoredRelationNameException e) {
			throw new FlightReservationsServerError(e.getMessage());
		}
	}
	/**
	 * @param flight
	 * @throws TransactionAbortedException 
	 */
	public static Flight addFlight(Flight flight) throws TransactionAbortedException {
		flight.setId(getNextID());
		StoredTuple tuple = flightToTuple(flight);
		StoredRelation storedRelation = getStoredRelation();
		storedRelation.addTuple(tuple, getTransaction());
		return flight;
	}

	protected static StoredTuple flightToTuple(Flight flight) {
		ArrayList<Field> fields = new ArrayList<Field>();
		fields.add(IntegerField.valueOf(flight.getId()));
		fields.add(StringField.valueOf(flight.getDate()));
		fields.add(StringField.valueOf(flight.getSource()));
		fields.add(StringField.valueOf(flight.getDestination()));
		fields.add(IntegerField.valueOf(flight.getOccupiedSeats()));
		fields.add(IntegerField.valueOf(flight.getMaximumSeats()));		
		return new StoredTuple(fields,DBInitializer.relationSchemas[0]);
	}
	
	protected static Flight  tupleToFlight(Tuple tuple) {
		int id;
		try {
		id = ((IntegerField) tuple.getFieldNamed("id")).getValue();
	
		String date = ((StringField) tuple.getFieldNamed("date")).getValue();
		String source = ((StringField) tuple.getFieldNamed("source")).getValue();
		String destination = ((StringField) tuple.getFieldNamed("destination")).getValue();
		int occupiedSeats = ((IntegerField) tuple.getFieldNamed("occupiedSeats")).getValue();
		int maximumSeats = ((IntegerField) tuple.getFieldNamed("maximumSeats")).getValue();
		return new Flight(id,date, source,destination,maximumSeats, occupiedSeats);
		} catch (InvalidAttributeNameException e) {
			throw new FlightReservationsServerError("invalid attribute name: "+e.getMessage());
		}
		
	}

	/**
	 * @param id
	 * @return
	 * @throws TransactionAbortedException 
	 * @throws NotFoundException 
	 */
	public static Flight getFlight(int id) throws TransactionAbortedException, NotFoundException {
		Predicate predicate = EqualsPredicateFactory.newPredicate("id",IntegerField.valueOf(id));
		SelectionOperator selectionOperator = new SelectionOperator(getStoredRelation(),predicate);
		RelationIterator<Tuple> iterator = selectionOperator.iterator(getTransaction());
		if (!iterator.hasNext()){
			throw new NotFoundException("Flight id "+id);
		}
		
		return tupleToFlight(iterator.next());
	}

	/**
	 * @param flight
	 * @throws TransactionAbortedException 
	 */
	public static void saveFlight(Flight flight) throws TransactionAbortedException {
		ArrayList<Modification> modifications = getModifications(flightToTuple(flight));
		Predicate predicate = EqualsPredicateFactory.newPredicate("id", IntegerField.valueOf(flight.getId()));
		ModifyOperation modifyOperation = new ModifyOperation(getStoredRelation(),predicate,modifications);
		try {
			modifyOperation.perform(getTransaction());
		} catch (InvalidAttributeNameException e) {
			throw new FlightReservationsServerError("InvalidAttributeNameException  "+e.getMessage());
		}
	}
	

	/**
	 * @param source
	 * @param destination
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws TransactionAbortedException 
	 */
	public static ArrayList<Flight> getFlights(String source, String destination, String fromDate, String toDate) throws TransactionAbortedException {
		Predicate predicate = EqualsPredicateFactory.newPredicate("source",StringField.valueOf(source));
		predicate = predicate.and(EqualsPredicateFactory.newPredicate("destination",StringField.valueOf(destination)));
		predicate = predicate.and( new NotPredicate(SmallerPredicateFactory.newPredicate("date", StringField.valueOf(fromDate))) );
		predicate = predicate.and( new NotPredicate(GreaterPredicateFactory.newPredicate("date", StringField.valueOf(toDate))) );
	
		SelectionOperator selectionOperator = new SelectionOperator(getStoredRelation(),predicate);
		RelationIterator<Tuple> iterator = selectionOperator.iterator(getTransaction());
		ArrayList<Flight> flights = new ArrayList<Flight>();
		while (iterator.hasNext()){
			Tuple tuple = iterator.next();
			flights.add(tupleToFlight(tuple));
		}
		return flights;
	}

}
