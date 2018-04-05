package org.flightreservationsclient.main.utils;

import java.util.Random;

/**
 * @author Facundo Quiroga
 * Creation date: Feb 7, 2009 5:44:35 PM
 */
public class RandomDateGenerator {
	protected int minYear, maxYear;
	protected Random random;
	
	public RandomDateGenerator(int minYear, int maxYear) {
		if (minYear>maxYear){
			throw new IllegalArgumentException("minYear should be <= than maxYear");
		}
		this.setMaxYear(maxYear);
		this.setMinYear(minYear);
		this.setRandom(new Random());
	}
	public String minDate(){
		return this.dateToString(this.getMinYear(), 1, 1);
	}
	public String maxDate(){
		return this.dateToString(this.getMaxYear(), 12, 30);
	}
	public String generateRandomDate(){
		int range = this.getMaxYear() - this.getMinYear()+1; 
		int year = this.getRandom().nextInt(range)+this.getMinYear();
		int month = this.getRandom().nextInt(12)+1;
		int day = this.getRandom().nextInt(30)+1;
		return this.dateToString(year, month, day);
	}
	protected String dateToString(int year, int month,int day){
		String yearString = ( (year<10)?"000": (year<100)?"00": (year<1000)?"0":"" ) + year;
		String monthString = "" + ( (month<10)?"0"+month:month);
		String dayString = "" + ( (day<10)?"0"+day:day);
		return  yearString+"/"+monthString+"/"+dayString;
	}
	protected int getMinYear() {
		return this.minYear;
	}

	protected void setMinYear(int minYear) {
		this.minYear = minYear;
	}

	protected int getMaxYear() {
		return this.maxYear;
	}

	protected void setMaxYear(int maxYear) {
		this.maxYear = maxYear;
	}
	protected Random getRandom() {
		return this.random;
	}
	protected void setRandom(Random random) {
		this.random = random;
	}
	
}
