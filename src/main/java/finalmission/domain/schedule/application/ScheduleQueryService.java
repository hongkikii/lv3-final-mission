package finalmission.domain.schedule.application;

import finalmission.domain.restaurant.exception.RestaurantNotAvailableException;
import finalmission.domain.schedule.domain.Schedule;
import finalmission.domain.schedule.exception.ScheduleNotExistedException;
import finalmission.domain.schedule.infrastructure.ScheduleRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ScheduleQueryService {

    private final ScheduleRepository scheduleRepository;

    public Schedule getBy(long restaurantId, long timeId, LocalDate date) {
        Schedule schedule = scheduleRepository.findByRestaurant_idAndTime_idAndDate(
                        restaurantId, timeId, date)
                .orElseThrow(ScheduleNotExistedException::new);

        if (schedule.isNotAvailable()) {
            throw new RestaurantNotAvailableException();
        }
        return schedule;
    }

    public Schedule getBy(long timeId, LocalDate date) {
        Schedule schedule = scheduleRepository.findByTime_idAndDate(timeId, date)
                .orElseThrow(ScheduleNotExistedException::new);

        if (schedule.isNotAvailable()) {
            throw new RestaurantNotAvailableException();
        }
        return schedule;
    }
}
