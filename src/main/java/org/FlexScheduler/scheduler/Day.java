package org.FlexScheduler.scheduler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@NamedQuery(query = "select d from Day d", name = "query_find_all_days")
@Table(name = "day")
public class Day implements Comparable<Day> {

	private Long id;
	
	private Calendar cal;
	private char dow;
	
	private Set<Shift> shifts = new HashSet<Shift>();
	
	protected Day() {}
	
	/**
	 * Creates a new Day object
	 * @param d	The day of the week {'M', 'T', 'W', 'R', 'F', 'S', 'U'}
	 * @param loc	The integer location of the Day
	 */
	public Day(char d, int loc) {
		dow = d;
		if (d == 'M' || d == 'W' || d == 'F') {
			// Monday, Wednesday, Friday
			shifts.add(new Shift(d, loc, "7:30 am", loc != 1 ? "9:05 am" : "10:10 am"));
			if (loc != 1) shifts.add(new Shift(d, loc, "9:05 am", "10:10 am"));
			shifts.add(new Shift(d, loc, "10:10 am", "11:15 am"));
			shifts.add(new Shift(d, loc, "11:15 am", "12:20 pm"));
			shifts.add(new Shift(d, loc, "12:20 pm", "1:25 pm"));
			shifts.add(new Shift(d, loc, "1:25 pm", "2:30 pm"));
			shifts.add(new Shift(d, loc, "2:30 pm", "3:35 pm"));
			shifts.add(new Shift(d, loc, "3:35 pm", "5:00 pm"));
			if (loc == 0)
				shifts.add(new Shift(d, loc, "5:00 pm", d == 'F' ? "7:30 pm" : "10:30 pm"));
			else if (loc == 1)
				shifts.add(new Shift(d, loc, "5:00 pm", d == 'F' ? "7:30 pm" : "9:30 pm"));
		} else if (d == 'T' || d == 'R') {
			// Tuesday, Thursday
			shifts.add(new Shift(d, loc, "7:30 am", loc != 1 ? "9:30 am" : "11:00 am"));
			if (loc != 1) shifts.add(new Shift(d, loc, "9:30 am", "11:00 am"));
			shifts.add(new Shift(d, loc, "11:00 am", "12:30 pm"));
			shifts.add(new Shift(d, loc, "12:30 pm", "2:00 pm"));
			shifts.add(new Shift(d, loc, "2:00 pm", "3:30 pm"));
			shifts.add(new Shift(d, loc, "3:30 pm", "5:00 pm"));
			if (loc == 0)
				shifts.add(new Shift(d, loc, "5:00 pm", "10:30 pm"));
			else if (loc == 1)
				shifts.add(new Shift(d, loc, "5:00 pm", "9:30 pm"));
		} else if (d == 'S') {
			// Saturday
			shifts.add(new Shift(d, loc, "10:00 am", "3:00 pm"));
			shifts.add(new Shift(d, loc, "2:00 pm", "7:30 pm"));
		}
		else {
			// Sunday
			shifts.add(new Shift(d, loc, "11:00 am", "3:00 pm"));
			shifts.add(new Shift(d, loc, "2:00 pm", "7:30 pm"));
		}
		// Setting the One in the OneToMany relationship
		for (Shift s : shifts) {
			s.setDay(this);
		}
	}
	
	/**
	 * Creates a copy of another Day object
	 * @param d	The Day to be copied
	 */
	public Day(Day d) {
		dow = d.getDow();
		Set<Shift> copyShifts = d.getShifts();
		for (Shift s : copyShifts) {
			shifts.add(new Shift(s));
		}
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public void setShifts(Set<Shift> set) {
		shifts = set;
	}
	
	/**
	 * Returns an ArrayList of the shifts in the Day
	 * @return	An ArrayList of the Day's shifts
	 */
	@OneToMany(cascade = CascadeType.ALL, mappedBy="day")
	public Set<Shift> getShifts() {
		return shifts;
	}
	
	/**
	 * Returns the day of the week
	 * @return	The day of the week
	 */
	public char getDow() {
		return dow;
	}
	
	public void setDow(char d) {
		dow = d;
	}
	
	/**
	 * Returns the Calendar associated to this Day
	 * @return	The Calendar associated with this Day
	 */
	@ManyToOne(cascade = CascadeType.ALL)
	public Calendar getCal() {
		return cal;
	}
	
	public void setCal(Calendar cal) {
		this.cal = cal;
	}
	/**
	 * Returns the full string of the day of the week
	 */
	public String toString() {
		String str = "";
		if (dow == 'M')
			str = "Monday";
		else if (dow == 'T')
			str = "Tuesday";
		else if (dow == 'W')
			str = "Wednesday";
		else if (dow == 'R')
			str = "Thursday";
		else if (dow == 'F')
			str = "Friday";
		else if (dow == 'S')
			str = "Saturday";
		else
			str = "Sunday";
		return str;
	}

	@Override
	public int compareTo(Day d) {
		ArrayList<Character> order = new ArrayList<Character>(Arrays.asList('M', 'T', 'W', 'R', 'F', 'S', 'U'));
		if (order.indexOf(new Character(dow)) < order.indexOf(new Character(d.getDow())))
			return -1;
		else
			return 1;
	}

}