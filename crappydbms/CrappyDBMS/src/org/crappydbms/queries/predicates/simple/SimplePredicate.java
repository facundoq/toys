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

package org.crappydbms.queries.predicates.simple;

import org.crappydbms.exceptions.InvalidAttributeNameException;
import org.crappydbms.queries.predicates.BasePredicate;
import org.crappydbms.queries.predicates.simple.comparison.ComparisonOperator;
import org.crappydbms.relations.fields.Field;
import org.crappydbms.relations.tuples.Tuple;

/**
 * @author Facundo Quiroga
 * Creation date: Nov 27, 2008 8:10:32 PM
 */
public abstract class SimplePredicate extends BasePredicate  {
	
	protected ComparisonOperator comparisonOperator;
	
	protected SimplePredicate(ComparisonOperator comparisonOperator){
		this.setComparisonOperator(comparisonOperator);
	}
	//public abstract static SimplePredicate newPredicate(Field firstField, Field secondField);
	//public abstract static SimplePredicate newPredicate(String attributeName, Field Field);
	//public abstract static SimplePredicate newPredicate(Field Field, String attributeName);
	//public abstract static SimplePredicate newPredicate(String firstAttributeName, String secondAttributeName);
	
	/*
	public SimplePredicate(String columnName,Field field){
		
	}	
	public SimplePredicate(Field field,String columnName){
		
	}
	public SimplePredicate(String columnName1,String columnName2){
		
	}
	public SimplePredicate(Field field1, Field field2){
		
	}*/

	
	@Override
	public boolean isTrueFor(Tuple tuple) throws InvalidAttributeNameException {
		Field firstValue = this.getFirstValueWith(tuple);
		Field secondValue = this.getSecondValueWith(tuple);
		return this.getComparisonOperator().isTrueForValues(firstValue,secondValue);
	}
	
	public abstract Field getFirstValueWith(Tuple tuple) throws InvalidAttributeNameException;
	public abstract Field getSecondValueWith(Tuple tuple) throws InvalidAttributeNameException;

	protected ComparisonOperator getComparisonOperator() {
		return this.comparisonOperator;
	}

	protected void setComparisonOperator(ComparisonOperator comparisonOperator) {
		this.comparisonOperator = comparisonOperator;
	}
	/*
	public String toString(){
		return "firstValue "+ this.getComparisonOperator().toString() +" secondValue";
	}*/



}
