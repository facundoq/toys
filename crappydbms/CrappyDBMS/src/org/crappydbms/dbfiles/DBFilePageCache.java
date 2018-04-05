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
package org.crappydbms.dbfiles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import org.crappydbms.dbfiles.locking.DBFileLockManager;
import org.crappydbms.dbfiles.locking.PageLock;
import org.crappydbms.exceptions.CrappyDBMSError;
import org.crappydbms.main.Database;
import org.crappydbms.main.PageBufferFullException;
import org.crappydbms.main.PageManager;

/**
 * @author Facundo Manuel Quiroga Jan 5, 2009
 */
public class DBFilePageCache {

	protected Hashtable<FilePageID, FilePage> pages;
	protected Object lock = new Object();
	protected DBFile DBFile;

	public DBFilePageCache(DBFile DBFile) {
		super();
		this.setDBFile(DBFile);
		this.setPages(new Hashtable<FilePageID, FilePage>(300, (float) 0.40));
	}

	
	/**
	 * Checks if the page is in the buffer; if not, tells the DBFile to read the page from disk
	 * The caller must ensure it has a shared or exclusive lock on the page
	 * @throws PageBufferFullException if there is not enough space in memory and there are no pages to evict
	 */
	public FilePage getPage(FilePageID pageID) throws IOException, PageBufferFullException {
		synchronized (this.lock) {
			FilePage dbFilePage = this.getPages().get(pageID);
			if (dbFilePage == null) {
				this.getPageManager().addPage();
				dbFilePage = this.readDBFilePage(pageID);
				this.getPages().put(pageID, dbFilePage);
			}
			return dbFilePage;
		}
	}
	/**
	 * Tells the DBFile to read the page from disk
	 * @param pageID of the FilePage
	 * @return the FilePage read
	 * @throws IOException if the page could not be read from disk
	 */
	protected FilePage readDBFilePage(FilePageID pageID) throws IOException {
		DBFile file = this.getDBFile();
		return file.readPage(pageID.getPageNumber());
	}



	/**
	 * Add the pages to the buffer, supposing they were already read from disk or created as new
	 * The caller must ensure it has an exclusive lock on the pages
	 * @param filePages
	 */
	public void addPages(ArrayList<FilePage> filePages) {
		Hashtable<FilePageID, FilePage> pages = this.getPages();
		synchronized (this.lock) {
			for (FilePage filePage : filePages) {
				FilePageID filePageID = filePage.getFilePageID();
				FilePage previous = pages.put(filePageID,filePage);
				if (previous != null){
					throw new CrappyDBMSError("There should not be a filePage with id "+filePageID+" in the buffer");
				}
				
			}
		}
	}
/**
 * Writes the page with filePageID to disk if it is dirty. If so, it is marked as clean again.
 * The caller must ensure it has an exclusive lock on the page
 * @param filePageID of the page to be flushed
 * @throws IOException if the DBFile could not write the page to disk
 */
	public void flushPage(FilePageID filePageID) throws IOException {
		synchronized (this.lock) {
			//System.out.println("Flushing page "+ filePageID);
			FilePage page = pages.get(filePageID);
			if (page == null) {
				throw new CrappyDBMSError("Can't flush a page that is not in this buffer, filePageID " + filePageID);
			}
			if (page.isDirty()) {
				filePageID.getFile().writePage(filePageID, page);
				page.markAsClean();
			}
			//System.out.println("Flushed page "+ filePageID);
		}
	}
	/**
	 * Discard the page with filePageID from this cache.
	 * The caller must ensure it has an exclusive lock on the page
	 * @param filePageID of the page t be discarded
	 */
	public void discardPage(FilePageID filePageID) {
		synchronized (this.lock) {
			FilePage filePage = pages.get(filePageID);
			if (filePage == null) {
				//TODO just log, don't raise exception since it's possible the transaction is aborting due to error and has mixed up its pages
				throw new CrappyDBMSError("Trying to discard a page that was not in the buffer, filepageid " + filePageID);
			}
			if (filePage.isDirty()) {
				this.getPages().remove(filePageID);
			}
		}
	}



	/**
	 * @return number of pages evicted
	 * @throws IOException if a page could not be flushed
	 */
	public int evictUnusedPages() throws IOException {
		DBFileLockManager DBFileLockManager = this.getDBFile().getDBFileLockManager();
		int evictedPages = 0;
		synchronized (this.lock) {
			Iterator<FilePageID> iterator = this.getPages().keySet().iterator();
			while (iterator.hasNext()) {
				FilePageID filePageID = iterator.next();
				PageLock pageLock = DBFileLockManager.getPageLockFor(filePageID);
				if (pageLock.tryExclusiveLock()) {
					FilePage filePage = this.getPages().get(filePageID);
					if (filePage.isDirty()) {
						throw new CrappyDBMSError("Pages are always written to disk on commit; this should never happen ");
						//this.flushPage(filePage);
					}
					evictedPages++;
					iterator.remove();
					pageLock.releaseExclusiveLock();
				}
			}
			return evictedPages;
		}
	}

	protected DBFile getDBFile() {
		return this.DBFile;
	}

	protected void setDBFile(DBFile file) {
		this.DBFile = file;
	}

	protected Database getDatabase() {
		return this.getDBFile().getDatabase();
	}

	protected PageManager getPageManager() {
		return this.getDatabase().getPageManager();
	}
	
	public Hashtable<FilePageID, FilePage> getPages() {
		return this.pages;
	}

	public void setPages(Hashtable<FilePageID, FilePage> pages) {
		this.pages = pages;
	}
	
	/*
	private void flushPages(Collection<FilePageID> pageIDs) throws IOException {
		synchronized (this.lock) {
			Hashtable<FilePageID, FilePage> pages = this.getPages();
			for (FilePageID filePageID : pageIDs) {
				// only flush pages if they are really dirty
				FilePage page = pages.get(filePageID);
				if (page == null) {
					throw new CrappyDBMSError("Can't flush a page that is not in this buffer, filePageID " + filePageID);
				}
				if (page.isDirty()) {
					filePageID.getFile().writePage(filePageID, page);
					page.markAsClean();
				}
			}
		}
	}*/
	
}
