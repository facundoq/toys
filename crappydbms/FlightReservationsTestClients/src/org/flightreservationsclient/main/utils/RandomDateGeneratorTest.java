package org.flightreservationsclient.main.utils;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * @author Facundo Quiroga
 * Creation date: Feb 7, 2009 5:59:53 PM
 */
public class RandomDateGeneratorTest extends TestCase {
	
	RandomDateGenerator randomDateGenerator;
	int minYear;
	int maxYear;
	protected void setUp() throws Exception {
		minYear = 30;
		maxYear = 2008;
		randomDateGenerator = new RandomDateGenerator(minYear,maxYear);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testMinMaxDates(){
		Assert.assertEquals("0030/01/01", randomDateGenerator.minDate());
		Assert.assertEquals("2008/12/30", randomDateGenerator.maxDate());
	}
	
	public void testDateGeneration(){
		String minDate = randomDateGenerator.minDate();
		String maxDate = randomDateGenerator.maxDate();
		for (int i = 0; i<10000; i++){
			String date = randomDateGenerator.generateRandomDate();
			assertFalse("min "+minDate+" vs "+date,minDate.compareTo(date) > 0);
			assertFalse("max "+maxDate+" vs "+date,date.compareTo(maxDate)>0);
		}
		
	}
}
