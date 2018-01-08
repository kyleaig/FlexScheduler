package org.FlexScheduler.home;

import org.FlexScheduler.scheduler.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalRepository extends JpaRepository<Calendar, Long> {

}