package finalmission.domain.reservation.application;

import finalmission.domain.reservation.domain.Reservation;
import finalmission.domain.reservation.dto.CreateReservationRequest;
import finalmission.domain.reservation.dto.DetailReservationResponse;
import finalmission.domain.reservation.dto.ModifyReservationRequest;
import finalmission.domain.reservation.dto.ReservationResponse;
import finalmission.domain.reservation.exception.HolidayException;
import finalmission.domain.reservation.exception.InvalidReservationUserException;
import finalmission.domain.reservation.exception.PastDateException;
import finalmission.domain.reservation.exception.ReservationAlreadyExistedException;
import finalmission.domain.restaurant.exception.RestaurantNotAvailableException;
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

        // 오늘이나 이전 날짜면 불가능
        validateFutureDate(date);

        // 존재하지 않는 사용자인지 확인
        User user = userQueryService.getBy(request.userId());

        // 예약이 있으면 불가능
        RestaurantSchedule restaurantSchedule = restaurantScheduleQueryService.getBy(restaurantId, timeId, date);
        validateNotReservedSchedule(restaurantSchedule);

        // 이용 가능한 스케줄이 아니면 불가능
        validateAvailableSchedule(restaurantSchedule);

        // 공휴일이면 불가능
        validateNotHoliday(date);

        // 성공!
        Reservation savedReservation = reservationRepository.save(
                new Reservation(restaurantSchedule, user));
        return ReservationResponse.from(savedReservation);
    }

    public List<ReservationResponse> getAll() {
        return reservationQueryService.findAll().stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public DetailReservationResponse getDetail(final long reservationId, final long userId) {
        Reservation reservation = reservationQueryService.findById(reservationId);
        validateReservationUser(userId, reservation);
        return DetailReservationResponse.from(reservation);
    }

    @Transactional
    public ReservationResponse modify(final ModifyReservationRequest request) {
        long reservationId = request.reservationId();
        LocalDate date = request.date();
        long timeId = request.timeId();
        long userId = request.userId();

        Reservation reservation = reservationQueryService.findById(reservationId);
        validateReservationUser(userId, reservation);

        // 오늘이나 과거 날짜면 못 바꿈
        validateFutureDate(date);

        // 이미 예약 있으면 못 바꿈
        RestaurantSchedule newSchedule = restaurantScheduleQueryService.getBy(timeId, date);
        validateNotReservedSchedule(newSchedule);

        // 이용 가능한 스케줄이 아니어도 못 바꿈
        validateAvailableSchedule(newSchedule);

        // 공휴일이면 불가능
        validateNotHoliday(date);

        reservation.changeSchedule(newSchedule);
        return ReservationResponse.from(reservation);
    }

    private void validateFutureDate(LocalDate date) {
        LocalDate today = LocalDate.now();

        if(date.isEqual(today) || date.isBefore(today)) {
            throw new PastDateException();
        }
    }

    private void validateNotReservedSchedule(RestaurantSchedule restaurantSchedule) {
        if(reservationQueryService.isAlreadyExisted(restaurantSchedule.getId())) {
            throw new ReservationAlreadyExistedException();
        }
    }

    private void validateAvailableSchedule(RestaurantSchedule restaurantSchedule) {
        if(restaurantSchedule.isNotAvailable()) {
            throw new RestaurantNotAvailableException();
        }
    }

    private void validateNotHoliday(final LocalDate date) {
        if (holidayApiClient.isHoliday(date)) {
            throw new HolidayException();
        }
    }

    private void validateReservationUser(long userId, Reservation reservation) {
        if(reservation.notBelongTo(userId)) {
            throw new InvalidReservationUserException();
        }
    }
}
