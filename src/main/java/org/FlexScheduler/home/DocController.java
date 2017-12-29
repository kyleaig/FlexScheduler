package org.FlexScheduler.home;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.FlexScheduler.scheduler.*;

@Controller
public class DocController {
	
	DateFormat formatter = new SimpleDateFormat("hh:mm a");

    @RequestMapping("/")
    public String schedule(@RequestParam(value="name", required=false, defaultValue="Kyle") String name, Model model) {
    	Calendar MLC = new Calendar(0);
    	ArrayList<Employee> employees = new ArrayList<Employee>();
    	employees.addAll(Arrays.asList(new Employee("Kyle"),
    									new Employee("Sayed"),
    									new Employee("Amrish"),
    									new Employee("Bonnie")));
        model.addAttribute("name", name);
        model.addAttribute("cal", MLC);
        model.addAttribute("emps", employees);
        try {
			model.addAttribute("timeslots", genTimeSlots());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return "index";
    }
    
    public ArrayList<String> genTimeSlots() throws ParseException {
    	ArrayList<String> slots = new ArrayList<String>();
    	Date time = formatter.parse("12:00 am");
    	long hour = 3600 * 1000;
    	for (int i = 0; i < 24; i++) {
    		slots.add(formatter.format(time));
    		time = new Date(time.getTime() + hour);
    	}
    	return slots;
    }
}