package org.FlexScheduler.scheduler;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;

@Entity
@NamedQuery(query = "select s from Shift s", name = "query_find_all_shifts")
public class Shift {

	private Long id;
	
	private Day day;
	private char dow;
	private Date startTime;
	private Date endTime;
	
	private Set<Employee> emps = new HashSet<Employee>();
	
	private int loc; // MLC = 0, SLC = 1, HSC = 2
	private final String[] LOCS = {"MLC", "SLC", "HSC"};
	
	private double length;
	
	DateFormat formatter = new SimpleDateFormat("hh:mm a");

	private int capacity = 2;
	
	protected Shift() {}
	
	/**
	 * Creates a new Shift
	 * @param d	The shift's day of the week
	 * @param loc	The shift's location
	 * @param s	The shift's start time "hh:mm a"
	 * @param e	The shift's end time "hh:mm a"
	 */
	public Shift(char d, int loc, String s, String e) {
		this.loc = loc;
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
		Set<Employee> set = s.getEmps();
		for (Employee e : set) {
			emps.add(e);
		}
		
		this.loc = s.getLoc();
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
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
	
	@ManyToOne(cascade = CascadeType.ALL)
	public Day getDay() {
		return day;
	}
	
	public void setDay(Day day) {
		this.day = day;
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
		for (Employee e : emps) {
			str+= e + ", ";
		}
		str += " (" + (0 <= loc && loc <= LOCS.length ? LOCS[loc] : "Anywhere") + ")";
		return str;
	}
	
	/**
	 * Assigns an employee to the shift
	 * @param e	The employee to be assigned to the shift
	 * @return	True if the assignment was successful, false otherwise
	 */
	public boolean addEmployee(Employee e) {
		if (emps.size() < capacity) {
			emps.add(e);
			return true;
		}
		return false;
	}
	
	/**
	 * Replaces the Shift's employees with a new list of employees
	 * @param list	The new list of employees to be assigned to the shift
	 */
	public void replaceEmps(Set<Employee> set) {
		emps = set;
	}
	
	public void setCap(int c) {
		capacity = c;
	}
	
	public int getCap() {
		return capacity;
	}
	
	/**
	 * Returns whether or not the shift is covered.
	 * <p>The SLC needs only one person to open; the HSC needs only one person at all times;
	 * and SLC daytime and MLC need two people at all times.
	 * @return
	 */
	public boolean atCapacity() {
		if (emps.size() == capacity)
			return true;
		return false;
	}
	
	/**
	 * Returns a Set of the Employees working this Shift
	 * @return	A Set of the Employees working this Shift
	 */
	@ManyToMany(cascade = CascadeType.ALL)
	public Set<Employee> getEmps() {
		return emps;
	}
	
	public void setEmps(Set<Employee> emps) {
		this.emps = emps;
	}

	/**
	 * Returns the length in hours of the Shift
	 * @return	The length in hours of the Shift
	 */
	public double getLength() {
		calcLength();
		return length;
	}
	
	public void setLength(double length) {
		this.length = length;
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
	
	public void setDow(char dow) {
		this.dow = dow;
	}
	
	/**
	 * Returns the Shift's location
	 * @return	The int denoting the Shift's location
	 */
	public int getLoc() {
		return loc;
	}
	
	public void setLoc(int loc) {
		this.loc = loc;
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
			if (startTime.getTime() <= s.getEnd().getTime() && endTime.getTime() >= s.getStart().getTime() && loc == s.getLoc())
				return true;
		}
		return false;
	}
	
	/**
	 * Returns whether or not the Shift overlaps with any Shift in an ArrayList of Shifts
	 * @param list	The ArrayList of Shifts in question
	 * @return	Returns true if the Shift overlaps with any of the Shifts in the ArrayList, false
	 * if otherwise
	 */
	public boolean overlapsShift(Set<Shift> list) {
		for (Shift s : list) {
			if (overlapsShift(s))
				return true;
		}
		return false;
	}

}