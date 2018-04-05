/**
 * 
 */
package org.flightreservationsserver.externalinterface;

import java.io.Serializable;
import java.rmi.RemoteException;

import org.crappydbms.transactions.TransactionAbortedException;
import org.flightreservationsserver.database.NotFoundException;
import org.flightreservationsserver.database.TransactionManager;
import org.flightreservationsserver.model.FlightReservationsSystem;
import org.flightreservationsserver.model.ReservationAgency;
import org.flightreservationsserverinterface.FlightReservationsAgencyInterface;
import org.flightreservationsserverinterface.FlightReservationsAirlineInterface;
import org.flightreservationsserverinterface.FlightReservationsLoginInterface;
import org.flightreservationsserverinterface.exceptions.ErrorInServerException;
import org.flightreservationsserverinterface.exceptions.InvalidLoginException;

/**
 * @author Facundo Manuel Quiroga Jan 31, 2009
 */
public class FlightReservationsLoginServer implements FlightReservationsLoginInterface, Serializable {

	protected FlightReservationsSystem flightReservationsSystem;

	public FlightReservationsLoginServer(FlightReservationsSystem flightReservationsSystem) {
		this.setFlightReservationsSystem(flightReservationsSystem);
	}

	@Override
	public FlightReservationsAgencyInterface loginAsReservationAgency(String reservationAgencyName, String password) throws InvalidLoginException, ErrorInServerException, RemoteException {
		ReservationAgency reservationAgency;
		try {
			TransactionManager.startTransaction();
			reservationAgency = this.getFlightReservationsSystem().getReservationAgency(reservationAgencyName);
			if ( ! reservationAgency.getPassword().equals(password)) {
				TransactionManager.abortTransaction();
				throw new InvalidLoginException();
			}
			FlightReservationsAgencyServer flightReservationsAgencyServer = new FlightReservationsAgencyServer(this.getFlightReservationsSystem(), reservationAgency);
			TransactionManager.commitTransaction();
			return (FlightReservationsAgencyInterface) flightReservationsAgencyServer;

		} catch (NotFoundException e) {
			TransactionManager.abortTransaction();
			throw new InvalidLoginException();
		} catch (TransactionAbortedException e) {
			TransactionManager.clearTransaction();
			this.synchronizedErrPrint("Transaction aborted in loginAsReservationAgency");
			throw new ErrorInServerException();
		} catch (Exception e) {
			TransactionManager.abortTransaction();
			this.synchronizedErrPrint("Unknown Error in loginAsReservationAgency");
			throw new ErrorInServerException("Unknown Error");
		}finally{
			TransactionManager.clearTransaction();
		}
	}

	@Override
	public FlightReservationsAirlineInterface loginAsAirline(String username, String password) throws RemoteException, InvalidLoginException {
		/*FlightReservationsAirlineInterface flightReservationsAirlineInterface =new FlightReservationsAirlineServer(this.getFlightReservationsSystem());
		return flightReservationsAirlineInterface;*/
		if (username.equals("airline") && password.equals("airline")) {
			return new FlightReservationsAirlineServer(this.getFlightReservationsSystem());
		}
		throw new InvalidLoginException();
	}

	protected FlightReservationsSystem getFlightReservationsSystem() {
		return this.flightReservationsSystem;
	}

	protected void setFlightReservationsSystem(FlightReservationsSystem flightReservationsSystem) {
		this.flightReservationsSystem = flightReservationsSystem;
	}
	protected void synchronizedErrPrint(String message){
		synchronized(this){
			System.err.println(message);
		}
	}
	protected void synchronizedOutPrint(String message){
		synchronized(this){
			System.err.println(message);
		}
	}
}
