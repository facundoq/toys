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
package org.crappydbms.queries.operators.projection;

import java.util.ArrayList;

import org.crappydbms.exceptions.InvalidAttributeNameException;
import org.crappydbms.relations.fields.Field;
import org.crappydbms.relations.schemas.RelationSchema;
import org.crappydbms.relations.schemas.StoredRelationSchema;
import org.crappydbms.relations.tuples.AbstractTuple;
import org.crappydbms.relations.tuples.InvalidFieldPositionException;
import org.crappydbms.relations.tuples.StoredTuple;
import org.crappydbms.relations.tuples.Tuple;

/**
 * @author Facundo Manuel Quiroga
 * Dec 4, 2008
 */

public class ProjectedTuple extends  AbstractTuple {

	protected ProjectedRelationSchema projectedRelationSchema;
	protected Tuple tuple;

	public ProjectedTuple(ProjectedRelationSchema projectedRelationSchema, Tuple tuple){
		this.setProjectedRelationSchema(projectedRelationSchema);
		this.setTuple(tuple);
	}

	// PUBLIC
	@Override
	public Field getFieldAtPosition(int n) throws InvalidFieldPositionException {
		return this.getProjectedRelationSchema().getFieldAtPosition(n,this);
	}
	
	@Override
	public ArrayList<Field> getFields() {
		return this.getProjectedRelationSchema().getFieldsFrom(this);
	}
	

	@Override
	public Field getFieldNamed(String attributeName) throws InvalidAttributeNameException {
		return this.getProjectedRelationSchema().getFieldNamedFrom(attributeName, this);
	}


	@Override
	public int numberOfFields() {
		return this.getProjectedRelationSchema().getNumberOfAttributes();
	}

	// PROTECTED
	
	protected ProjectedRelationSchema getProjectedRelationSchema() {
		return this.projectedRelationSchema;
	}


	protected void setProjectedRelationSchema(ProjectedRelationSchema projectedRelationSchema) {
		this.projectedRelationSchema = projectedRelationSchema;
	}

	protected Tuple getInnerTuple(){
		return this.tuple;
	}

	protected void setTuple(Tuple tuple) {
		this.tuple = tuple;
	}

	@Override
	public RelationSchema getRelationSchema() {
		return this.getProjectedRelationSchema();
	}

	@Override
	public StoredTuple convertToStoredTuple(StoredRelationSchema schema) {
		return new StoredTuple(this.getFields(),schema);
	}
}
