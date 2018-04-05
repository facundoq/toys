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
package org.crappydbms.queries.operators.join.cartesianproduct;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.crappydbms.main.CrappyDBMSTestCase;
import org.crappydbms.queries.operators.join.TuplesAndSchemasMatchException;
import org.crappydbms.relations.schemas.StoredRelationSchema;
import org.crappydbms.relations.tuples.Tuple;

/**
 * @author Facundo Manuel Quiroga
 * Dec 11, 2008
 * 
 */
public class JoinTupleTest extends TestCase {

	protected StoredRelationSchema assetsRelationSchema;
	protected StoredRelationSchema customersRelationSchema;
	protected JoinRelationSchema joinRelationSchema;
	protected Tuple assetTuple;
	protected Tuple customerTuple;
	
	
	protected void setUp() throws Exception {
		assetsRelationSchema = CrappyDBMSTestCase.createAssetsRelationSchema();
		customersRelationSchema = CrappyDBMSTestCase.createCustomersRelationSchema(); 
		joinRelationSchema = new JoinRelationSchema(assetsRelationSchema,customersRelationSchema);
		assetTuple = CrappyDBMSTestCase.createRandomTupleForAssetsRelation( assetsRelationSchema);
		customerTuple = CrappyDBMSTestCase.createRandomTupleForCustomersRelation( customersRelationSchema);
	}
	
	public void testNew(){
		try {
			new JoinTuple(assetTuple,customerTuple,joinRelationSchema);
		} catch (TuplesAndSchemasMatchException e) {
			Assert.fail("Tuple's schema should match with join schema");
		}
	}
	
	public void testFields() throws Exception {
		JoinTuple joinTuple = new JoinTuple(assetTuple,customerTuple,joinRelationSchema);
		Assert.assertEquals("Wrong number of fields",7, joinTuple.numberOfFields());
		Assert.assertEquals("Incorrect joining of fields at assets 0, joined 0 ",assetTuple.getFieldAtPosition(0), joinTuple.getFieldAtPosition(0));
		Assert.assertEquals("Incorrect joining of fields at assets 1, joined 1 ",assetTuple.getFieldAtPosition(1), joinTuple.getFieldAtPosition(1));
		Assert.assertEquals("Incorrect joining of fields at assets 2, joined 2 ",assetTuple.getFieldAtPosition(2), joinTuple.getFieldAtPosition(2));
		Assert.assertEquals("Incorrect joining of fields at assets 3, joined 3 ",assetTuple.getFieldAtPosition(3), joinTuple.getFieldAtPosition(3));
		Assert.assertEquals("Incorrect joining of fields at customers 0, joined 4 ",customerTuple.getFieldAtPosition(0), joinTuple.getFieldAtPosition(4));
		Assert.assertEquals("Incorrect joining of fields at customers 1, joined 5 ",customerTuple.getFieldAtPosition(1), joinTuple.getFieldAtPosition(5));
		Assert.assertEquals("Incorrect joining of fields at customers 2, joined 6 ",customerTuple.getFieldAtPosition(2), joinTuple.getFieldAtPosition(6));

}

	protected void tearDown() throws Exception {
	}
	
}
