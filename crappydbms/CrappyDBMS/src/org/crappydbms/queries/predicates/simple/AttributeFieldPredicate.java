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
package org.crappydbms.queries.predicates.simple;

import org.crappydbms.exceptions.InvalidAttributeNameException;
import org.crappydbms.queries.predicates.simple.comparison.ComparisonOperator;
import org.crappydbms.relations.fields.Field;
import org.crappydbms.relations.tuples.Tuple;

/**
 * @author Facundo Manuel Quiroga
 * Dec 1, 2008
 * 
 */
public class AttributeFieldPredicate extends SimplePredicate {

	protected String attributeName;
	protected Field field;
	
	public AttributeFieldPredicate(String attributeName, Field field,ComparisonOperator comparisonOperator) {
		super(comparisonOperator);
		this.field = field;
		this.attributeName = attributeName;
	}



	@Override
	public Field getFirstValueWith(Tuple tuple) throws InvalidAttributeNameException {
		return tuple.getFieldNamed(this.attributeName);
	}

	@Override
	public Field getSecondValueWith(Tuple tuple) {
		return this.field;
	}
	
	public String toString(){
		return "attributeName: "+this.attributeName+" "+ this.getComparisonOperator().toString() +" "+this.field;
	}

}
