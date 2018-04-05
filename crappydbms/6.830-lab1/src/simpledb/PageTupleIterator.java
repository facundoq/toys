/**
 * 
 */
package simpledb;

import java.util.Iterator;

/**
 * @author Facundo Quiroga
 * Creation date: Oct 29, 2008 5:33:26 PM
 */
public class PageTupleIterator implements Iterator<Tuple> {

    protected HeapPage heapPage;
    protected int currentTuple = 0;
    
    public PageTupleIterator(HeapPage heapPage) {
	this.heapPage = heapPage;
    }

    /* (non-Javadoc)
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext() {	
	return this.currentTuple < this.heapPage.numSlots();
    }

    /* (non-Javadoc)
     * @see java.util.Iterator#next()
     */
    public Tuple next() {
	Tuple tuple = this.heapPage.getTuple(this.currentTuple);
	this.currentTuple++;
	while ( ( this.currentTuple < this.heapPage.numSlots() ) && !( this.heapPage.getSlot(this.currentTuple) ) ){
	    currentTuple++;
	}
	return tuple;
    }

    /* (non-Javadoc)
     * @see java.util.Iterator#remove()
     */
    public void remove() {
	throw new UnsupportedOperationException();

    }

}
