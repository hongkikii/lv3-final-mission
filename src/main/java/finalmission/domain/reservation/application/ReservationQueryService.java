package finalmission.domain.reservation.application;

import finalmission.domain.reservation.domain.Reservation;
import finalmission.domain.reservation.exception.ReservationNotFoundException;
import finalmission.domain.reservation.infrastructure.ReservationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationQueryService {

    private final ReservationRepository reservationRepository;

    public boolean isAlreadyExisted(Long reservationScheduleId) {
        return reservationRepository.existsByRestaurantSchedule_Id(reservationScheduleId);
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public Reservation findById(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(ReservationNotFoundException::new);
    }
}
