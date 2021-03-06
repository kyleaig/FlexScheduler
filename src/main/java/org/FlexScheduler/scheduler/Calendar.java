package org.FlexScheduler.scheduler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@NamedQuery(query = "select c from Calendar c", name = "query_find_all_calendars")
@Table(name = "cal")
public class Calendar {
	
	private Long id;
	
	public static final char DAYS[] = {'M', 'T', 'W', 'R', 'F', 'S', 'U'};
	
	private List<Day> week = new ArrayList<Day>();
	
	private String name = "Calendar";
	
	protected Calendar() {}
	
	/**
	 * Creates a new Calendar object
	 * @param i	Specifies the type of calendar to create (0 - MLC, 1 - SLC, 2 - HSC)
	 */
	public Calendar(int i) {
		if (i == 0) {
			week.addAll(Arrays.asList(new Day('M', i), new Day('T', i), new Day('W', i), new Day('R', i),
					new Day('F', i), new Day('S', i), new Day('U', i)));
		} else {
			week.addAll(Arrays.asList(new Day('M', i), new Day('T', i), new Day('W', i), new Day('R', i),
					new Day('F', i)));
		}
		
		for (Day day : week) {
			day.setCal(this);
		}
		if (i == 0)
			setName("MLC Calendar");
		else if (i == 1)
			setName("SLC Calendar");
		else if (i == 2)
			setName("HSC Calendar");
	}
	
	/**
	 * Creates a copy of another Calendar object
	 * @param cal	The Calendar to be copied
	 */
	public Calendar(Calendar cal) {	// Copy Constructor
		name = getName();
		for (Day d : cal.getWeek()) {
			week.add(new Day(d));
		}
	}
	
	/**
	 * Returns the name of the Calendar
	 * @return	The name of the Calendar
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the Calendar's ID
	 * @return	The Calendar's ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * Sets the name of the Calendar
	 * @param s	The string which to assign to the Calendar's name
	 */
	public void setName(String s) {
		name = s;
	}
	
	/**
	 * Shuffles the Calendar's ArrayList of Days
	 */
	public void shuffleDays() {
		Collections.shuffle(week);
	}
	
	/**
	 * Orders the Calendar's ArrayList of Days (Monday - Sunday)
	 */
	public void orderDays() {
		Collections.sort(week);
	}
	
	/**
	 * Returns the Calendar's work week
	 * @return	An ArrayList of Days in the Calendar's work week
	 */
	@OneToMany(cascade = CascadeType.ALL, mappedBy="cal")
	public List<Day> getWeek() {
		return week;
	}
	
	public void setWeek(List<Day> week) {
		this.week = week;
	}
	
	/**
	 * Returns the Day object in the Calendar specified by d
	 * @param d	The specified day of the week
	 * @return	The Calendar's day object denoted by d
	 */
	public Day getDay(int d) {
		return week.get(d);
	}
	
	public double calcGrade(ArrayList<Employee> roster) {
		double grade = 0.0, avgHours = 0.0, avgMinHours = 0.0, avgShifts = 0.0;
		int totalShifts = 0, uncoveredShifts = 0;
		for (Employee e : roster) {
			avgHours += e.getCurrHours() / roster.size();
			avgMinHours += e.getMinHours() / roster.size();
			avgShifts += (double) e.getShifts().size() / roster.size();
		}

		for (Day d : week) {
			Set<Shift> shifts = d.getShifts();
			for (Shift s : shifts) {
				if (!s.atCapacity())
					uncoveredShifts++;
			}
			totalShifts += shifts.size();
		}

		// Maximize this number for high grade!
		grade = (avgHours / avgMinHours) / avgShifts - (uncoveredShifts / totalShifts);
		return grade;
	}
	/*
	public String getStats(ArrayList<Employee> roster) {
		String stats = "";
		stats += "Roster Size: " + String.format("%d", roster.size());
		stats += "Average Hours: " + String.format("%.1f", avgHours);
		return stats;
		
		System.out.printf("Average Hours: %.1f \nAverage Min Hours: %.1f \nAverage Shifts: %.3f \nUncovered Shifts: %d \nTotal Shifts: %d\n\n",
				avgHours, avgMinHours, avgShifts, uncoveredShifts, totalShifts);
	}*/

}
