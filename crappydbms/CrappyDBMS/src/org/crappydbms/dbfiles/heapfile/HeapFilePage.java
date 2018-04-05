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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.crappydbms.dbfiles.FilePage;
import org.crappydbms.exceptions.CrappyDBMSError;
import org.crappydbms.exceptions.InvalidAttributeNameException;
import org.crappydbms.main.PageManager;
import org.crappydbms.queries.operations.Modification;
import org.crappydbms.relations.fields.Field;
import org.crappydbms.relations.schemas.StoredRelationSchema;
import org.crappydbms.relations.schemas.attributes.Attribute;
import org.crappydbms.relations.tuples.InvalidAttributeTypeException;
import org.crappydbms.relations.tuples.StoredTuple;
import org.crappydbms.relations.tuples.Tuple;

/**
 * @author Facundo Manuel Quiroga Nov 9, 2008 Page structure: 1st byte = number
 *         of occupied tuple slots usedSize = bytes that can be used to hold
 *         tuples (but maybe they are not currently holding any tuple)
 *         freeEndingBytes = bytes that cannot be used for any purpose
 */
public class HeapFilePage implements FilePage, Iterable<StoredTuple> {

	//private static final Logger fLogger = CrappyDBMSLogger.getLogger(HeapFilePage.class.getPackage().getName());

	protected HeapFilePageID heapFilePageID;

	boolean dirty;

	protected ArrayList<StoredTuple> tuples;
	
	protected int tupleSlots;
	
	protected void initializeHeapPage(HeapFilePageID heapFilePageID){
		this.setHeapFilePageID(heapFilePageID);
		this.markAsClean();
		this.setTupleSlots(this.getNumberOfTupleSlots());
	}
	/**
	 * Create a file processing the tuples from bytes byte array
	 * @param bytes
	 * @param heapFilePageID
	 */
	public HeapFilePage(byte[] bytes, HeapFilePageID heapFilePageID) {
		this.setHeapFilePageID(heapFilePageID);
		this.setTuples(this.convertToTuples(bytes));
		this.markAsClean();
		this.setTupleSlots(this.getNumberOfTupleSlots());
		//fLogger.logp(Level.FINE, HeapFilePage.class.getName(), "HeapFilePage", "instantiated page "+heapFilePageID+" with "+this.getNumberOfTuples()+" tuples");
	}
	/**
	 * create a truly NEW, empty, file page. 
	 * @param heapFilePageID
	 */
	public HeapFilePage(HeapFilePageID heapFilePageID) {
		this.setHeapFilePageID(heapFilePageID);
		this.setTuples( new ArrayList<StoredTuple>());
		this.markAsClean();
		this.setTupleSlots(this.getNumberOfTupleSlots());
		//fLogger.logp(Level.FINE, HeapFilePage.class.getName(), "HeapFilePage", "instantiated page "+heapFilePageID+" with "+this.getNumberOfTuples()+" tuples");
	}

	protected StoredRelationSchema getRelationSchema() {
		return this.getFilePageID().getFile().getRelationSchema();
	}

	protected int getTupleSize() {
		return this.getRelationSchema().getTupleSize();
	}

	protected int getPageSize() {
		return PageManager.getDefaultPageSize();
	}

	protected int getNumberOfTupleSlots() {
		double usablePageSize = this.usablePageSize();
		double tupleSize = this.getTupleSize();
		return (int) Math.floor(usablePageSize/tupleSize);
	}
	/**
	 * @return the amount of bytes that could be used, for all pages that have a size of getPageSize
	 * For now, its just the size of the page minus 1 byte to store the number of actually used tuple slots in the page   
	 */
	protected int usablePageSize(){
		return this.getPageSize() - 1;
	}

	/**
	 * @return the effectively size of the page that can be used, excluding the
	 *         first byte This is amount of bytes that can be used for tuple
	 *         storing, not the number of bytes that are CURRENTLY storing a tuple
	 *         So all bytes from (getPageSize - (getUsedSize+1)) to getPageSize are bytes
	 *         that are wasted in every page with this schema.
	 */
	protected int getUsedSize() {
		return this.getTupleSlots() * this.getTupleSize();
	}
	/**
	 * Takes a byte array with the tuples and returns an ArrayList of those tuples decoded as objects
	 * @param bytes with the encoded tuples
	 * @return
	 */
	protected ArrayList<StoredTuple> convertToTuples(byte[] bytes)  {
		byte numberOfTuples = bytes[0];
		ArrayList<StoredTuple> tuples = new ArrayList<StoredTuple>();
		int currentByte = 1;
		for (int tupleNumber = 0; tupleNumber < numberOfTuples; tupleNumber++) {
			byte[] tupleBytes = new byte[this.getTupleSize()];
			for (int i = 0; i<this.getTupleSize();i++){
				tupleBytes[i] = bytes[currentByte];
				currentByte++;
			}
			tuples.add(tupleNumber, this.parseTuple(tupleBytes, this.getRelationSchema()));
		}
		return tuples;
	}


	protected StoredTuple parseTuple(byte[] tupleBytes, StoredRelationSchema relationSchema)  {
		ArrayList<Field> fields = new ArrayList<Field>();
		ByteArrayInputStream byteInput = new ByteArrayInputStream(tupleBytes);
		DataInputStream input = new DataInputStream(byteInput);
		for (Attribute attribute : relationSchema.getAttributes()) {
			Field field;
			try {
				field = attribute.getType().read(input);
			} catch (IOException e) {
				throw new CrappyDBMSError("Should never throw IO error reading from an in-memory byte array");
			}
			fields.add(field);
		}

		StoredTuple tuple = new StoredTuple(fields, relationSchema);
		return tuple;
	}

	/**
	 * 
	 * @return a byte array containing the number of tuples and tuples themselves
	 *         of this page. The length of the byte array equals the size of a serialized page 
	 */
	public byte[] serialize()  {
		byte[] bytes = new byte[this.getPageSize()];
		// serialize the number of tuples
		ByteArrayOutputStream pageBAOS = new ByteArrayOutputStream(this.getPageSize());
		pageBAOS.write(  this.getNumberOfTuples());
		// serialize all the tuples
		DataOutputStream pageStream = new DataOutputStream(pageBAOS);
		for (StoredTuple tuple : this) {
			try {
				tuple.serializeTo(pageStream);
			} catch (IOException e) {
				throw new CrappyDBMSError("Error serializing tuple in page "+this.getFilePageID());
			}
		}
		// get the used bytes and pass them to a byte array of the size of a page 
		byte[] usedBytes = pageBAOS.toByteArray();
		for (int i = 0; i<usedBytes.length;i++){
			bytes[i] = usedBytes[i];
		}
		return bytes;
	}

	public Iterator<StoredTuple> iterator() {
		return this.getTuples().iterator();
	}

	public byte getNumberOfTuples() {
		return (byte) this.getTuples().size();
	}

	@Override
	public boolean isFull() {
		return this.getNumberOfTuples() == this.getTupleSlots();
	}

	public boolean isEmpty() {
		return this.getTuples().isEmpty();
	}

	@Override
	public boolean hasTuple(StoredTuple tuple) {
		return this.getTuples().contains(tuple);
	}
	@Override
	public void addTuple(StoredTuple tuple) {
		if (this.isFull()) {
			throw new CrappyDBMSError("cant add a tuple to a full page");
		}
		this.getTuples().add(tuple);
		this.markAsDirty();
		//fLogger.logp(Level.FINER, HeapFilePage.class.getName(), "addTuple", "Added to page: "+ this.getFilePageID() + " tuple: "+tuple+". Remaining tupleSlots: "+(this.getNumberOfTupleSlots()-this.getNumberOfTuples()));
	}

	@Override
	public void removeTuple(StoredTuple tuple) {
		this.getTuples().remove(tuple);
		this.markAsDirty();
	}

	@Override
	public void modifyTuple(StoredTuple tuple, Modification modification) throws InvalidAttributeNameException, InvalidAttributeTypeException {
		if (!this.hasTuple(tuple)){
			throw new CrappyDBMSError("Modifying a tuple that is not in this PageFile");
		}
		modification.applyTo(tuple);
		this.markAsDirty();
	}

	public String toString() {
		String result = "HeapFilePage " + this.getFilePageID().getPageNumber() + ", tuples: \n";
		for (Tuple tuple : this.tuples) {
			result += tuple.toString() + "\n";
		}
		return result;
	}
	
	public void markAsDirty() {
		this.setDirty(true);
	}

	public void markAsClean() {
		this.setDirty(false);
	}
	
	public boolean isDirty() {
		return this.dirty;
	}

	protected void setDirty(boolean dirty) {
		this.dirty = dirty;
	}
	
	protected void setTuples(ArrayList<StoredTuple> tuples) {
		this.tuples = tuples;
	}

	protected ArrayList<StoredTuple> getTuples() {
		return this.tuples;
	}

	public HeapFilePageID getFilePageID() {
		return this.heapFilePageID;
	}

	public void setHeapFilePageID(HeapFilePageID heapFilePageID) {
		this.heapFilePageID = heapFilePageID;
	}
	protected int getTupleSlots() {
		return this.tupleSlots;
	}
	protected void setTupleSlots(int tupleSlots) {
		this.tupleSlots = tupleSlots;
	}	

}
