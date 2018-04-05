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
package org.crappydbms.relations.schemas.attributes;

/**
 * @author Facundo Manuel Quiroga
 * Oct 31, 2008
 * 
 */
public class BaseAttribute implements Attribute{
	protected String name;
	protected AttributeType type;
	
	public BaseAttribute(String name, AttributeType type){
		this.setName(name);
		this.setType(type);
	}
	

	public boolean equals(Attribute attribute) {
		return (this.matches(attribute) && this.isCompatibleWith(attribute));
	}
	
	public boolean equals(Object attribute) {
	    	return this.equals( (Attribute) attribute);
	}
	public int hashCode(){
		return this.getName().hashCode()+this.getType().hashCode();
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public AttributeType getType() {
		return this.type;
	}

	protected void setName(String name) {
		this.name = name;
	}

	protected void setType(AttributeType type) {
		this.type = type;
	}
	public String toString(){
		return this.getName()+" ("+this.getType().toString()+")";
	}

	@Override
	public boolean matches(Attribute attribute) {
	    return this.getName().equals(attribute.getName());
	}

	public boolean isCompatibleWith(Attribute attribute){
		return this.getType().equals(attribute.getType());
	}

}
