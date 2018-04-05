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
package org.crappydbms.queries.predicates.composite;

import org.crappydbms.queries.predicates.Predicate;



/**
 * @author Facundo Manuel Quiroga
 * Dec 1, 2008
 * 
 */
public abstract class BinaryPredicate extends CompositePredicate {
	
	protected Predicate firstPredicate;
	protected Predicate secondPredicate;
	
	public BinaryPredicate(Predicate firstPredicate,Predicate secondPredicate){
		this.setFirstPredicate(firstPredicate);
		this.setSecondPredicate(secondPredicate);
	}


	public Predicate getFirstPredicate() {
		return this.firstPredicate;
	}
	public void setFirstPredicate(Predicate firstPredicate) {
		this.firstPredicate = firstPredicate;
	}
	public Predicate getSecondPredicate() {
		return this.secondPredicate;
	}
	public void setSecondPredicate(Predicate secondPredicate) {
		this.secondPredicate = secondPredicate;
	}

}
