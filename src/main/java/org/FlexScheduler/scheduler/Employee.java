package org.FlexScheduler.scheduler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Employee implements Comparable<Employee>{
	
	private String name;
	private double minHours = 0;
	private double maxHours = 20;
	
	private double maxHoursDay = 5.5;
	private double minHoursDay = 1.5;
	private int maxShiftsPerDay = 1;
	private double sHours = 20;
	private double mHours = 20;
	
	ArrayList<Integer> locBan = new ArrayList<Integer>();
	
	private ArrayList<Shift> unavailTimes;
	private ArrayList<Shift> avoidedTimes;
	
	private ArrayList<Shift> shifts;
	
	/**
	 * Creates an employee
	 * @param s		Name of the employee (First Last)
	 * @param minH	Minimum hours employee should be scheduled for (greater than or equal to 0)
	 * @param maxH	Maximum hours employee should be scheduled for (must not exceed default of 20)
	 */
	public Employee(String s, int minH, int maxH) {
		name = s;
		minHours = minH;
		maxHours = maxH;
		if (maxHours >= 20)
			maxHours = 20;
		sHours = 20;
		shifts = new ArrayList<Shift>();
		unavailTimes = new ArrayList<Shift>();
		avoidedTimes = new ArrayList<Shift>();
	}
	
	/**
	 * Returns the name of the employee
	 * @return	String name of the employee
	 */
	public String getName() {
		return name;
	}

	/**
	 * Adds a new shift to the employee's ArrayList of Shifts
	 * @param s	The shift to be added
	 */
	public void addShift(Shift s) {
		Shift newS = new Shift(s);
		boolean consolidated = false;
		for (Shift sh : shifts) {	// Time slot consolidation
			if (newS.getStart().equals(sh.getEnd()) && sh.getDow() == newS.getDow()) {
				sh.setEnd(newS.getEnd());
				consolidated = true;
				sh.replaceEmps(Arrays.asList(this));
			} else if (newS.getEnd().equals(sh.getStart()) && sh.getDow() == newS.getDow()) {
				sh.setStart(newS.getStart());
				consolidated = true;
				sh.replaceEmps(Arrays.asList(this));
			}
		}
		if (!consolidated)
			shifts.add(newS);
		if (newS.getLoc() == 1)
			sHours -= newS.getLength();
		else if (newS.getLoc() == 0)
			mHours -= newS.getLength();
		addUnavail(newS);
	}
	/**
	 * Adds a shift denoting a time frame that the employee would like to avoid working
	 * @param s2	The shift denoting the time frame that the employee would like to avoid
	 * 				working
	 */
	public void avoid(Shift s2) {
		Shift s = new Shift(s2);
		for (Shift sh : avoidedTimes) {
			if (s.getStart().equals(sh.getEnd())) {
				sh.setEnd(s.getEnd());
				return;
			} else if (s.getEnd().equals(sh.getStart())) {
				sh.setStart(s.getStart());
				return;
			}
		}
		avoidedTimes.add(s);
	}
	
	/**
	 * Returns whether or not the employee is avoiding working during the specified shift 
	 * @param s	The shift in question
	 * @return	True if the employee is avoiding it, false otherwise
	 */
	public boolean isAvoiding(Shift s) {
		for (Shift a : avoidedTimes) {
			if (s.overlapsShift(a)) {
				if (a.getLoc() < 0 || s.getLoc() == a.getLoc()) // Considering location
					return true;
			}
		}
		return false;
	}
	/**
	 * An employee is overworked if they have are assigned the max amount of hours 
	 * they can work in a single day, if they have more than the maximum allowed shifts per day
	 * 
	 * @param s	The added shift that may or may not make the employee overworked.
	 * @return	True if the employee will become overworked by adding the shift, false otherwise
	 */
	public boolean isOverWorked(Shift s) {
		// Too many hours in a day
		char d = s.getDow();
		if (getCurrHours(d) >= maxHoursDay && s.getLoc() != 2)
			return true;
		else if (getShifts(d).size() >= maxShiftsPerDay && !s.overlapsShift(getShifts()))	// Too many shifts in a day
			return true;
		else if (s.getLoc() == 1 && getSHours() <= 0)	// too many hours at SLC
			return true;
		else if (s.getLoc() == 0 && getMHours() <= 0)	// too many hours at MLC
			return true;
		else
			return false;
	}
	/**
	 * Returns an ArrayList of the employee's avoided times
	 * @return	An ArrayList of avoided times
	 */
	public ArrayList<Shift> getAvoidedTimes() {
		return avoidedTimes;
	}
	
	/**
	 * Returns the employee's current hour count
	 * @return	The employee's current hour count
	 */
	public double getCurrHours() {
		double total = 0.0;
		for (Shift s : shifts) {
			total += s.getLength();
		}
		return total;
	}
	
	/**
	 * Returns the employee's current hour count for a specific day
	 * @param d	The specified day in question: {'M', 'T', 'W', 'R', 'F', 'S', 'U'}
	 * @return	The total number of hours on a given day
	 */
	public double getCurrHours(char d) {
		double total = 0.0;
		for (Shift s : shifts) {
			if (s.getDow() == Character.toUpperCase(d))
				total += s.getLength();
		}
		return total;
	}
	/**
	 * Returns the maximum total hours an employee can work
	 * @return	The maximum total hours an employee can work
	 */
	public double getMaxHours() {
		return maxHours;
	}
	/**
	 * Returns the maximum total hours an employee can work in one day
	 * @param d	Any char d will return the same max hours of a single day
	 * @return	The maximum total hours an employee can work in a single day
	 */
	public double getMaxHours(char d) {
		return maxHoursDay;
	}
	
	/**
	 * Returns the minimum total hours an employee can work in one day
	 * @return	The minimum total hours an employee can work
	 */
	public double getMinHours() {
		return minHours;
	}
	/**
	 * Returns the minimum total hours an employee can work in one day
	 * @param d	Any char d will return the same min hours of a single day
	 * @return	The minimum total hours an employee can work in a single day
	 */
	public double getMinHours(char d) {
		return minHoursDay;
	}
	
	/**
	 * Assigns the employee's maximum hour limit to d
	 * @param d	The double to which the employee's maximum hour limit should be assigned
	 */
	public void setMaxHours(double d) {
		maxHours = d;
	}
	
	/**
	 * Assigns the employee's single day maximum hour limit to d 
	 * @param d	The double to which the employee's single day maximum hour limit should be assigned
	 */
	public void setMaxHoursDay(double d) {
		maxHoursDay = d;
	}
	
	/**
	 * Returns the employee's ArrayList of Shifts 
	 * @return	An ArrayList of the employee's shifts
	 */
	public ArrayList<Shift> getShifts() {
		return shifts;
	}
	
	/**
	 * Returns an ArrayList of the employee's shifts for a specific day
	 * @param d	The specified day in question
	 * @return
	 */
	public ArrayList<Shift> getShifts(char d) {
		ArrayList<Shift> d_shifts = new ArrayList<Shift>();
		for (Shift s : shifts) {
			if (s.getDow() == d)
				d_shifts.add(s);
		}
		return d_shifts;
	}
	
	/**
	 * Adds a shift denoting the time frame for which the employee is unavailable to work
	 * @param u	The shift denoting the time frame for which the employee is unavailable to work
	 */
	public void addUnavail(Shift u) {
		Shift s = new Shift(u);
		for (Shift sh : unavailTimes) {	// Consolidating time slots
			if (s.getStart().equals(sh.getEnd())) {
				sh.setEnd(s.getEnd());
				return;
			} else if (s.getEnd().equals(sh.getStart())) {
				sh.setStart(s.getStart());
				return;
			}
		}
		unavailTimes.add(s);
	}
	
	/**
	 * Returns the ArrayList of shifts denoting the employee's unavailable time frames
	 * @return	An ArrayList of shifts denoting the employee's unavailable time frames
	 */
	public ArrayList<Shift> getUnavail() {
		return unavailTimes;
	}
	
	/**
	 * Returns whether or not the employee is unavailable during a specified shift or time frame
	 * @param s	The specified shift or time frame in question
	 * @return
	 */
	public boolean isUnavailable(Shift s) {
		for (Shift u : getUnavail()) {
			if (s.overlapsClass(u) || s.overlapsShift(u)) // They can make it to class but wb a shift?
				return true;
		}
		return false;
	}
	
	/**
	 * Returns the employee's name
	 */
	public String toString() {
		return name;
	}
	
	/**
	 * Returns the maximum number of shifts per day the employee can work
	 * @return	The maximum number of shifts per day the employee can work
	 */
	public int getMaxShiftsPerDay() {
		return maxShiftsPerDay;
	}
	
	/**
	 * Assigns the employee's maximum number of shifts per day to n
	 * @param n	The integer to which the employee's single day maximum number of 
	 * 			shifts limit should be assigned
	 */
	public void setMaxShiftsPerDay(int n) {
		maxShiftsPerDay = n;
	}

	public void setLB(List<Integer> list) {
		for(Integer n : list) {
			locBan.add(new Integer(n));
		}
	}
	
	public List<Integer> getLB() {
		return locBan;
	}
	
	public void setLB(int n) {
		locBan.add(n);
	}
	
	public void setSHours(double h) {
		sHours = h;
	}
	
	public double getSHours() {
		return sHours;
	}
	
	public void setMHours(double h) {
		mHours = h;
	}
	
	public double getMHours() {
		return mHours;
	}
	
	@Override
	public int compareTo(Employee e) {
		if (getCurrHours() - getMinHours() == e.getCurrHours() - e.getMinHours())	// How close they are to achieving their min hours
			return 0;
		else if (getCurrHours() - getMinHours() < e.getCurrHours() - e.getMinHours())
			return -1;
		else 
			return 1;
	}
	
	/**
	 * Returns whether or not the employee is working the specified shift already
	 * @param s	The shift in question
	 * @return	True if the employee is working the shift, false otherwise
	 */
	public boolean isWorking(Shift s) {
		ArrayList<Employee> emps = s.getEmployees();
		for (Employee e : emps) {
			if (e.getName().equals(name))
				return true;
		}
		return false;
	}

	/**
	 * Clears the employee's shifts
	 */
	public void clearShifts() {
		shifts.clear();
	}
/*
	public int compareTo(Employee e) {
		if (getCurrHours() == e.getCurrHours())	// Compare by current hours
			return 0;
		else if (getCurrHours()< e.getCurrHours())
			return -1;
		else 
			return 1;
	}
*/
	/*public boolean hasCloseByShift(Shift s) {
		Shift win = new Shift(s);
		Date threeBef = new Date((long) (s.getStart().getTime() - (3 * (3.6 * Math.pow(10, 6)))));
		for (Shift sh : shifts) {
			if (sh.getEnd().before(threeBef))
		}
		return false;
	}*/
}