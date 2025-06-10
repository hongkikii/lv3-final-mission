package finalmission.domain.restaurantSchedule.infrastructure;

import finalmission.domain.restaurantSchedule.domain.RestaurantSchedule;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantScheduleRepository extends JpaRepository<RestaurantSchedule, Long> {
    Optional<RestaurantSchedule> findByRestaurant_idAndTime_idAndDate(Long restaurantId, Long timeId, LocalDate date);

}
