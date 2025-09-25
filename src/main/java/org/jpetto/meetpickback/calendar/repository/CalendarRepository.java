package org.jpetto.meetpickback.calendar.repository;

import org.jpetto.meetpickback.calendar.entity.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    Optional<Calendar> findById(long id);
    List<Calendar> findByAccountIdAndStartDateBetween(Long id, LocalDateTime start, LocalDateTime end);
}
