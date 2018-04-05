/* CrappyDBMS, a simple relational DBMS written in Java.
    Copyright (C) 2008  Facundo Manuel Quiroga <facundoq@gmail.com>
 	
 	This file is part of CrappyDBMS.

    CrappyDBMS is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    CrappyDBMS is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with CrappyDBMS.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.crappydbms.transactions.states;

import org.crappydbms.transactions.BaseTransaction;

/**
 * @author Facundo Manuel Quiroga
 * Jan 14, 2009
 * 
 */
public class TransactionAbortedState extends TransactionNotActiveState {

	protected String reason;
	
	/**
	 * @param baseTransaction
	 * @param message
	 */
	public TransactionAbortedState(BaseTransaction baseTransaction, String reason) {
		super(baseTransaction);
		this.setReason(reason);
	}

	@Override
	public String toString() {
		return "Aborted, reason:"+this.getReason();
	}

	protected String getReason() {
		return this.reason;
	}

	protected void setReason(String reason) {
		this.reason = reason;
	}

}
