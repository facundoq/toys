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
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import junit.framework.Assert;

import org.crappydbms.datadictionary.DataDictionary;
import org.crappydbms.datadictionary.InvalidStoredRelationNameException;
import org.crappydbms.exceptions.CrappyDBMSException;
import org.crappydbms.logging.CrappyDBMSTestLogger;
import org.crappydbms.relations.schemas.StoredRelationSchema;
import org.crappydbms.relations.schemas.attributes.Attribute;

/**
 * @author Facundo Manuel Quiroga Jan 9, 2009
 * 
 */
public class DatabaseMultithreadedTest extends CrappyDBMSTestCase {
	protected Logger logger = CrappyDBMSTestLogger.getLogger(DatabaseMultithreadedTest.class.getName());

	static volatile int finishedTasks;

	static synchronized void finished() {
		DatabaseMultithreadedTest.finishedTasks++;
	}

	Database theDatabase;
	File directory;

	public void setUp() throws Exception {
		// Create test file
		CrappyDBMSTestCase.deleteTestDirectory();
		directory = CrappyDBMSTestCase.createTestDirectory();
		DataDictionary dataDictionary = new DataDictionary(directory);
		theDatabase = new Database(dataDictionary);
		DatabaseMultithreadedTest.finishedTasks = 0;
	}

	public void tearDown() throws Exception {
		super.tearDown();
	}

	public void testStoredRelationABM() throws InterruptedException {
		int numberOfTasks = 5;
		int amountOfStoredRelationsPerTask = 50;
		for (int i = 0; i < numberOfTasks; i++) {
			StoredRelationABM storedRelationABM = new StoredRelationABM(i, amountOfStoredRelationsPerTask, this.theDatabase);
			new Thread(storedRelationABM).start();
			// System.out.println("Started thread " + i);
		}
		while (DatabaseMultithreadedTest.finishedTasks < numberOfTasks) {
			Thread.sleep(1000);
		}
	}

	public class StoredRelationABM implements Runnable {

		protected int id;

		protected int amount;

		protected Database database;

		StoredRelationSchema assetsSchema;

		public StoredRelationABM(int id, int amount, Database database) {
			this.id = id;
			this.amount = amount;
			this.database = database;
			this.assetsSchema = CrappyDBMSTestCase.createAssetsRelationSchema();
		}

		public void run() {
			try {
				// System.out.println("thread " + id + " adding relations");
				for (int i = 0; i < this.amount; i++) {
					String name = this.getName(id, i);
					try {
						database.addStoredRelation(name, this.assetsSchema, new ArrayList<Attribute>());
					} catch (CrappyDBMSException e) {
						throw new CrappyDBMSTestingError(e.getMessage());
					}
				}

				// System.out.println("thread " + id +
				// " checking relations where added correctly");
				for (int i = 0; i < this.amount; i++) {
					String name = this.getName(id, i);
					try {
						database.getStoredRelationNamed(name);
					} catch (InvalidStoredRelationNameException e) {
						Assert.fail("StoredRelation " + name + " should exist");
					}
				}

				// System.out.println("thread " + id + " removing..");
				for (int i = 0; i < this.amount; i += 10) {
					String name = this.getName(id, i);
					try {
						database.removeStoredRelation(database.getStoredRelationNamed(name));
					} catch (InvalidStoredRelationNameException e) {
						Assert.fail("StoredRelation " + name + " should exist");
					} catch (IOException e) {
						Assert.fail("Error deleting the file");
					}
				}

				// System.out.println("thread " + id + " re checking");
				for (int i = 0; i < this.amount; i++) {
					String name = this.getName(id, i);
					if ((i % 10) == 0) {
						try {
							database.getStoredRelationNamed(name);
							Assert.fail("StoredRelation " + name + " should NOT exist");
						} catch (InvalidStoredRelationNameException e) {
						}
					} else {
						try {
							database.getStoredRelationNamed(name);
						} catch (InvalidStoredRelationNameException e) {
							Assert.fail("StoredRelation " + name + " should exist");
						}
					}
				}
				// System.out.println("thread " + id + " finished");
			} finally {
				DatabaseMultithreadedTest.finished();
			}
		}

		protected String getName(int id, int number) {
			return "id " + id + "-storedRelation-n " + number;
		}
	}

}
