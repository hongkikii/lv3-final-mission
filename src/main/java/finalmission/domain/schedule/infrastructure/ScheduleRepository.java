package finalmission.domain.schedule.infrastructure;

import finalmission.domain.schedule.domain.Schedule;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Optional<Schedule> findByRestaurant_idAndTime_idAndDate(Long restaurantId, Long timeId, LocalDate date);

    Optional<Schedule> findByTime_idAndDate(Long timeId, LocalDate date);
}
