package org.FlexScheduler.home;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.FlexScheduler.scheduler.Day;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
@EntityScan(basePackages = "org.FlexScheduler.scheduler")
public class DayService {

	@PersistenceContext
	private EntityManager entityManager;
	
	public long insert(Day day) {
		entityManager.persist(day);
		return day.getId();
	}
	
	public Day find(long id) {
		return entityManager.find(Day.class, id);
	}
	
	public List<Day> findAll() {
		Query query = entityManager.createNamedQuery(
								"query_find_all_days", Day.class);
		return query.getResultList();
	}
}
