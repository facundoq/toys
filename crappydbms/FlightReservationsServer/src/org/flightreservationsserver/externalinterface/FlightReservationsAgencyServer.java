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
import org.flightreservationsserverinterface.FlightReservationsAgencyInterface;
import org.flightreservationsserverinterface.dtos.CustomerDTO;
import org.flightreservationsserverinterface.dtos.FlightDTO;
import org.flightreservationsserverinterface.dtos.FlightReservationDTO;
import org.flightreservationsserverinterface.dtos.ReservationAgencyDTO;
import org.flightreservationsserverinterface.exceptions.ErrorInServerException;

/**
 * @author Facundo Manuel Quiroga
 * Jan 26, 2009
 * 
 */

public class FlightReservationsAgencyServer extends FlightReservationsServer implements FlightReservationsAgencyInterface, Serializable  {

	protected ReservationAgency reservationAgency;
	
	public FlightReservationsAgencyServer(FlightReservationsSystem flightReservationsSystem,ReservationAgency reservationAgency) throws RemoteException {
		super(flightReservationsSystem);
		this.setReservationAgency(reservationAgency);
	}

	@Override
	public ReservationAgencyDTO getCurrentReservationAgency() {	
		return POJOToDTO.reservationAgencyToReservationAgencyDTO(this.getReservationAgency());
	}

	@Override
	public ArrayList<FlightReservationDTO> getFlightReservationsBy(CustomerDTO customerDTO) throws  ErrorInServerException, org.flightreservationsserverinterface.exceptions.NotFoundException {
		try {
			TransactionManager.startTransaction();
			ArrayList<FlightReservationDTO> flightReservationDTOs = this.getFlightReservationsBy(customerDTO, this.getReservationAgency());
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
		}finally{
			TransactionManager.clearTransaction();
		}
	}

	@Override
	public ArrayList<FlightReservationDTO> getFlightReservationsFor(FlightDTO flightDTO) throws org.flightreservationsserverinterface.exceptions.NotFoundException, ErrorInServerException {
		try {
			TransactionManager.startTransaction();
			ArrayList<FlightReservationDTO> flightReservationDTOs =this.getFlightReservationsFor(flightDTO, this.getReservationAgency()); 
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
	public void bookPassenger(FlightDTO flightDTO, CustomerDTO customerDTO, ReservationAgencyDTO reservationAgencyDTO, String passengerName) throws  ErrorInServerException, org.flightreservationsserverinterface.exceptions.NotFoundException, org.flightreservationsserverinterface.exceptions.FlightFullException {
		super.bookPassenger(flightDTO, customerDTO, POJOToDTO.reservationAgencyToReservationAgencyDTO(this.getReservationAgency()), passengerName);
	}

	protected ReservationAgency getReservationAgency() {
		return this.reservationAgency;
	}

	protected void setReservationAgency(ReservationAgency reservationAgency) {
		this.reservationAgency = reservationAgency;
	}

	@Override
	protected boolean checkPermission(ReservationAgency reservationAgency) {
		return reservationAgency.getId() == this.getReservationAgency().getId();
	}

}
