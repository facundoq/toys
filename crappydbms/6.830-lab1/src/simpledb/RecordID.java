package simpledb;

/**
 * A RecordID is a reference to a specific tuple on a specific page of a
 * specific table.
 */
public class RecordID {
    PageId pageID;
    int tupleNumber;
    /** Constructor.
     * @param pid the pageid of the page on which the tuple resides
     * @param tupleno the tuple number within the page.
     */
    public RecordID(PageId pageID, int tupleNumber) {
	this.pageID = pageID;
	this.tupleNumber = tupleNumber;
    }

    /**
     * @return the tuple number this RecordId references.
     */
    public int tupleno() {
        return this.tupleNumber;
    }

    /**
     * @return the table id this RecordId references.
     */
    public PageId pageid() {
       return this.pageID;
    }
}
