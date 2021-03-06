/* CrappyDBMS, a top-down 2d action-rpg game written in Java.
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

package org.crappydbms.queries.predicates;

import org.crappydbms.exceptions.InvalidAttributeNameException;
import org.crappydbms.queries.predicates.composite.AndPredicate;
import org.crappydbms.queries.predicates.composite.OrPredicate;
import org.crappydbms.relations.tuples.Tuple;

/**
 * @author Facundo Quiroga
 * Creation date: Nov 27, 2008 8:30:05 PM
 */
public interface Predicate {
	public boolean isTrueFor(Tuple tuple) throws InvalidAttributeNameException;
	
	public AndPredicate and(Predicate predicate);
	public OrPredicate or(Predicate predicate);
	

}
