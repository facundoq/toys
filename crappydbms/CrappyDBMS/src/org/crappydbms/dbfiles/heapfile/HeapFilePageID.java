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
package org.crappydbms.dbfiles.heapfile;

import org.crappydbms.dbfiles.DBFile;
import org.crappydbms.dbfiles.FilePageID;

/**
 * @author Facundo Manuel Quiroga
 * Nov 9, 2008
 * 
 */
public class HeapFilePageID implements FilePageID {


    protected long pageNumber;
    protected HeapFile file;

    public HeapFilePageID(HeapFile file,long pageNumber) {
	super();
	this.setPageNumber(pageNumber);
	this.setFile(file);
    }
    
    @Override
    public boolean equals(FilePageID heapPageID) {
	boolean sameFile = this.getFile().equals(heapPageID.getFile());
	boolean samePage = this.getPageNumber()==heapPageID.getPageNumber();
	return sameFile && samePage;
    }
    public boolean equals(Object object) {
    	FilePageID heapPageID = (FilePageID) object;
    	return this.equals(heapPageID);
        }

    @Override
    public DBFile getFile() {
	return this.file;
    }


    protected void setFile(HeapFile file) {
	this.file = file;
    }
    public int hashCode(){
	return this.getFile().hashCode()+ (int) this.getPageNumber();
    }

    @Override
    public long getPageNumber() {
	return this.pageNumber;
    }

    public void setPageNumber(long pageNumber) {
        this.pageNumber = pageNumber;
    }
    public String toString(){
	return "File: " + this.getFile().toString() + " / Page Number: " + this.getPageNumber(); 
    }

}
