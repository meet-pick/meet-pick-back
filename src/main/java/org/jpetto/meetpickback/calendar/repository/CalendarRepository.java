package org.jpetto.meetpickback.calendar.repository;

import org.jpetto.meetpickback.calendar.entity.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {
}
