package org.FlexScheduler.home;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.FlexScheduler.scheduler.Employee;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
@EntityScan(basePackages = "org.FlexScheduler.scheduler")
public class EmployeeService {

	@PersistenceContext
	private EntityManager entityManager;
	
	public long insert(Employee emp) {
		entityManager.persist(emp);
		return emp.getId();
	}
	
	public Employee find(long id) {
		return entityManager.find(Employee.class, id);
	}
	
	public List<Employee> findAll() {
		Query query = entityManager.createNamedQuery(
								"query_find_all_employees", Employee.class);
		return query.getResultList();
	}
}
