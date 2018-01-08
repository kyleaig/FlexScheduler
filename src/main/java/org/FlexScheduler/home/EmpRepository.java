package org.FlexScheduler.home;

import org.FlexScheduler.scheduler.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpRepository extends JpaRepository<Employee, Long> {

}
