package org.FlexScheduler.home;

import org.FlexScheduler.scheduler.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class EmployeeManagerCommandLineRunner implements CommandLineRunner {

	@Autowired
	private EmpRepository empRepository;
	
	@Autowired
	private CalRepository calRepository;
	
	@Override
	public void run(String... arg0) throws Exception {
		Employee kyle = new Employee("Kyle Aig-Imoukhuede");
		Employee sayed = new Employee("Sayed Hussaini");
		Employee rish = new Employee("Amrish Nair");
		Employee bon = new Employee("Bonnie Hester");
		//empRepository.save(kyle);
		//empRepository.save(sayed);
		//empRepository.save(rish);
		//empRepository.save(bon);
		Calendar cal = new Calendar(0);
		calRepository.save(cal);
	}

}
