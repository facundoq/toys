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
package org.crappydbms.queries.operations;

import java.util.ArrayList;

import org.crappydbms.exceptions.InvalidAttributeNameException;
import org.crappydbms.queries.predicates.Predicate;
import org.crappydbms.relations.StoredRelation;
import org.crappydbms.relations.StoredRelationIterator;
import org.crappydbms.relations.fields.IntegerField;
import org.crappydbms.relations.tuples.StoredTuple;
import org.crappydbms.relations.tuples.Tuple;
import org.crappydbms.transactions.LockMode;
import org.crappydbms.transactions.Transaction;
import org.crappydbms.transactions.TransactionAbortedException;

/**
 * @author Facundo Manuel Quiroga Dec 19, 2008
 * 
 */
public class ModifyOperation	 extends AbstractOperation {

	ArrayList<Modification> modifications;
	
	public ModifyOperation(StoredRelation relation, Predicate predicate, Modification modification) {
		this(relation,predicate,modificationToArrayList(modification));
	}
	protected static ArrayList<Modification> modificationToArrayList(Modification modification){
		 ArrayList<Modification> modifications = new ArrayList<Modification>();
		 modifications.add(modification);
		 return modifications;
	}
	
	public ModifyOperation(StoredRelation relation, Predicate predicate, ArrayList<Modification> modifications) {
		this.setRelation(relation);
		this.setPredicate(predicate);
		this.setModifications(new ArrayList<Modification>());
		this.getModifications().addAll(modifications);
	}

	@Override
	public IntegerField perform(Transaction transaction) throws TransactionAbortedException, InvalidAttributeNameException {
		int count = 0;
		StoredTuple tuple;
		StoredRelationIterator<Tuple> i = this.getRelation().iterator(transaction,LockMode.exclusive());
		while (i.hasNext()) {
			tuple = (StoredTuple) i.next();
			if (predicate.isTrueFor(tuple)) {
				i.modifyTuple(modifications);
				count++;
			}
		}
		return IntegerField.valueOf(count);
	}

	protected ArrayList<Modification> getModifications() {
		return this.modifications;
	}

	protected void setModifications(ArrayList<Modification> modifications) {
		this.modifications = modifications;
	}

}
