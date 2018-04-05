/**
 * 
 */
package simpledb;

/**
 * @author Facundo Quiroga
 * Creation date: Oct 29, 2008 4:46:53 PM
 */
public class DirtyMark {
    protected boolean dirty;
    protected TransactionId transactionID;
    /*
     * Creates a new non-dirty DirtyMark
     */
    public DirtyMark(){	
	this.dirty=false;
	this.transactionID = null;
    }
    public boolean isDirty() {
        return this.dirty;
    }
    protected void setDirty(boolean dirty) {
        this.dirty = dirty;
    }
    public TransactionId getTransactionID() {
        return this.transactionID;
    }
    protected void setTransactionID(TransactionId transactionID) {
        this.transactionID = transactionID;
    }
    public void markAsDirty(boolean dirty, TransactionId transactionId){
	this.setDirty(dirty);
	if (dirty){
	    this.setTransactionID(transactionId);
	}else{
	    this.setTransactionID(null);
	}
	
    }


}
