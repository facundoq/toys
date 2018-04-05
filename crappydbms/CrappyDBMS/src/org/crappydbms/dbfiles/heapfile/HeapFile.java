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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.crappydbms.datadictionary.DataDictionary;
import org.crappydbms.dbfiles.DBFile;
import org.crappydbms.dbfiles.DBFilePageCache;
import org.crappydbms.dbfiles.FilePage;
import org.crappydbms.dbfiles.FilePageID;
import org.crappydbms.dbfiles.FilePageIterator;
import org.crappydbms.dbfiles.locking.DBFileLockManager;
import org.crappydbms.exceptions.CrappyDBMSError;
import org.crappydbms.logging.CrappyDBMSLogger;
import org.crappydbms.main.Database;
import org.crappydbms.main.PageBufferFullException;
import org.crappydbms.main.PageManager;
import org.crappydbms.relations.StoredRelationIterator;
import org.crappydbms.relations.schemas.StoredRelationSchema;
import org.crappydbms.relations.tuples.StoredTuple;
import org.crappydbms.relations.tuples.Tuple;
import org.crappydbms.transactions.LockMode;
import org.crappydbms.transactions.Transaction;
import org.crappydbms.transactions.TransactionAbortedException;

/**
 * A file of unordered, not indexed pages which have tuples. Does not actually
 * have tuples or pages, but has a DBFilePageCache of them.
 * 
 * @author Facundo Manuel Quiroga Nov 7, 2008
 */
public class HeapFile implements DBFile {
	private static final Logger fLogger = CrappyDBMSLogger.getLogger(HeapFile.class.getPackage().getName());

	protected File file;
	protected StoredRelationSchema relationSchema;

	protected DBFileLockManager DBFileLockManager;
	protected DBFilePageCache DBFilePageCache;
	/* the data dictionary this HeapFile belongs to */
	protected DataDictionary dataDictionary;
	protected ReentrantLock lock;
	protected ReentrantLock numberOfPagesLock;

	protected volatile long numberOfPages;

	/**
	 * @param file
	 * @param dataDictionary
	 * @throws IOException
	 */
	public HeapFile(File file, StoredRelationSchema relationSchema, DataDictionary dataDictionary) throws IOException {
		this.setFile(file);
		if (!file.exists()) {
			file.createNewFile();
		}
		this.setRelationSchema(relationSchema);
		this.setDBFileLockManager(new DBFileLockManager());
		this.setDBFilePageCache(new DBFilePageCache(this));
		this.setDataDictionary(dataDictionary);
		this.setLock(new ReentrantLock());
		this.setNumberOfPagesLock(new ReentrantLock());
		this.setNumberOfPages(this.calculateNumberOfPages());
	}

	public File getFile() {
		return this.file;
	}

	protected void setFile(File file) {
		this.file = file;
	}

	public StoredRelationSchema getRelationSchema() {
		return this.relationSchema;
	}

	protected void setRelationSchema(StoredRelationSchema relationSchema) {
		this.relationSchema = relationSchema;
	}

	public StoredRelationIterator<Tuple> iterator(Transaction transaction, LockMode lockMode) throws TransactionAbortedException {
		return new HeapFileIterator(this, transaction, lockMode);
	}

	/**
	 * @return an iterator over ALL of the heap files of this file
	 */
	public FilePageIterator<FilePage> getHeapFilePagesIterator(Transaction transaction, LockMode lockMode) {
		return new HeapFilePageIterator(this, transaction, lockMode);
	}

	public long size() {
		return this.getFile().length();
	}

	@Override
	public FilePage readPage(long pageNumber) throws IOException {
		try {
			//fLogger.logp(Level.FINEST, HeapFile.class.getName(), "read", "reading page " + pageNumber);
			int pageSize = this.getPageSize();
			RandomAccessFile file = new RandomAccessFile(this.getFile(), "r");
			file.seek(pageNumber * pageSize);
			byte[] bytes = new byte[pageSize];
			file.readFully(bytes);
			file.close();
			return new HeapFilePage(bytes, new HeapFilePageID(this, pageNumber));
		} catch (FileNotFoundException e) {
			String message = "File should ALWAYS exist, when HeapFile is instantiated it gets created if it did not exist; filename "
					+ this.getFile().getName();
			fLogger.logp(Level.SEVERE, HeapFile.class.getName(), "read", message);
			throw new CrappyDBMSError(message);
		}
	}

	@Override
	/*
	 * Page numbers go from 0 to numberOfPages -1.
	 */
	public FilePage getPage(long pageNumber) throws IOException, PageBufferFullException {
		// We always need to lock here since RandomAccessFile is not thread-safe for writing or reading.
		this.getLock().lock();
		try {

			//fLogger.logp(Level.FINEST, HeapFile.class.getName(), "getPage", "Getting page" + pageNumber);
			if (pageNumber >= this.getNumberOfPages()) {
				String message = "Invalid page number " + pageNumber + " for file " + this.getFile().getName();
				fLogger.logp(Level.SEVERE, HeapFile.class.getName(), "getPage", message);
				throw new InvalidPageNumberError(message);
			}
			HeapFilePageID pageID = new HeapFilePageID(this, pageNumber);
			return this.getDBFilePageCache().getPage(pageID);
		} finally {
			this.getLock().unlock();
		}
	}

	/**
	 * Returns the number of pages in this HeapFile
	 */
	protected long calculateNumberOfPages() {
		// this division should always return an integer result
			return this.getFile().length() / this.getPageSize();
	}

	public int getPageSize() {
		return PageManager.getDefaultPageSize();
	}

	@Override
	public void addTuple(StoredTuple tuple, Transaction transaction) throws IOException, TransactionAbortedException, PageBufferFullException {
		//fLogger.logp(Level.FINEST, HeapFile.class.getName(), "addTuple", "adding new tuple: " + tuple);
		FilePage selectedPage;

		try {
			selectedPage = searchForNotFullPage(transaction);
			selectedPage.addTuple(tuple);
		} catch (NoEmptyPagesException e) {
			//fLogger.logp(Level.FINER, HeapFile.class.getName(), "addTuple", "no empty pages");
			transaction.addTuple(tuple,this);
		}

	}

	protected FilePage searchForNotFullPage(Transaction transaction) throws NoEmptyPagesException, IOException, TransactionAbortedException,
			PageBufferFullException {
		// Pages that are locked in exclusive mode but not used to add a tuple
		// should have their locks released
		// BUT iff they hadn't an exclusive or shared lock previously; in those
		// cases, we either should not release or should downgrade, respectively.
		// In addition, the HeapFilePageIterator also iterates over the private uncommitted pages of the transaction
		// something we would like to distinguish here
		// This is why HeapFilePageIterator functionality was re-implemented here
		// with the added constraints

		long pages = transaction.getNumberOfPagesOfDBFile(this);
		long currentPage = 0;
		
		while (currentPage < pages) {
			FilePageID filePageID = new HeapFilePageID(this, currentPage);
			// remember the previous locking state for the page
			LockMode previousLockMode = null;
			boolean hasExclusiveLock = false;
			if (transaction.hasExclusiveLockOn(filePageID)) {
				previousLockMode = LockMode.exclusive();
				hasExclusiveLock = true;
			} else if (transaction.hasSharedLockOn(filePageID)) {
				previousLockMode = LockMode.shared();
				hasExclusiveLock = transaction.doTryUpgradeLock(filePageID);
			}else{
				hasExclusiveLock = transaction.doTryLockExclusive(filePageID);
			}
			// if we could get an exclusive lock, check if the page is full; if so, exit method by returning the page
			if (hasExclusiveLock){
				FilePage page = this.getPage(currentPage);
				if (!page.isFull()) {
					return page;
				}
				// if the page was full, restore the previous locking state
				if (previousLockMode == null){
					transaction.releaseExclusiveLock(filePageID);
				}else if (previousLockMode == LockMode.shared()){
					transaction.downgradeToShared(filePageID);
				}
			}
			currentPage++;
		}
		throw new NoEmptyPagesException();
	}


	@Override
	public void removeSelf() {
		fLogger.logp(Level.FINE, HeapFile.class.getName(), "cleanUp", "Cleaning up file" + this.getFileName());
		this.getFile().delete();
	}

	public String toString() {
		return "HeapFile for StoredRelation: " + this.getFileName();
	}

	/**
	 * Write the page to disk. Should ONLY be called from the transaction when
	 * committing, or from the DBFilePageBuffer when evicting pages.
	 * Does not mark the page as clean; this is a responsibility of the client.
	 */
	@Override
	public void writePage(FilePageID selectedPageID, FilePage selectedPage) throws IOException {
		this.getLock().lock();
		try {
			//fLogger.logp(Level.FINER, HeapFile.class.getName(), "write", "writing to disk " + selectedPageID);
			int pageSize = this.getPageSize();
			long pageNumber = selectedPageID.getPageNumber();

			RandomAccessFile file = null;
			try {

				file = this.getRandomAccessFileForWriting();
				file.seek(pageNumber * pageSize);
				byte[] bytes = selectedPage.serialize();
				file.write(bytes);
			} catch (IOException e) {
				fLogger.logp(Level.SEVERE, HeapFile.class.getName(), "write", "IOException, reason:" + e.getMessage());
				throw e;
			} finally {
				if (file != null) {
					file.close();
				}
			}
		} finally {
			this.getLock().unlock();
		}
	}

	protected RandomAccessFile getRandomAccessFileForWriting() throws FileNotFoundException {
		return new RandomAccessFile(this.getFile(), "rws");
	}

	protected String getFileName() {
		return this.getFile().getName();
	}

	@Override
	public int evictUnusedPages() throws IOException {
		return this.getDBFilePageCache().evictUnusedPages();
	}

	@Override
	public void flush(FilePageID filePageID) throws IOException, PageBufferFullException {
		this.getLock().lock();
		try{
			this.getDBFilePageCache().flushPage(filePageID);
		}finally{
			this.getLock().unlock();
		}
	}
	
	@Override
	public void discardPage(FilePageID filePageID) {
		this.getDBFilePageCache().discardPage(filePageID);
	}

	@Override
	public void addNewPages(ArrayList<FilePage> filePages) throws IOException {
		long pagesAdded = 0;
		this.getLock().lock();
		try {
			long newPageNumber = this.getNumberOfPages();

			for (FilePage filePage : filePages) {
				HeapFilePage heapFilePage = (HeapFilePage) filePage;
				heapFilePage.setHeapFilePageID(new HeapFilePageID(this, newPageNumber));
				this.writePage(heapFilePage.getFilePageID(), heapFilePage);
				heapFilePage.markAsClean();
				newPageNumber++;
				pagesAdded++;
			}
			// There is no need to lock the pages since this transaction is committing (it won't modify any pages after this) 
			// and the other transactions will not see the pages until they are added because they store the number of pages they first saw in the file in order to avoid phantom reads.
			// add pages to buffer first
			this.getDBFilePageCache().addPages(filePages);
			// THEN add number of pages to file, so that no other transaction can see this pages until they are in the buffer
			this.addNumberOfPages(pagesAdded);
		} finally {
			this.getLock().unlock();
		}
	}
	
	protected void addNumberOfPages(long addedNumberOfPages) {
		this.getNumberOfPagesLock().lock();
		try {
			this.numberOfPages += addedNumberOfPages;
		} finally {
			this.getNumberOfPagesLock().unlock();
		}
	}


	public DBFileLockManager getDBFileLockManager() {
		return this.DBFileLockManager;
	}

	protected void setDBFileLockManager(DBFileLockManager fileLockManager) {
		this.DBFileLockManager = fileLockManager;
	}

	public DBFilePageCache getDBFilePageCache() {
		return this.DBFilePageCache;
	}

	protected void setDBFilePageCache(DBFilePageCache filePageCache) {
		this.DBFilePageCache = filePageCache;
	}

	protected DataDictionary getDataDictionary() {
		return this.dataDictionary;
	}

	protected void setDataDictionary(DataDictionary dataDictionary) {
		this.dataDictionary = dataDictionary;
	}

	public Database getDatabase() {
		return this.getDataDictionary().getDatabase();
	}

	protected ReentrantLock getLock() {
		return this.lock;
	}

	protected void setLock(ReentrantLock lock) {
		this.lock = lock;
	}

	protected ReentrantLock getNumberOfPagesLock() {
		return this.numberOfPagesLock;
	}

	protected void setNumberOfPagesLock(ReentrantLock numberOfPagesLock) {
		this.numberOfPagesLock = numberOfPagesLock;
	}

	public long getNumberOfPages() {
		return this.numberOfPages;
	}

	// DO NOT call this unless it is from the constructor
	protected void setNumberOfPages(long numberOfPages) {
		this.numberOfPages = numberOfPages;
	}

	

}
