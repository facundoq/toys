/* TiledZelda, a top-down 2d action-rpg game written in Java.
    Copyright (C) 2008  Facundo Manuel Quiroga <facundoq@gmail.com>
 	
 	This file is part of TiledZelda.

    TiledZelda is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    TiledZelda is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with TiledZelda.  If not, see <http://www.gnu.org/licenses/>.
*/

package org.flightreservationsserver.database;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import org.crappydbms.datadictionary.DataDictionary;
import org.crappydbms.datadictionary.InvalidStoredRelationNameException;
import org.crappydbms.exceptions.CrappyDBMSException;
import org.crappydbms.exceptions.InvalidAttributeNameException;
import org.crappydbms.main.Database;
import org.crappydbms.relations.StoredRelation;
import org.crappydbms.relations.fields.IntegerField;
import org.crappydbms.relations.schemas.RepeatedAttributeNameException;
import org.crappydbms.relations.schemas.StoredRelationSchema;
import org.crappydbms.relations.schemas.attributes.Attribute;
import org.crappydbms.relations.schemas.attributes.BaseAttribute;
import org.crappydbms.relations.schemas.attributes.IntegerAttributeType;
import org.crappydbms.relations.schemas.attributes.StringAttributeType;
import org.crappydbms.relations.tuples.Tuple;
import org.crappydbms.transactions.Transaction;
import org.crappydbms.transactions.TransactionAbortedException;
import org.flightreservationsserver.model.exceptions.FlightReservationsServerError;
import org.flightreservationsserver.model.exceptions.FlightReservationsServerException;

/**
 * @author Facundo Quiroga
 * Creation date: Jan 28, 2009 6:30:21 PM
 */
public class DBInitializer  {
	
	
	static StoredRelationSchema[] relationSchemas = new StoredRelationSchema[4];
	
	
	public static void initializeSchemas() throws FlightReservationsServerException{
		relationSchemas[0] = getFlightsSchema();
		relationSchemas[1] = getFlightReservationsSchema();
		relationSchemas[2] = getReservationAgenciesSchema();
		relationSchemas[3] = getCustomersSchema();
	}

	protected static StoredRelationSchema getFlightsSchema() throws FlightReservationsServerException {
		 ArrayList<Attribute> attributes = new ArrayList<Attribute>();
			attributes.add(new BaseAttribute("id",IntegerAttributeType.getInstance()));
			attributes.add(new BaseAttribute("date",StringAttributeType.getInstance()));
			attributes.add(new BaseAttribute("source",StringAttributeType.getInstance()));
			attributes.add(new BaseAttribute("destination",StringAttributeType.getInstance()));
			attributes.add(new BaseAttribute("occupiedSeats",IntegerAttributeType.getInstance()));
			attributes.add(new BaseAttribute("maximumSeats",IntegerAttributeType.getInstance()));
			return createStoredRelationSchema(attributes);
	 }
	 
	 protected static StoredRelationSchema getCustomersSchema() throws FlightReservationsServerException {
		 ArrayList<Attribute> attributes = new ArrayList<Attribute>();
			attributes.add(new BaseAttribute("id",IntegerAttributeType.getInstance()));
			attributes.add(new BaseAttribute("name",StringAttributeType.getInstance()));
			attributes.add(new BaseAttribute("dni",IntegerAttributeType.getInstance()));
			attributes.add(new BaseAttribute("creditCardNumber",IntegerAttributeType.getInstance()));
			return createStoredRelationSchema(attributes);
	 }
	 
	 protected static StoredRelationSchema getFlightReservationsSchema() throws FlightReservationsServerException {
		 ArrayList<Attribute> attributes = new ArrayList<Attribute>();
			attributes.add(new BaseAttribute("id",IntegerAttributeType.getInstance()));
			attributes.add(new BaseAttribute("customerID",IntegerAttributeType.getInstance()));
			attributes.add(new BaseAttribute("reservationAgencyID",IntegerAttributeType.getInstance()));
			attributes.add(new BaseAttribute("flightID",IntegerAttributeType.getInstance()));
			attributes.add(new BaseAttribute("passengerName",StringAttributeType.getInstance()));
			attributes.add(new BaseAttribute("paid",IntegerAttributeType.getInstance()));
			attributes.add(new BaseAttribute("cancelled",IntegerAttributeType.getInstance()));
			return createStoredRelationSchema(attributes);
	 }	
	 
	 protected static StoredRelationSchema getReservationAgenciesSchema() throws FlightReservationsServerException {
		 ArrayList<Attribute> attributes = new ArrayList<Attribute>();
			attributes.add(new BaseAttribute("id",IntegerAttributeType.getInstance()));
			attributes.add(new BaseAttribute("name",StringAttributeType.getInstance()));
			attributes.add(new BaseAttribute("password",StringAttributeType.getInstance()));
			return createStoredRelationSchema(attributes);
	 }	
	 
	protected static StoredRelationSchema createStoredRelationSchema(ArrayList<Attribute> attributes) throws FlightReservationsServerException {
		try {
			return new StoredRelationSchema(attributes);
		} catch (RepeatedAttributeNameException e) {
			throw new FlightReservationsServerException("Error initializing Flights' StoredRelationSchema");
		}
	}
	
	static Database initializeDatabase(File directory) throws  FlightReservationsServerException, IOException{
		initializeSchemas();
		if (!directory.exists()){
			directory.mkdir();
		}
		DataDictionary dataDictionary;
		try {
			dataDictionary = new DataDictionary(directory);
		} catch (ParseException e) {
			throw new FlightReservationsServerException(e.getMessage());
		} catch (CrappyDBMSException e) {
			throw new FlightReservationsServerException(e.getMessage());
		}
		Database database = new Database(dataDictionary);
		
		ArrayList<Attribute> primaryKey = new ArrayList<Attribute>();
		primaryKey.add(new BaseAttribute("id",IntegerAttributeType.getInstance()));
		String[] relationNames = DAO.relationNames;
		for (int i = 0 ; i< relationNames.length; i++){
			try {
				database.getStoredRelationNamed(relationNames[i]);
			} catch (InvalidStoredRelationNameException e) {
				try {
					database.addStoredRelation(relationNames[i], relationSchemas[i], primaryKey);
				} catch (CrappyDBMSException e1) {
					throw new FlightReservationsServerException(e1.getMessage());
				}
			}
		}
		
		return database;
	}

	public static void initializeDAOsInitialIDs(Transaction transaction) throws TransactionAbortedException {

	 ReservationAgencyDAO.nextID = getMaximumIDOf(ReservationAgencyDAO.getStoredRelation(),transaction)+1;
	 FlightDAO.nextID = getMaximumIDOf(FlightDAO.getStoredRelation(),transaction)+1;
	 FlightReservationDAO.nextID = getMaximumIDOf(FlightReservationDAO.getStoredRelation(),transaction)+1;
	 CustomerDAO.nextID = getMaximumIDOf(CustomerDAO.getStoredRelation(),transaction)+1;
		
	}

	private static int getMaximumIDOf(StoredRelation storedRelation, Transaction transaction) throws TransactionAbortedException {

		ArrayList<Tuple> tuples = storedRelation.iterateAndReturnTuples(transaction);
		return getMaximumID(tuples);
	}

	private static int getMaximumID(ArrayList<Tuple> tuples){
		int max = -1;
		for (Tuple tuple : tuples){
			int id;
			try {
				id = ( (IntegerField) tuple.getFieldNamed("id")).getValue();
			} catch (InvalidAttributeNameException e) {
				throw new FlightReservationsServerError(e.getMessage());
			} 
			if (id > max){
				max = id;
			}
		}
		return max;
	}
	
}
