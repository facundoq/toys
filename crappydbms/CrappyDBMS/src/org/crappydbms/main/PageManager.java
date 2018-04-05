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
package org.crappydbms.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.crappydbms.dbfiles.DBFile;
import org.crappydbms.logging.CrappyDBMSLogger;
import org.crappydbms.relations.StoredRelation;



/**
 * @author Facundo Manuel Quiroga
 * Jan 5, 2009
 * 
 */
public class PageManager {
	private static final Logger fLogger = CrappyDBMSLogger.getLogger(PageManager.class.getPackage().getName());
	
	protected Database database;

	protected int indexOfLastFileDiscarded; 
	protected int maxPagesToDiscard; 
	
	protected long pages;
	protected long maxPages;
	
	protected Object lock = new Object();

	protected PageManager(Database database){
		this.setDatabase(database);
		maxPagesToDiscard = 1;
		pages = 0;
		maxPages = 5000;
		indexOfLastFileDiscarded = 0;
	}
	
	/**
	 *  Checks if there is enough space in memory to add an extra page.
	 * @throws IOException if there is an error evicting pages
	 *  @throws PageBufferFullException if there is not enough space in memory and no pages to evict
	 */
	public  void addPage() throws IOException, PageBufferFullException{
		synchronized (this.lock){
			// try to add the page; if successful, return true
			if (!this.tryToAddPage()){
				// there aren't enough pages, try to discard some.
				long discardedPages = this.evictUnusedPages();
				this.pages = this.pages - discardedPages;
				// try to add the page again, and return if it was possible to do so
				// need to do this because discards and checking use different locks
				 if(!this.tryToAddPage()){
					 throw new PageBufferFullException("Buffer size: "+this.maxPages+ ". Discarded pages = "+discardedPages);
				 }
			}
		}
	}
	
	protected boolean tryToAddPage(){
		if (this.pages < this.maxPages){
			this.pages++;
			return true;
		}
		return false;
	}	

	protected long  evictUnusedPages() throws IOException {
		//fLogger.logp(Level.SEVERE, PageManager.class.getName(), "evictUnusedPages", "evicting unused pages");
		ArrayList<DBFile> files = this.getFiles();
		int discardedPages = 0;
		int numberOfFilesDiscarded = 0;
		int numberOfFiles = files.size();
		while (numberOfFilesDiscarded < numberOfFiles && discardedPages < this.maxPagesToDiscard){
			this.indexOfLastFileDiscarded = (this.indexOfLastFileDiscarded+1) % numberOfFiles;
			DBFile file = files.get(this.indexOfLastFileDiscarded);
			discardedPages =+ file.evictUnusedPages();
			numberOfFilesDiscarded++;
		}
		fLogger.logp(Level.SEVERE, PageManager.class.getName(), "evictUnusedPages", discardedPages+" evicted");
		return discardedPages;
	}

	protected ArrayList<DBFile> getFiles() {
		ArrayList<DBFile> files = new ArrayList<DBFile>();
		Collection<StoredRelation> storedRelations =this.getDatabase().getDataDictionary().getStoredRelations().values() ; 
		for (StoredRelation storedRelation :  storedRelations){
			files.add(storedRelation.getDBFile());
		}
		return files;
	}

	protected Database getDatabase() {
		return this.database;
	}

	protected void setDatabase(Database database) {
		this.database = database;
	}

	/**
	 * @throws IOException if there was a problem evicting unused pages. 
	 * 
	 */
	public void shutDown() throws IOException {
		this.evictUnusedPages();
		
	}

	/**
	 * @return
	 */
	public static int getDefaultPageSize() {
		return 1024;
	}

}
