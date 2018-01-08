package org.FlexScheduler.home;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.FlexScheduler.scheduler.Shift;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
@EntityScan(basePackages = "org.FlexScheduler.scheduler")
public class ShiftService {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public long insert(Shift sh) {
		entityManager.persist(sh);
		return sh.getId();
	}
	
	public Shift find(long id) {
		return entityManager.find(Shift.class, id);
	}
	
	public List<Shift> findAll() {
		Query query = entityManager.createNamedQuery(
							"query_find_all_shifts", Shift.class);
		return query.getResultList();
	}

}
