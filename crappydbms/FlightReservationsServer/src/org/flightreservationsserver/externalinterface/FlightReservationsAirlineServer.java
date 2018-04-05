/**
 * 
 */
package org.flightreservationsserver.externalinterface;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

import org.crappydbms.transactions.TransactionAbortedException;
import org.flightreservationsserver.database.NotFoundException;
import org.flightreservationsserver.database.TransactionManager;
import org.flightreservationsserver.model.FlightReservationsSystem;
import org.flightreservationsserver.model.ReservationAgency;
import org.flightreservationsserverinterface.FlightReservationsAirlineInterface;
import org.flightreservationsserverinterface.dtos.CustomerDTO;
import org.flightreservationsserverinterface.dtos.FlightDTO;
import org.flightreservationsserverinterface.dtos.FlightReservationDTO;
import org.flightreservationsserverinterface.dtos.ReservationAgencyDTO;
import org.flightreservationsserverinterface.exceptions.ErrorInServerException;
import org.flightreservationsserverinterface.exceptions.ReservationAgencyRepeatedNameException;

/**
 * @author Facundo Manuel Quiroga Feb 2, 2009
 */
public class FlightReservationsAirlineServer extends FlightReservationsServer implements FlightReservationsAirlineInterface, Serializable {

	/**
	 * @param flightReservationsSystem
	 * @throws RemoteException
	 */
	public FlightReservationsAirlineServer(FlightReservationsSystem flightReservationsSystem) throws RemoteException {
		super(flightReservationsSystem);
	}

	@Override
	public void addFlight(String source, String destination, String date, int maximumSeats) throws ErrorInServerException {
		try {
			TransactionManager.startTransaction();
			this.getFlightReservationsSystem().addFlight(source, destination, date, maximumSeats);
			TransactionManager.commitTransaction();
		} catch (TransactionAbortedException e) {
			TransactionManager.clearTransaction();
			this.synchronizedErrPrint("Transaction aborted in addFlight");
			throw new ErrorInServerException("Aborted transaction");
		} catch (Exception e) {
			TransactionManager.abortTransaction();
			this.synchronizedErrPrint("Unknown Error in addFlight");
			throw new ErrorInServerException("Unknown Error");
		} finally{
			TransactionManager.clearTransaction();
		}

	}

	@Override
	public void addReservationAgency(String name, String password) throws ErrorInServerException, ReservationAgencyRepeatedNameException {
		try {
			TransactionManager.startTransaction();
			this.getFlightReservationsSystem().addReservationAgency(name, password);
			TransactionManager.commitTransaction();
		} catch (TransactionAbortedException e) {
			TransactionManager.clearTransaction();
			this.synchronizedErrPrint("Transaction aborted in addReservationAgency");
			throw new ErrorInServerException();
		} catch (ReservationAgencyRepeatedNameException e) {
			TransactionManager.abortTransaction();
			throw e;
		} catch (Exception e) {
			TransactionManager.abortTransaction();
			this.synchronizedErrPrint("Unknown Error in addReservationAgency");
			throw new ErrorInServerException("Unknown Error");
		}finally{
			TransactionManager.clearTransaction();
		}
	}

	@Override
	public ArrayList<FlightReservationDTO> getFlightReservationsBy(CustomerDTO customerDTO, ReservationAgencyDTO reservationAgencyDTO) throws org.flightreservationsserverinterface.exceptions.NotFoundException, ErrorInServerException {
		try {
			TransactionManager.startTransaction();
			ReservationAgency reservationAgency = this.getReservationAgencyFromSystem(reservationAgencyDTO.getId());
			ArrayList<FlightReservationDTO> flightReservationDTOs = this.getFlightReservationsBy(customerDTO, reservationAgency);
			TransactionManager.commitTransaction();
			return flightReservationDTOs;
		} catch (TransactionAbortedException e) {
			TransactionManager.clearTransaction();
			this.synchronizedErrPrint("Transaction aborted in getFlightReservationsBy");
			throw new ErrorInServerException();
		} catch (NotFoundException e) {
			TransactionManager.abortTransaction();
			throw new org.flightreservationsserverinterface.exceptions.NotFoundException();
		} catch (Exception e) {
			TransactionManager.abortTransaction();
			this.synchronizedErrPrint("Unknown Error in getFlightReservationsBy");
			throw new ErrorInServerException("Unknown Error");
		} finally {
			TransactionManager.clearTransaction();
		}
	}

	@Override
	public ArrayList<FlightReservationDTO> getFlightReservationsFor(FlightDTO flightDTO, ReservationAgencyDTO reservationAgencyDTO) throws org.flightreservationsserverinterface.exceptions.NotFoundException, ErrorInServerException {
		try {
			TransactionManager.startTransaction();
			ReservationAgency reservationAgency = this.getReservationAgencyFromSystem(reservationAgencyDTO.getId());
			ArrayList<FlightReservationDTO> flightReservationDTOs =  this.getFlightReservationsFor(flightDTO, reservationAgency);
			TransactionManager.commitTransaction();
			return flightReservationDTOs;
		} catch (TransactionAbortedException e) {
			TransactionManager.clearTransaction();
			this.synchronizedErrPrint("Transaction aborted in getFlightReservationsFor");
			throw new ErrorInServerException();
		} catch (NotFoundException e) {
			TransactionManager.abortTransaction();
			throw new org.flightreservationsserverinterface.exceptions.NotFoundException();
		} catch (Exception e) {
			TransactionManager.abortTransaction();
			this.synchronizedErrPrint("Unknown Error in getFlightReservationsFor");
			throw new ErrorInServerException("Unknown Error");
		}finally{
			TransactionManager.clearTransaction();
		}
	}

	@Override
	public ReservationAgencyDTO getReservationAgency(int id) throws org.flightreservationsserverinterface.exceptions.NotFoundException, ErrorInServerException {
		try {
			TransactionManager.startTransaction();
			ReservationAgencyDTO reservationAgencyDTO = POJOToDTO.reservationAgencyToReservationAgencyDTO(this.getReservationAgencyFromSystem(id));
			TransactionManager.commitTransaction();
			return reservationAgencyDTO;
		} catch (TransactionAbortedException e) {
			TransactionManager.clearTransaction();
			this.synchronizedErrPrint("Transaction aborted in getReservationAgency(int)");
			throw new ErrorInServerException();
		} catch (NotFoundException e) {
			TransactionManager.abortTransaction();
			throw new org.flightreservationsserverinterface.exceptions.NotFoundException();
		} catch (Exception e) {
			TransactionManager.abortTransaction();
			this.synchronizedErrPrint("Unknown Error in getReservationAgency(int)");
			throw new ErrorInServerException("Unknown Error");
		}finally{
			TransactionManager.clearTransaction();
		}
	}

	@Override
	public ReservationAgencyDTO getReservationAgency(String name) throws ErrorInServerException, org.flightreservationsserverinterface.exceptions.NotFoundException {
		try {
			TransactionManager.startTransaction();
			ReservationAgencyDTO reservationAgencyDTO = POJOToDTO.reservationAgencyToReservationAgencyDTO(this.getFlightReservationsSystem().getReservationAgency(name));
			TransactionManager.commitTransaction();
			return reservationAgencyDTO; 
		} catch (TransactionAbortedException e) {
			TransactionManager.clearTransaction();
			this.synchronizedErrPrint("Transaction aborted in getReservationAgency(name)");
			throw new ErrorInServerException();
		} catch (NotFoundException e) {
			TransactionManager.abortTransaction();
			throw new org.flightreservationsserverinterface.exceptions.NotFoundException();
		} catch (Exception e) {
			TransactionManager.abortTransaction();
			this.synchronizedErrPrint("Unknown Error in getReservationAgency(name)");
			throw new ErrorInServerException("Unknown Error");
		}finally{
			TransactionManager.clearTransaction();
		}
	}

	@Override
	protected boolean checkPermission(ReservationAgency reservationAgency) {
		return true;
	}

}
