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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.crappydbms.dbfiles.DBFile;
import org.crappydbms.dbfiles.FilePage;
import org.crappydbms.dbfiles.heapfile.HeapFile;
import org.crappydbms.dbfiles.heapfile.HeapFilePage;
import org.crappydbms.dbfiles.heapfile.HeapFilePageID;
import org.crappydbms.main.PageBufferFullException;
import org.crappydbms.relations.tuples.StoredTuple;

/**
 * @author Facundo Manuel Quiroga
 * Jan 14, 2009
 * 
 */
public class TransactionPagesManager {

	
	protected BaseTransaction baseTransaction;
	
	protected HashMap<DBFile, ArrayList<FilePage>> pagesAddedPerDBFile;
	protected HashMap<DBFile, Long> numberOfValidPagesInFile;
	
	public TransactionPagesManager(BaseTransaction baseTransaction){
		this.setBaseTransaction(baseTransaction);
		this.setPagesAddedPerDBFile(new HashMap<DBFile, ArrayList<FilePage>>());
		this.setNumberOfValidPagesInFile(new HashMap<DBFile, Long>());
	}

	public void addPage(FilePage filePage) throws PageBufferFullException, IOException   {
		// Adding a new page that occupies memory requires permission to do so from the page manager.

			this.getBaseTransaction().getDatabase().getPageManager().addPage();
			DBFile DBFile = filePage.getFilePageID().getFile();
			ArrayList<FilePage> pages = this.getPages(DBFile);
			pages.add(filePage);

	}
	public long getNumberOfPagesOfDBFile(DBFile DBFile){
		Long numberOfPages = this.getNumberOfValidPagesInFile().get(DBFile);
		if (numberOfPages == null){
			numberOfPages = DBFile.getNumberOfPages();
			this.getNumberOfValidPagesInFile().put(DBFile, numberOfPages);
		}
		return numberOfPages;
	}
	
	public ArrayList<FilePage> getPages(DBFile DBFile){
		ArrayList<FilePage> pages = this.getPagesAddedPerDBFile().get(DBFile);
		if (pages == null){
			pages = new ArrayList<FilePage>();
			this.getPagesAddedPerDBFile().put(DBFile, pages);
		}
		return pages;
	}
	
	protected BaseTransaction getBaseTransaction() {
		return this.baseTransaction;
	}

	protected void setBaseTransaction(BaseTransaction baseTransaction) {
		this.baseTransaction = baseTransaction;
	}
	
	protected HashMap<DBFile, Long> getNumberOfValidPagesInFile() {
		return this.numberOfValidPagesInFile;
	}

	protected void setNumberOfValidPagesInFile(HashMap<DBFile, Long> numberOfValidPagesInFile) {
		this.numberOfValidPagesInFile = numberOfValidPagesInFile;
	}
	
	public HashMap<DBFile, ArrayList<FilePage>> getPagesAddedPerDBFile() {
		return this.pagesAddedPerDBFile;
	}

	protected void setPagesAddedPerDBFile(HashMap<DBFile, ArrayList<FilePage>> pagesAddedPerDBFile) {
		this.pagesAddedPerDBFile = pagesAddedPerDBFile;
	}

	/**
	 * @param tuple to add
	 * @throws TransactionAbortedException 
	 * @throws IOException 
	 * @throws PageBufferFullException 
	 */
	public void addTuple(StoredTuple tuple,HeapFile DBFile) throws TransactionAbortedException, PageBufferFullException, IOException {
		
		// check if there is a page added by this transaction that is not full 
		HeapFilePage heapFilePage = null;
		ArrayList<FilePage> pages = this.getPages(DBFile);
		
		// check the last page, if it exists
		// there could not full pages somewhere in the list because of a delete operation after an add
		// but it would be very uncommon for a transaction to create and delete tuples in the same transaction,
		// and if so, it should not create much fragmentation
		int numberOfPages = pages.size();
		if (numberOfPages > 0){
			FilePage filePage = pages.get(numberOfPages-1);
			if (!filePage.isFull()){
				heapFilePage = (HeapFilePage) filePage;
			}
		}
		// if there weren't any pages or the last page was full create a new one and add it.
		if (heapFilePage == null){
			HeapFilePageID nullPageID = new HeapFilePageID(DBFile, -1);
			heapFilePage = new HeapFilePage(nullPageID);
			this.addPage(heapFilePage);
		}
		heapFilePage.addTuple(tuple);
	}

	/**
	 * 
	 */
	public void discardPages() {
		this.getPagesAddedPerDBFile().clear();
		
	}

}
