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
package org.crappydbms.transactions;

import org.crappydbms.exceptions.CrappyDBMSError;

/**
 * @author Facundo Manuel Quiroga
 * Dec 24, 2008
 * 
 */
public class LockMode {
	protected static int LOCK_SHARED = 0;
	protected static int LOCK_EXCLUSIVE= 1;
	
	protected int mode;
	
	protected static LockMode shared = new LockMode(LockMode.LOCK_SHARED);
	protected static LockMode exclusive = new LockMode(LockMode.LOCK_EXCLUSIVE);
	
	public static LockMode shared(){
		return LockMode.shared;
	}
	public static LockMode exclusive(){
		return LockMode.exclusive;
	}
	
	
	private LockMode(int mode){
		this.mode = mode;
	}
	
	public String toString(){
		if (this.mode == LOCK_EXCLUSIVE) {
			return "Exclusive LockMode";
		}else if (this.mode == LOCK_SHARED) {
			return "Shared LockMode";
		}else{
			throw new CrappyDBMSError("Invalid LockMode "+this.mode);
		}
	}

}
