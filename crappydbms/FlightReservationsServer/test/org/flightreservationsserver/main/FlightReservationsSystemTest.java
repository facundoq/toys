/**
 * 
 */
package org.flightreservationsserver.main;

import java.io.File;
import java.io.IOException;

import org.crappydbms.exceptions.CrappyDBMSException;
import org.crappydbms.transactions.TransactionAbortedException;
import org.flightreservationsserver.database.DAO;
import org.flightreservationsserver.database.NotFoundException;
import org.flightreservationsserver.database.TransactionManager;
import org.flightreservationsserver.model.Customer;
import org.flightreservationsserver.model.Flight;
import org.flightreservationsserver.model.FlightReservation;
import org.flightreservationsserver.model.FlightReservationsSystem;
import org.flightreservationsserver.model.ReservationAgency;
import org.flightreservationsserver.model.exceptions.FlightFullException;
import org.flightreservationsserver.model.exceptions.FlightReservationAlreadyCanceledException;
import org.flightreservationsserver.model.exceptions.FlightReservationsServerException;
import org.flightreservationsserverinterface.exceptions.ErrorInServerException;
import org.flightreservationsserverinterface.exceptions.ReservationAgencyRepeatedNameException;

/**
 * @author Facundo Manuel Quiroga
 * Jan 31, 2009
 * 
 */
public class FlightReservationsSystemTest {
	protected FlightReservationsSystem flightReservationsSystem;

	public FlightReservationsSystemTest() {
		try {
			this.deleteDirectory(DAO.getDirectory());
			this.flightReservationsSystem = new FlightReservationsSystem();
		} catch (FlightReservationsServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] argv) {
		FlightReservationsSystemTest flightReservationsSystemTest = new FlightReservationsSystemTest();
		/*System.out.println(FlightDAO.getNextID());
		System.out.println(FlightReservationDAO.getNextID());
		System.out.println(CustomerDAO.getNextID());
		System.out.println(ReservationAgencyDAO.getNextID());
		System.out.println(FlightDAO.getStoredRelation());
		System.out.println(FlightReservationDAO.getStoredRelation());
		System.out.println(CustomerDAO.getStoredRelation());
		System.out.println(ReservationAgencyDAO.getStoredRelation());*/
		flightReservationsSystemTest.testSystem();
		flightReservationsSystemTest.finish();
	}

	/**
	 * 
	 */
	private void testSystem() {
		try {
			TransactionManager.startTransaction();
			Flight cubaChina = flightReservationsSystem.addFlight("cuba", "china", "2009-01-10", 30);
			Flight cubaJapan = flightReservationsSystem.addFlight("cuba", "japan", "2009-01-15", 2);
			flightReservationsSystem.getFlight(cubaChina.getId());
			flightReservationsSystem.getFlight(cubaJapan.getId());
			assert (!flightReservationsSystem.getFlights("cuba", "china", "2009-01-09", "2009-01-10").isEmpty());
			assert (!flightReservationsSystem.getFlights("cuba", "japan", "2009-01-15", "3000-01-15").isEmpty());
			assert (!flightReservationsSystem.getFlights("cuba", "japan", "2009-01-15", "3000-01-16").isEmpty());
			assert (flightReservationsSystem.getFlights("cuba", "japan", "2009-01-05", "2009-01-06").isEmpty());

			Customer pepe = flightReservationsSystem.addCustomer(0, "pepe", 0);
			Customer juan = flightReservationsSystem.addCustomer(1, "juan", 1);
			flightReservationsSystem.getCustomerByID(pepe.getId());
			flightReservationsSystem.getCustomerByID(juan.getId());
			assert (!flightReservationsSystem.getCustomerByDNI(0).isEmpty());
			assert (!flightReservationsSystem.getCustomerByDNI(1).isEmpty());
			assert (!flightReservationsSystem.getCustomersByName("pepe").isEmpty());
			assert (!flightReservationsSystem.getCustomersByName("juan").isEmpty());
			assert (flightReservationsSystem.getCustomersByName("matias").isEmpty());

			ReservationAgency ra1= null;
			ReservationAgency ra2 = null;
			try {
				ra1 = flightReservationsSystem.addReservationAgency("ra1", "ra1");
				 ra2 = flightReservationsSystem.addReservationAgency("ra2", "ra2");
			} catch (ReservationAgencyRepeatedNameException e1) {
				assert(false);
			}

			flightReservationsSystem.getReservationAgency(ra1.getId());
			flightReservationsSystem.getReservationAgency(ra2.getId());
			flightReservationsSystem.getReservationAgency("ra1");
			flightReservationsSystem.getReservationAgency("ra2");

			FlightReservation flightReservationPepe = null;
			FlightReservation flightReservationJuan = null;
			// add pepe's and juan's reservations
			try {
				flightReservationPepe = flightReservationsSystem.bookPassenger(cubaJapan, pepe, ra1, "cholito");
				flightReservationJuan = flightReservationsSystem.bookPassenger(cubaJapan, juan, ra1, "juan");
			} catch (FlightFullException e) {
				e.printStackTrace();
			}
			try {
				flightReservationsSystem.bookPassenger(cubaJapan, juan, ra1, "juan");
				assert (false);
			} catch (FlightFullException e) {
			}
			// cancel pepe's reservation
			try {
				flightReservationsSystem.cancelReservation(flightReservationPepe);
			} catch (FlightReservationAlreadyCanceledException e) {
				assert (false);
			}
			try {
				flightReservationsSystem.cancelReservation(flightReservationPepe);
				assert (false);
			} catch (FlightReservationAlreadyCanceledException e) {

			}
			//add another reservation from pepe
			try {
				flightReservationPepe = flightReservationsSystem.bookPassenger(cubaJapan, pepe, ra1, "cholito");
			} catch (FlightFullException e) {
				e.printStackTrace();
			}
			try {
				flightReservationsSystem.bookPassenger(cubaJapan, juan, ra1, "juan");
				assert (false);
			} catch (FlightFullException e) {
			}
			flightReservationsSystem.getFlightReservation(flightReservationJuan.getId());
			flightReservationsSystem.getFlightReservation(flightReservationPepe.getId());
			/*for (FlightReservation flightReservation : flightReservationsSystem.getFlightReservationsBy(juan, ra1)) {
				System.out.println("id " + flightReservation.getId() + " customer " + flightReservation.getCustomer().getName() + " flight "
						+ flightReservation.getFlight().getSource() + "-" + flightReservation.getFlight().getDestination());
			}*/
			assert (flightReservationsSystem.getFlightReservationsBy(juan, ra1).size() == 1);
			assert (flightReservationsSystem.getFlightReservationsBy(juan, ra2).isEmpty());
			assert (flightReservationsSystem.getFlightReservationsBy(pepe, ra1).size() == 2);
			assert (flightReservationsSystem.getFlightReservationsBy(pepe, ra2).isEmpty());

			assert (flightReservationsSystem.getFlightReservationsFor(cubaJapan, ra1).size() == 3);
			assert (flightReservationsSystem.getFlightReservationsFor(cubaChina, ra1).size() == 0);
			TransactionManager.commitTransaction();
			//DAO.abortTransaction();

		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (TransactionAbortedException e) {
			e.printStackTrace();
		} catch (ErrorInServerException e) {
			e.printStackTrace();
		}

	}

	protected void deleteDirectory(File directory) {
		if (directory.isDirectory()) {
			for (File file : directory.listFiles()) {
				deleteDirectory(file);
			}
		}
		directory.delete();
	}

	protected void finish() {
		try {
			flightReservationsSystem.shutdown();
			this.deleteDirectory(DAO.getDirectory());
		} catch (CrappyDBMSException e) {
			e.printStackTrace();
		}

	}
}
