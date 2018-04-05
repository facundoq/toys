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

package org.crappydbms.queries.predicates.simple.factories;

import org.crappydbms.queries.predicates.simple.AttributeAttributePredicate;
import org.crappydbms.queries.predicates.simple.AttributeFieldPredicate;
import org.crappydbms.queries.predicates.simple.FieldAttributePredicate;
import org.crappydbms.queries.predicates.simple.FieldFieldPredicate;
import org.crappydbms.queries.predicates.simple.SimplePredicate;
import org.crappydbms.queries.predicates.simple.comparison.ComparisonOperator;
import org.crappydbms.queries.predicates.simple.comparison.EqualsComparisonOperator;
import org.crappydbms.relations.fields.Field;

/**
 * @author Facundo Manuel Quiroga
 * Dec 1, 2008
 * 
 */
public abstract class EqualsPredicateFactory  {

	public static ComparisonOperator getComparisonOperator(){
		return EqualsComparisonOperator.newEqualsComparisonOperator();
	}
	
	public static SimplePredicate newPredicate(Field firstField, Field secondField){
		return new FieldFieldPredicate(firstField,secondField, EqualsPredicateFactory.getComparisonOperator());
	}
	public static SimplePredicate newPredicate(Field field, String attributeName){
		return new FieldAttributePredicate(field,attributeName,EqualsPredicateFactory.getComparisonOperator());
	}
	public static SimplePredicate newPredicate(String attributeName,Field field){
		return new AttributeFieldPredicate(attributeName,field,EqualsPredicateFactory.getComparisonOperator());
	}
	public static SimplePredicate newPredicate(String firstAttributeName,String secondAttributeName){
		return new AttributeAttributePredicate(firstAttributeName,secondAttributeName,EqualsPredicateFactory.getComparisonOperator());
	}


}
