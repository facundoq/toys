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
package org.crappydbms.dbfiles.locking;

import java.util.HashMap;

import org.crappydbms.dbfiles.FilePageID;



/**
 * @author Facundo Manuel Quiroga
 * Jan 5, 2009
 * 
 */
public class DBFileLockManager {

	
	public DBFileLockManager(){
		this.setPageLocks(new HashMap<FilePageID, PageLock>());
	}
	
	protected HashMap<FilePageID, PageLock> pageLocks;

	public synchronized PageLock getPageLockFor(FilePageID filePageID) {
		PageLock lock = this.getPageLocks().get(filePageID);
		if ( lock == null ){
			lock = new PageLock(filePageID);
			this.getPageLocks().put(filePageID,lock);
		}
		return lock;
	}

	public HashMap<FilePageID, PageLock> getPageLocks() {
		return this.pageLocks;
	}
	protected void setPageLocks(HashMap<FilePageID, PageLock> pageLocks) {
		this.pageLocks = pageLocks;
	}
	public String toString(){
		String result="{ ";
		for (PageLock pageLock : this.getPageLocks().values()){
			result += pageLock.toString()+" / ";
		}
		result += "}";
		return result;
	}

}
