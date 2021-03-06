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
package org.crappydbms.queries.operators.join;

import org.crappydbms.queries.operators.Operator;
import org.crappydbms.relations.Relation;

/**
 * @author Facundo Manuel Quiroga
 * Feb 22, 2009
 * 
 */
public abstract class CompositeOperator implements Operator {
	protected Relation firstRelation;

	protected Relation secondRelation;

	protected Relation getFirstRelation() {
		return this.firstRelation;
	}

	protected void setFirstRelation(Relation firstRelation) {
		this.firstRelation = firstRelation;
	}

	protected Relation getSecondRelation() {
		return this.secondRelation;
	}

	protected void setSecondRelation(Relation secondRelation) {
		this.secondRelation = secondRelation;
	}
	
	
	
}
