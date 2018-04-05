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

import java.util.Iterator;

import org.crappydbms.exceptions.InvalidAttributeNameException;
import org.crappydbms.queries.operations.Modification;
import org.crappydbms.relations.tuples.InvalidAttributeTypeException;
import org.crappydbms.relations.tuples.StoredTuple;


/**
 * @author Facundo Manuel Quiroga
 * Nov 9, 2008
 * 
 */
public interface FilePage {


    public Iterator<StoredTuple> iterator();
    public boolean hasTuple(StoredTuple tuple);
    public void removeTuple(StoredTuple tuple);
    void modifyTuple(StoredTuple tuple, Modification modification) throws InvalidAttributeNameException, InvalidAttributeTypeException;
    public void addTuple(StoredTuple tuple);

    public byte getNumberOfTuples();
    public boolean isFull();
    public boolean isEmpty();

    public FilePageID getFilePageID();

    public String toString();
    public byte[] serialize();

		public void markAsDirty();
		public void markAsClean();
		public boolean isDirty();

}
