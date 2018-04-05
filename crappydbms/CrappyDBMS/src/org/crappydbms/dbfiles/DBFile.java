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

import org.crappydbms.dbfiles.locking.DBFileLockManager;
import org.crappydbms.main.Database;
import org.crappydbms.main.PageBufferFullException;
import org.crappydbms.relations.StoredRelationIterator;
import org.crappydbms.relations.schemas.StoredRelationSchema;
import org.crappydbms.relations.tuples.StoredTuple;
import org.crappydbms.relations.tuples.Tuple;
import org.crappydbms.transactions.LockMode;
import org.crappydbms.transactions.Transaction;
import org.crappydbms.transactions.TransactionAbortedException;


/**
 * @author Facundo Manuel Quiroga
 * Nov 7, 2008
 * 
 */
public interface DBFile {
	
    public long getNumberOfPages();
    public long size();
    public StoredRelationIterator<Tuple> iterator( Transaction transaction, LockMode lockMode) throws TransactionAbortedException;
    public StoredRelationSchema getRelationSchema();
    /**
     * Should ONLY be called from a class that implements PageBufferPool
     * @param pageNumber to read
     * @return the DBFilePage with page number = pageNumber
     * @throws IOException 
     */
    public FilePage readPage(long pageNumber) throws IOException;
    public FilePage getPage(long pageNumber) throws IOException, PageBufferFullException;
    public void addTuple(StoredTuple tuple, Transaction transaction) throws IOException, TransactionAbortedException, PageBufferFullException;
    public void removeSelf();
/**
 *  Write the page to disk. Should ONLY be called from the transaction when
	 * committing, or from the DBFilePageBuffer when evicting pages.
	 * Does not mark the page as clean; this is a responsibility of the client.
 */
    public void writePage(FilePageID selectedPageID, FilePage selectedPage) throws IOException;

    
		public int evictUnusedPages() throws IOException;
		public DBFilePageCache getDBFilePageCache();
		public DBFileLockManager getDBFileLockManager();
		public void flush(FilePageID filePageID) throws IOException, PageBufferFullException;
		
	public Database getDatabase();

	public void addNewPages(ArrayList<FilePage> filePages) throws IOException;
	/**
	 * @param filePageID
	 */
	public void discardPage(FilePageID filePageID);	
}
