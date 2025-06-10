package finalmission.domain.reservation.application;

import finalmission.domain.reservation.domain.Reservation;
import finalmission.domain.reservation.dto.CreateReservationRequest;
import finalmission.domain.reservation.dto.ReservationResponse;
import finalmission.domain.reservation.exception.HolidayException;
import finalmission.domain.reservation.exception.PastDateException;
import finalmission.domain.reservation.exception.ReservationAlreadyExistedException;
import finalmission.domain.reservation.exception.RestaurantNotAvailableException;
import finalmission.domain.reservation.infrastructure.ReservationRepository;
import finalmission.domain.restaurantSchedule.application.RestaurantScheduleQueryService;
import finalmission.domain.restaurantSchedule.domain.RestaurantSchedule;
import finalmission.domain.user.application.UserQueryService;
import finalmission.domain.user.domain.User;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RestaurantScheduleQueryService restaurantScheduleQueryService;
    private final ReservationQueryService reservationQueryService;
    private final UserQueryService userQueryService;
    private final HolidayApiClient holidayApiClient;

    @Transactional
    public ReservationResponse create(CreateReservationRequest request) {
        long restaurantId = request.restaurantId();
        long timeId = request.timeId();
        LocalDate date = request.date();
        LocalDate today = LocalDate.now();

        // 오늘이나 이전 날짜면 불가능
        if(date.isEqual(today) || date.isBefore(today)) {
            throw new PastDateException();
        }

        // 존재하지 않는 사용자인지 확인
        User user = userQueryService.getBy(request.userId());

        RestaurantSchedule restaurantSchedule = restaurantScheduleQueryService.getBy(restaurantId, timeId, date);

        // 예약이 있으면 불가능
        if(reservationQueryService.isAlreadyExisted(restaurantSchedule.getId())) {
            throw new ReservationAlreadyExistedException();
        }

        // 이용 가능한 시간대가 아니면 불가능
        if(restaurantSchedule.isNotAvailable()) {
            throw new RestaurantNotAvailableException();
        }

        // 공휴일이면 불가능
        if (holidayApiClient.isHoliday(date)) {
            throw new HolidayException();
        }

        // 성공!
        Reservation savedReservation = reservationRepository.save(new Reservation(restaurantSchedule, user));
        return ReservationResponse.from(savedReservation);
    }

    public List<ReservationResponse> getAll() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponse::from)
                .toList();
    }
}
