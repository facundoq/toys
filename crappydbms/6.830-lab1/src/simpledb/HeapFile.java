package simpledb;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.*;

/**
 * HeapFile is an implementation of a DbFile that stores a collection
 * of tuples in no particular order.  Tuples are stored on pages, each of
 * which is a fixed size, and the file is simply a collection of those
 * pages. HeapFile works closely with HeapPage.  The format of HeapPages
 * is described in the HeapPage constructor.
 *
 * @see simpledb.HeapPage#HeapPage
 * @author Sam Madden
 */
public class HeapFile implements DbFile {
    File file;

    /**
     * Constructor.
     * Creates a new heap file that stores pages in the specified buffer pool.
     *
     * @param f The file that stores the on-disk backing store for this DbFile.
     */
    public HeapFile(File file) {
	this.file = file;
    }

    /**
     * Return a Java File corresponding to the data from this HeapFile on disk.
     */
    public File getFile() {
	return this.file;
    }

    /**
     * @return an ID uniquely identifying this HeapFile
     *  (Implementation note:  you will need to generate this tableid somewhere,
     *    ensure that each HeapFile has a "unique id," and that you always
     *    return the same value for a particular HeapFile.  The implementation we
     *    suggest you use could hash the absolute file name of the file underlying
     *    the heapfile, i.e. f.getAbsoluteFile().hashCode()
     *    )
     */
    public int id() {
	return this.file.getAbsolutePath().hashCode();
    }

    /**
     * Returns a Page from the file.
     * @throws DbException 
     */
    public Page readPage(PageId pid) throws NoSuchElementException {
	//long pageNumber = pid.pageno();
	byte[] data = new byte[this.bytesPerPage()];
	int offset = this.bytesPerPage() * pid.pageno();
	RandomAccessFile raf;

	try {
	    raf = new RandomAccessFile(this.file, "rws");
	    raf.read(data, offset, data.length);
	    HeapPage hp = new HeapPage(new HeapPageId(pid.tableid(), pid.pageno()), data);
	    return hp;

	    //FileChannel fc = raf.getChannel();
	} catch (FileNotFoundException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	throw new RuntimeException();
    }

    /**
     * Writes the given page to the appropriate location in the file.
     */
    public void writePage(Page page) throws IOException {
	// some code goes here
	// not necessary for lab1
    }

    /**
     * Returns the number of pages in this HeapFile.
     */
    public int numPages() {
	int pageSize = this.bytesPerPage();
	long pages = (long) Math.ceil(this.file.length() / pageSize);
	return (int) pages;
    }

    /**
     * Adds the specified tuple to the table under the specified TransactionId.
     *
     * @throws DbException
     * @throws IOException
     * @return An ArrayList contain the pages that were modified
     */
    public ArrayList<Page> addTuple(TransactionId tid, Tuple t) throws DbException, IOException, TransactionAbortedException {
	// some code goes here
	return null;
	// not necessary for lab1
    }

    /**
     * Deletes the specified tuple from the table, under the specified
     * TransactionId.
     */
    public Page deleteTuple(TransactionId tid, Tuple t) throws DbException, TransactionAbortedException {
	// some code goes here
	return null;
	// not necessary for lab1
    }

    /**
     * An iterator over all tuples on this file, over all pages.
     * Note that this iterator should use BufferPool.getPage(), rather than HeapFile.getPage()
     * to iterate through pages.
     */
    public DbFileIterator iterator(TransactionId tid) {
	// some code goes here
	return new HeapFileIterator(this);
    }

    /**
     * @return the number of bytes on a page, including the number of bytes
     * in the header.
     */
    public int bytesPerPage() {
	// some code goes here
	Table table = Database.getCatalog().tableForFile(this);
	int slots = HeapPage.slotsForTupleWith(table.getTupleDesc());
	int headerSize = HeapPage.headerSizeForTupleWith(slots);
	return BufferPool.PAGE_SIZE+headerSize;
    }
}
