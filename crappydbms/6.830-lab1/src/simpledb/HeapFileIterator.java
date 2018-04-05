/**
 * 
 */
package simpledb;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author Facundo Quiroga
 * Creation date: Oct 30, 2008 6:27:35 PM
 */
public class HeapFileIterator implements DbFileIterator {
    private HeapFile heapFile;
    private HeapPageId currentHeapPage;
    private Iterator<Tuple> currentPageTupleIterator;
    
    public HeapFileIterator(HeapFile heapFile) {
	super();
	this.setHeapFile(heapFile);	
    }

    /* (non-Javadoc)
     * @see simpledb.DbFileIterator#close()
     */
    public void close() {
	// TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see simpledb.DbFileIterator#hasNext()
     */
    public boolean hasNext() throws DbException, TransactionAbortedException {
	boolean moreTuplesInThisPage = this.getCurrentPageTupleIterator().hasNext();
	boolean morePages = (this.currentHeapPage.pageno()< (this.getHeapFile().numPages()-1) );
	return ( moreTuplesInThisPage || morePages );
    }

    /* (non-Javadoc)
     * @see simpledb.DbFileIterator#next()
     */
    public Tuple next() throws DbException, TransactionAbortedException, NoSuchElementException {
	if (!this.getCurrentPageTupleIterator().hasNext()){
	 HeapPageId currentID = this.getCurrentHeapPage();
	 HeapPageId newID = new HeapPageId(currentID.tableid(),currentID.pageno());
	 this.setCurrentHeapPage(newID);
	 this.setCurrentPageTupleIterator(this.getPage(newID).iterator());
	}
	return this.getCurrentPageTupleIterator().next();
    }

    /* (non-Javadoc)
     * @see simpledb.DbFileIterator#open()
     */
    public void open() throws DbException, TransactionAbortedException {
	this.setCurrentHeapPage(this.getHeapPageIdFor(0));

    }
    public HeapPage getPage(HeapPageId heapPageID) throws TransactionAbortedException, DbException{
	return (HeapPage) Database.getBufferPool().getPage(null, heapPageID, Permissions.READ_ONLY);
    }
    public HeapPageId getHeapPageIdFor(int page){
	Table table = Database.getCatalog().tableForFile(this.getHeapFile());
	int tableid = Database.getCatalog().getTableId(table.getName());
	return new HeapPageId(tableid,page);
    }

    /* (non-Javadoc)
     * @see simpledb.DbFileIterator#rewind()
     */
    public void rewind() throws DbException, TransactionAbortedException {
	this.setCurrentHeapPage(this.getHeapPageIdFor(0));
	
    }

    public HeapFile getHeapFile() {
        return this.heapFile;
    }

    public void setHeapFile(HeapFile heapFile) {
        this.heapFile = heapFile;
    }

    public HeapPageId getCurrentHeapPage() {
        return this.currentHeapPage;
    }

    public void setCurrentHeapPage(HeapPageId currentHeapPage) {
        this.currentHeapPage = currentHeapPage;
    }

    public Iterator<Tuple> getCurrentPageTupleIterator() {
        return this.currentPageTupleIterator;
    }

    public void setCurrentPageTupleIterator(Iterator<Tuple> currentPageTupleIterator) {
        this.currentPageTupleIterator = currentPageTupleIterator;
    }

}
