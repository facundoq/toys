/**
 * 
 */
package org.flightreservationsserver.database;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.crappydbms.datadictionary.InvalidStoredRelationNameException;
import org.crappydbms.main.Database;
import org.crappydbms.queries.operations.Modification;
import org.crappydbms.queries.operations.ValueModification;
import org.crappydbms.relations.StoredRelation;
import org.crappydbms.relations.fields.Field;
import org.crappydbms.relations.schemas.attributes.Attribute;
import org.crappydbms.relations.tuples.InvalidFieldPositionException;
import org.crappydbms.relations.tuples.StoredTuple;
import org.crappydbms.transactions.Transaction;
import org.crappydbms.transactions.TransactionAbortedException;
import org.flightreservationsserver.model.exceptions.FlightReservationsServerError;
import org.flightreservationsserver.model.exceptions.FlightReservationsServerException;

/**
 * @author Facundo Manuel Quiroga Jan 24, 2009
 */
public class DAO {
	
	static String[] relationNames = {"flights", "flightReservations","reservationAgencies","customers" };
	
	public static Database database;

	public static File getDirectory(){
		return 	new File("flightReservationsServerDB/");
	}
	public static void initialize() throws FlightReservationsServerException, IOException {
		database = DBInitializer.initializeDatabase(getDirectory());
		
		try {
			DBInitializer.initializeDAOsInitialIDs(database.newTransaction());
		} catch (TransactionAbortedException e) {
			throw new FlightReservationsServerException(e.getMessage());
		}
	}

	public static Transaction getTransaction(){
		return TransactionManager.getTransaction();
	}

	protected static Database getDatabase() {
		return database;
	}

	public static StoredRelation getStoredRelation(String name) throws InvalidStoredRelationNameException {
		return database.getStoredRelationNamed(name);
	}

	
	protected static ArrayList<Modification> getModifications(StoredTuple tuple){
		
		ArrayList<Modification> modifications = new ArrayList<Modification>();
		for (int i = 0 ; i< tuple.numberOfFields();i++){
			Field field;
			try {
				field = tuple.getFieldAtPosition(i);
			} catch (InvalidFieldPositionException e) {
				throw new FlightReservationsServerError("InvalidFieldPositionException  "+e.getMessage());
			}
			Attribute attribute = tuple.getRelationSchema().getAttribute(i);
			ValueModification modification = new ValueModification(attribute.getName(),field);
			modifications.add(modification);
		}
		return modifications;
	}





}
