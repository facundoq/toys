package org.flightreservationsclient.main;

import java.util.ArrayList;

/**
 * @author Facundo Quiroga
 * Creation date: Feb 9, 2009 3:36:12 PM
 */
public class StressTestUtils {
	
	public static ArrayList<String> getCountries(){
		ArrayList<String> countries = new ArrayList<String>();
		countries.add("Japan");
		countries.add("China");
		countries.add("Switzerland");
		countries.add("Australia");
		countries.add("Fiji");
		countries.add("New Zeland");
		countries.add("Jamaica");
		countries.add("Cuba");
		countries.add("Honk Kong");
		countries.add("Tibet");
		countries.add("Iran");
		countries.add("Mozambique");
		countries.add("Slovakia");
		countries.add("Germany");
		return countries;
	}
	
	public static int getMaxYear(){
		return 2009;
	}
	public static int getMinYear(){
		return 2008;
	}
}
