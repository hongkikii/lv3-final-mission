package finalmission.domain.restaurantSchedule.application;

import finalmission.domain.restaurantSchedule.domain.RestaurantSchedule;
import finalmission.domain.restaurantSchedule.exception.RestaurantScheduleNotExistedException;
import finalmission.domain.restaurantSchedule.infrastructure.RestaurantScheduleRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RestaurantScheduleQueryService {

    private final RestaurantScheduleRepository restaurantScheduleRepository;

    public RestaurantSchedule getBy(long restaurantId, long timeId, LocalDate date) {
        return restaurantScheduleRepository.findByRestaurant_idAndTime_idAndDate(restaurantId, timeId, date)
                .orElseThrow(RestaurantScheduleNotExistedException::new);
    }

    public RestaurantSchedule getBy(long timeId, LocalDate date) {
        return restaurantScheduleRepository.findByTime_idAndDate(timeId, date)
                .orElseThrow(RestaurantScheduleNotExistedException::new);
    }
}
