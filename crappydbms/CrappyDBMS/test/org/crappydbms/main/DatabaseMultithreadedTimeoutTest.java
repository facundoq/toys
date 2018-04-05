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
package org.crappydbms.main;


import java.io.File;

import junit.framework.Assert;

import org.crappydbms.datadictionary.DataDictionary;
import org.crappydbms.queries.operations.Modification;
import org.crappydbms.queries.operations.ModifyOperation;
import org.crappydbms.queries.operations.ValueModification;
import org.crappydbms.queries.operators.selection.SelectionOperator;
import org.crappydbms.queries.predicates.TruePredicate;
import org.crappydbms.relations.RelationIterator;
import org.crappydbms.relations.StoredRelation;
import org.crappydbms.relations.fields.StringField;
import org.crappydbms.relations.tuples.Tuple;
import org.crappydbms.transactions.Transaction;
import org.crappydbms.transactions.TransactionAbortedException;



/**
 * @author Facundo Manuel Quiroga
 * Jan 22, 2009
 * 
 */
public class DatabaseMultithreadedTimeoutTest {

	static int finished;

	static synchronized void finished() {
		finished++;
	}

	static int failed;

	static synchronized void failed() {
		failed++;
	}

	static volatile int step;

	static Database database;
	static StoredRelation assets;

	public static void setUp() throws Exception {
		// Create test file
		CrappyDBMSTestCase.deleteTestDirectory();
		File directory = CrappyDBMSTestCase.createTestDirectory();
		DataDictionary dataDictionary = new DataDictionary(directory);
		database = new Database(dataDictionary);
		finished = 0;
		failed = 0;
		step = 0;
		CrappyDBMSTestCase.createTestingAssetsStoredRelationNamed(database, "assets");
		assets = database.getStoredRelationNamed("assets");
		Transaction transaction = database.newTransaction();
		CrappyDBMSTestCase.addRandomTuplesForAssetsRelation(100, assets, transaction);
		transaction.commit();
	}
	
	public static void tearDown() throws Exception {
		if (database != null){
			database.shutDown();
		}
		assets = null;
		database = null;
		CrappyDBMSTestCase.deleteTestDirectory();
	}
	
	public static void main(String[] args) throws Exception{
		try{
			setUp();
			testModifierTimesOut();
		}finally{
			tearDown();
		}

	}
	
	
	public static void testModifierTimesOut() {

		// first reader
		new Thread() {
			public void run() {
				try {
					Transaction transaction = database.newTransaction();
					SelectionOperator select = new SelectionOperator(assets,new TruePredicate());
					RelationIterator<Tuple> iterator = select.iterator(transaction);
					while (iterator.hasNext()){
						iterator.next();
					}
					step++;
					while (step != 2){
						Thread.sleep(3000);
					}
					transaction.commit();
				}catch (TransactionAbortedException e){
					failed();
				}catch (Exception e){
					failed();
				}finally{
					finished();
				}
			}
		}.start();
		
		
		// modifier that will timeout
		new Thread() {
			public void run() {
				try {
					while (step != 1){
						Thread.sleep(3000);
					}
					Transaction transaction = database.newTransaction();
					Modification modification = new ValueModification("name",StringField.valueOf("pepito"));
					ModifyOperation modify = new ModifyOperation(assets,new TruePredicate(),modification);
					modify.perform(transaction);
					failed();
					
					transaction.commit();
				}catch (TransactionAbortedException e){
					// didn't fail, timed out
					//System.out.println("Timed out " +e.getMessage());
				}catch (Exception e){
					failed();
				}finally{
					step++;
					finished();
				}
			}
		}.start();

		while (finished < 2) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				Assert.fail("interrupted exception");
			}
		}
		Assert.assertEquals("A thread failed", 0, failed);
		Assert.assertEquals("Incorrect number  of finished threads", 2, finished);
	}
	
	
}
