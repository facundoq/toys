package simpledb;

import java.util.*;

/**
 * TupleDesc describes the schema of a tuple.
 */
public class TupleDesc {
	protected Type[] typeAr;

	protected String[] fieldAr;

	/**
	 * Merge two TupleDescs into one, with td1.numFields + td2.numFields fields,
	 * with the first td1.numFields coming from td1 and the remaining from td2.
	 * 
	 * @param td1
	 *            The TupleDesc with the first fields of the new TupleDesc
	 * @param td2
	 *            The TupleDesc with the last fields of the TupleDesc
	 * @return the new TupleDesc
	 */
	public static TupleDesc combine(TupleDesc td1, TupleDesc td2) {

		int newTupleDescSize = td1.numFields() + td2.numFields();
		Type[] typeAr = new Type[newTupleDescSize];
		String[] fieldAr = new String[newTupleDescSize];
		int currentField = 0;
		for (int i = 0; i < td1.numFields(); i++) {
			typeAr[currentField] = td1.getType(i);
			fieldAr[currentField] = td1.getFieldName(i);
			currentField++;
		}
		for (int i = 0; i < td2.numFields(); i++) {
			typeAr[currentField] = td2.getType(i);
			fieldAr[currentField] = td2.getFieldName(i);
			currentField++;
		}
		return new TupleDesc(typeAr, fieldAr);
	}

	/**
	 * Constructor. Create a new tuple desc with typeAr.length fields with
	 * fields of the specified types, with associated named fields.
	 * 
	 * @param typeAr
	 *            array specifying the number of and types of fields in this
	 *            TupleDesc. It must contain at least one entry.
	 * @param fieldAr
	 *            array specifying the names of the fields. Note that names may
	 *            be null.
	 */
	public TupleDesc(Type[] typeAr, String[] fieldAr) {
		this.typeAr = new Type[typeAr.length];
		for (int i = 0; i < this.typeAr.length; i++) {
			this.typeAr[i] = typeAr[i];
		}

		this.fieldAr = new String[fieldAr.length];
		for (int i = 0; i < this.fieldAr.length; i++) {
			this.fieldAr[i] = fieldAr[i];
		}

	}

	/**
	 * Constructor. Create a new tuple desc with typeAr.length fields with
	 * fields of the specified types, with anonymous (unnamed) fields.
	 * 
	 * @param typeAr
	 *            array specifying the number of and types of fields in this
	 *            TupleDesc. It must contain at least one entry.
	 */
	public TupleDesc(Type[] typeAr) {
		this(typeAr, new String[typeAr.length]);
	}

	/**
	 * @return the number of fields in this TupleDesc
	 */
	public int numFields() {
		return this.typeAr.length;
	}

	/**
	 * Gets the (possibly null) field name of the ith field of this TupleDesc.
	 * 
	 * @param i
	 *            index of the field name to return. It must be a valid index.
	 * @return the name of the ith field
	 * @throws NoSuchElementException
	 *             if i is not a valid field reference.
	 */
	public String getFieldName(int i) throws NoSuchElementException {
		if (i < 0 || i >= this.numFields()) {
			throw new NoSuchElementException("Invalid field number " + i + ".");
		}
		return this.fieldAr[i];
	}

	/**
	 * Find the index of the field with a given name.
	 * 
	 * @param name
	 *            name of the field.
	 * @return the index of the field that is first to have the given name.
	 * @throws NoSuchElementException
	 *             if no field with a matching name is found.
	 */
	public int nameToId(String name) throws NoSuchElementException {
		if (name == null){
			throw new NoSuchElementException();
		}
		
		for (int i = 0; i < this.fieldAr.length; i++) {			
			if (name.equals(this.fieldAr[i])) {
				return i;
			}
		}
		throw new NoSuchElementException("No field with name " + name + ".");
	}

	/**
	 * Gets the type of the ith field of this TupleDesc.
	 * 
	 * @param i
	 *            The index of the field to get the type of. It must be a valid
	 *            index.
	 * @return the type of the ith field
	 * @throws NoSuchElementException
	 *             if i is not a valid field reference.
	 */
	public Type getType(int i) throws NoSuchElementException {
		if (i < 0 || i >= this.numFields()) {
			throw new NoSuchElementException("Invalid type number " + i + ".");
		}
		return this.typeAr[i];
	}

	/**
	 * @return The size (in bytes) of tuples corresponding to this TupleDesc.
	 *         Note that tuples from a given TupleDesc are of a fixed size.
	 */
	public int getSize() {
		int size = 0;
		for (Type type : this.typeAr) {
			size += type.getLen();
		}
		return size;
	}

	/**
	 * Compares the specified object with this TupleDesc for equality. Two
	 * TupleDescs are considered equal if they are the same size and if the n-th
	 * type in this TupleDesc is equal to the n-th type in td.
	 * 
	 * @param o
	 *            the Object to be compared for equality with this TupleDesc.
	 * @return true if the object is equal to this TupleDesc.
	 */
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		try {
			TupleDesc td = (TupleDesc) o;
			if (td.numFields() != this.numFields()) {
				return false;
			}
			for (int i = 0; i < this.numFields(); i++) {
				if (!this.getType(i).equals(td.getType(i))) {
					return false;
				}
			}
			return true;
		} catch (ClassCastException e) {
			return false;
		}
	}

	public int hashCode() {
		// If you want to use TupleDesc as keys for HashMap, implement this so
		// that equal objects have equals hashCode() results

		int hashCode = 0;
		for (Type type : this.typeAr) {
			hashCode += type.hashCode();
		}
		return hashCode;
	}

	/**
	 * Returns a String describing this descriptor. It should be of the form
	 * "fieldType[0](fieldName[0]), ..., fieldType[M](fieldName[M])", although
	 * the exact format does not matter.
	 * 
	 * @return String describing this descriptor.
	 */
	public String toString() {
		// some code goes here
		return "A Tuple Desc (not yet implemented";
	}
	
	public Field[] createFields(){
		Field[] fields = new Field[this.numFields()];
		for (int i=0;i<fields.length;i++){
			if (this.typeAr[i].typeId == Type.INT_ID){
				fields[i] = new IntField(0);
			}else if (this.typeAr[i].typeId == Type.STRING_ID){
				fields[i] = new StringField("",Type.STRING_LEN);
			}else {
				throw new RuntimeException("Invalid Type");
			}		 
		}		
		return fields;
	}
}
