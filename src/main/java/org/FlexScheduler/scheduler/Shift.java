package org.FlexScheduler.scheduler;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Shift {
	
	private char dow;
	private Date startTime;
	private Date endTime;
	private Employee eOne = null;
	private Employee eTwo = null;
	
	private int location; // MLC = 0, SLC = 1, HSC = 2
	private final String[] LOCS = {"MLC", "SLC", "HSC"};
	
	private double length;
	
	DateFormat formatter = new SimpleDateFormat("hh:mm a");
	
	/**
	 * Creates a new Shift
	 * @param d	The shift's day of the week
	 * @param loc	The shift's location
	 * @param s	The shift's start time "hh:mm a"
	 * @param e	The shift's end time "hh:mm a"
	 */
	public Shift(char d, int loc, String s, String e) {
		location = loc;
		dow = d;
		try {
			startTime = formatter.parse(s);
			endTime = formatter.parse(e);
		} catch (ParseException e1) {
			e1.printStackTrace();
			System.out.println("Invalid time");
		}
	}
	
	/**
	 * Creates a copy of another shift
	 * @param s	The shift to be copied
	 */
	public Shift(Shift s) {
		dow = s.getDow();
		startTime = s.getStart();
		endTime = s.getEnd();
		eOne = s.getEmployees(0);
		eTwo = s.getEmployees(1);
		
		location = s.getLoc();
	}
	
	/**
	 * Changes the start of the shift 
	 * @param d	The start date which the shift should be assigned (only indicating time)
	 */
	public void setStart(Date d) {
		startTime = d;
	}
	
	/**
	 * Changes the end of the shift
	 * @param d	The end date which the shift should be assigned (only indicating time)
	 */
	public void setEnd(Date d) {
		endTime = d;
	}
	
	/**
	 * Returns whether or not two shifts denote the same start and end times
	 * @param s	The shift to be compared to
	 * @return	True if the shifts are equal, false otherwise
	 */
	public boolean equals(Shift s) {
		if (dow == s.getDow() && startTime.equals(s.getStart()) && endTime.equals(s.getEnd()))
			return true;
		else 
			return false;
	}
	
	/**
	 * Returns the start and end times of the shift, the day of the week, the employees working it,
	 * and the location
	 */
	public String toString() {
		String str = "", start, end;
		start = formatter.format(startTime);
		end = formatter.format(endTime);
		str += "Time: " + start + " - " + end;
		str += " (" + dow + ")";
		str += ": " + (eOne != null ? eOne : "")  + (eTwo != null ? ", " + eTwo : "") 
				+ " (" + (0 <= location && location <= LOCS.length ? LOCS[location] : "Anywhere") + ")";
		return str;
	}
	
	/**
	 * Assigns an employee to the shift
	 * @param e	The employee to be assigned to the shift
	 * @return	True if the assignment was successful, false otherwise
	 */
	public boolean addEmployee(Employee e) {		
		if (eOne == null) {
			eOne = e;
			return true;
		} else if (eTwo == null && (dow != 'S' && dow != 'U')) {
			eTwo = e;
			return true;
		}
		return false;
	}
	
	/**
	 * Replaces the Shift's employees with a new list of employees
	 * @param list	The new list of employees to be assigned to the shift
	 */
	public void replaceEmps(List<Employee> list) {
		if (list.size() == 1) {
			eOne = list.get(0);
			eTwo = null;
		} else {
			eOne = list.get(0);
			eTwo = list.get(1);
		}
	}
	
	/**
	 * Returns whether or not the shift is covered.
	 * <p>The SLC needs only one person to open; the HSC needs only one person at all times;
	 * and SLC daytime and MLC need two people at all times.
	 * @return
	 */
	public boolean isCovered() {
		boolean opening = false;
		try {
			opening = startTime.before(formatter.parse(dow == 'T' || dow == 'R' ? "11:00 am" : "10:10 am"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if (location == 1 && opening) 		// SLC needs one person to open
			return eOne != null;
		else if (location == 2)				// HSC only needs one person
			return eOne != null;
		else								// SLC daytime and MLC need two people
			return eOne != null && eTwo != null;
	}
	
	/**
	 * Returns whether the Shift is an opening shift
	 * @return	True if the Shift is an opening shift, false otherwise
	 */
	public boolean isOpening() {
		try {
			return startTime.equals(formatter.parse("7:30 am"));
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Returns whether the Shift is a closing shift
	 * @return	True if the Shift is a closing shift, false otherwise
	 */
	public boolean isClosing() {
		try {
			return startTime.equals(formatter.parse("5:00 pm"));
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Returns an ArrayList of the Employees working this Shift
	 * @return	An ArrayList of the Employees working this Shift
	 */
	public ArrayList<Employee> getEmployees() {
		ArrayList<Employee> list = new ArrayList<Employee>();
		if(eOne != null) {
			list.add(eOne);
		}
		if (eTwo != null) {
			list.add(eTwo);
		}
		return list;
	}
	
	/**
	 * Returns the Employee at the specified index in the Shift's ArrayList<Employee>
	 * @param i	The index in the ArrayList<Employee>
	 * @return	The Employee at index i of the Shift's ArrayList<Employee>
	 */
	public Employee getEmployees(int i) {
		if (i == 0)
			return eOne;
		else if (i == 1)
			return eTwo;
		else
			return null;
	}
	/**
	 * Returns the length in hours of the Shift
	 * @return	The length in hours of the Shift
	 */
	public double getLength() {
		calcLength();
		return length;
	}
	
	/**
	 * Returns the start time of the Shift
	 * @return	The start date of the Shift (only indicating time) 
	 */
	public Date getStart() {
		return startTime;
	}
	
	/**
	 * Returns the end time of the Shift
	 * @return	The end date of the Shift (only indicating time)
	 */
	public Date getEnd() {
		return endTime;
	}
	
	public void calcLength() {
		length = (double) (endTime.getTime() - startTime.getTime()) / (3.6 * Math.pow(10, 6));
	}
	
	/**
	 * Returns the day of the week the Shift lies on
	 * @return	The char denoting the day of the week the Shift lies on
	 */
	public char getDow() {
		return dow;
	}
	
	/**
	 * Returns the Shift's location
	 * @return	The int denoting the Shift's location
	 */
	public int getLoc() {
		return location;
	}
	
	/**
	 * Returns whether or not the Shift overlaps with a specified class period. This
	 * assumes that the employee can make it to the location in the 15 minute break between
	 * classes.
	 * @param c	The Shift denoting the class time in question
	 * @return
	 */
	public boolean overlapsClass(Shift c) {	// As in, if they have a class they will make it if the timeslots touch
		if (c.getDow() == dow) {
			// startA < startB && endA > endB
			if (startTime.getTime() < c.getEnd().getTime() && endTime.getTime() > c.getStart().getTime())
				return true;
			else
				return false;
		} else return false;
	}

	/**
	 * Returns whether or not the Shift overlaps with another Shift. This assumes that the
	 * employee will not be able to make an adjacent shift at a different location
	 * @param s	The Shift in question
	 * @return	Returns true if the Shift overlaps with the other Shift, false if otherwise
	 */
	public boolean overlapsShift(Shift s) {
		if (s.getDow() == dow || s.getDow() == 'Z') {
			// startA <= startB && endA >= endB
			if (startTime.getTime() <= s.getEnd().getTime() && endTime.getTime() >= s.getStart().getTime() && location == s.getLoc())
				return true;
		}
		return false;
	}
	
	/**
	 * Returns whether or not the Shift overlaps with any Shift in an ArrayList of Shifts
	 * @param arr	The ArrayList of Shifts in question
	 * @return	Returns true if the Shift overlaps with any of the Shifts in the ArrayList, false
	 * if otherwise
	 */
	public boolean overlapsShift(ArrayList<Shift> arr) {
		for (Shift s : arr) {
			if (overlapsShift(s))
				return true;
		}
		return false;
	}

}