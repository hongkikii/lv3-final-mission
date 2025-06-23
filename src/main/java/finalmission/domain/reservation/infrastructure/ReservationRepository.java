package finalmission.domain.reservation.infrastructure;

import finalmission.domain.reservation.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    boolean existsBySchedule_Id(Long restaurantScheduleId);
}
