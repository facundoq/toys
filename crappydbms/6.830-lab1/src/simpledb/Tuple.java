package simpledb;

/**
 * Tuple maintains information about the contents of a tuple.
 * Tuples have a specified schema specified by a TupleDesc object and contain
 * Field objects with the data for each field.
 */
public class Tuple {
	TupleDesc tupleDesc;
	RecordID recordID;
	Field[] fields;
	
    /**
     * Create a new tuple with the specified schema (type).
     *
     * @param td the schema of this tuple. It must be a valid TupleDesc
     * instance with at least one field.
     */
	
    public Tuple(TupleDesc td) {
        this.tupleDesc = td;
        this.fields = td.createFields();
    }

    /**
     * @return The TupleDesc representing the schema of this tuple.
     */
    public TupleDesc getTupleDesc() {
        return tupleDesc;
    }

    /**
     * @return The RecordID representing the location of this tuple on
     *   disk. May be null.
     */
    public RecordID getRecordID() {
        return this.recordID;
    }

    /**
     * Set the RecordID information for this tuple.
     * @param rid the new RecordID for this tuple.
     */
    public void setRecordID(RecordID rid) {
    	this.recordID = rid;
    }

    /**
     * Change the value of the ith field of this tuple.
     *
     * @param i index of the field to change. It must be a valid index.
     * @param f new value for the field.
     */
    public void setField(int i, Field f) {
    	this.fields[i] = f;
    }

    /**
     * @return the value of the ith field, or null if it has not been set.
     *
     * @param i field index to return. Must be a valid index.
     */
    public Field getField(int i) {        
        return this.fields[i];
    }

    /**
     * Returns the contents of this Tuple as a string.
     * Note that to pass the system tests, the format needs to be as
     * follows:
     *
     * column1\tcolumn2\tcolumn3\t...\tcolumnN\n
     *
     * where \t is any whitespace, except newline, and \n is a newline
     */
    public String toString() {
    	String tuple=new String();
    	for ( Field field : this.fields ){
    		tuple = tuple + field.toString() + " ";    		
    	}
    	// remove the last " "
    	tuple = tuple.substring( 0 , tuple.length()-2 );
    	tuple = tuple +"\n";
    	return tuple;        
    }
}
