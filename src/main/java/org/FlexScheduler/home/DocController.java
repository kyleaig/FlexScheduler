package org.FlexScheduler.home;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.FlexScheduler.scheduler.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DocController {
	
	@Autowired
	private EmpRepository empRepository;
	
	@Autowired
	private CalRepository calRepository;
	
	DateFormat formatter = new SimpleDateFormat("hh:mm a");

    @RequestMapping("/")
    public String schedule(@RequestParam(value="calId", required=false)Long calId, Model model) {
    	// Which Calendars to send
        if (calId != null)
        	model.addAttribute("cal", calRepository.findOne(calId));
        else if (calRepository.count() > 0) {
        	model.addAttribute("cal", calRepository.findAll().get(0));
        	calId = calRepository.findAll().get(0).getId();
        }
        model.addAttribute("calId", calId);
        model.addAttribute("emps", empRepository.findAll());
        try {
			model.addAttribute("timeslots", genTimeSlots());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return "index";
    }
    
    // New Employee
    @PostMapping(value = "/", params = "newEmpName")
    public String newEmp(@RequestParam(value="newEmpName", required=true) String newEmpName, Model model) {
    	Employee newEmp = new Employee(newEmpName);
    	model.addAttribute("newEmp", newEmp.getName());
    	empRepository.save(newEmp);
    	return schedule(calRepository.findAll().get(0).getId(), model);
    }
    
    /*
    // New Shift
    @PostMapping(value = "/", method = RequestMethod.POST)
    public String newShift(@RequestParam(value="empId", required=true) Long empId, @RequestParam(value="calId", required=true)Long calId, Model model) {
    	
    	return schedule(calId, model);
    }*/
    
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