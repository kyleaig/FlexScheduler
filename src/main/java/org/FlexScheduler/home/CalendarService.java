package org.FlexScheduler.home;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.FlexScheduler.scheduler.Calendar;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
@EntityScan(basePackages = "org.FlexScheduler.scheduler")
public class CalendarService {

	@PersistenceContext
	private EntityManager entityManager;
	
	public long insert(Calendar cal) {
		entityManager.persist(cal);
		return cal.getId();
	}
	
	public Calendar find(long id) {
		return entityManager.find(Calendar.class, id);
	}
	
	public List<Calendar> findAll() {
		Query query = entityManager.createNamedQuery(
								"query_find_all_calendars", Calendar.class);
		return query.getResultList();
	}
}
