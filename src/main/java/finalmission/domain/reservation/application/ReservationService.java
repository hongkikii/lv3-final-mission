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
import finalmission.domain.reservation.infrastructure.ReservationRepository;
import finalmission.domain.schedule.application.ScheduleQueryService;
import finalmission.domain.schedule.domain.Schedule;
import finalmission.domain.user.application.UserQueryService;
import finalmission.domain.user.domain.User;
import java.time.Clock;
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
    private final ScheduleQueryService scheduleQueryService;
    private final ReservationQueryService reservationQueryService;
    private final UserQueryService userQueryService;
    private final HolidayApiClient holidayApiClient;
    private final Clock clock;

    @Transactional
    public ReservationResponse create(CreateReservationRequest request) {
        validateDate(request.date());

        User user = userQueryService.getBy(request.userId());
        Schedule schedule = scheduleQueryService.getBy(request.restaurantId(), request.timeId(), request.date());

        validateNotReservedSchedule(schedule);

        Reservation savedReservation = reservationRepository.save(new Reservation(schedule, user));
        return ReservationResponse.from(savedReservation);
    }

    public List<ReservationResponse> getAll() {
        return reservationQueryService.findAll().stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public DetailReservationResponse getDetail(long reservationId, long userId) {
        Reservation reservation = reservationQueryService.getBy(reservationId);
        validateReservationUser(userId, reservation);
        return DetailReservationResponse.from(reservation);
    }

    @Transactional
    public ReservationResponse modify(ModifyReservationRequest request) {
        validateDate(request.date());

        Reservation reservation = reservationQueryService.getBy(request.reservationId());
        validateReservationUser(request.userId(), reservation);

        Schedule newSchedule = scheduleQueryService.getBy(request.timeId(), request.date());
        validateNotReservedSchedule(newSchedule);

        reservation.changeSchedule(newSchedule);
        return ReservationResponse.from(reservation);
    }

    @Transactional
    public void delete(long reservationId, long userId) {
        Reservation reservation = reservationQueryService.getBy(reservationId);

        validateReservationUser(userId, reservation);
        reservationRepository.delete(reservation);
    }

    private void validateDate(LocalDate date) {
        validateFutureDate(date);
        validateNotHoliday(date);
    }

    private void validateFutureDate(LocalDate date) {
        LocalDate today = LocalDate.now(clock);

        if (date.isEqual(today) || date.isBefore(today)) {
            throw new PastDateException();
        }
    }

    private void validateNotHoliday(LocalDate date) {
        if (holidayApiClient.isHoliday(date)) {
            throw new HolidayException();
        }
    }

    private void validateNotReservedSchedule(Schedule schedule) {
        if (reservationQueryService.isAlreadyExisted(schedule.getId())) {
            throw new ReservationAlreadyExistedException();
        }
    }

    private void validateReservationUser(long userId, Reservation reservation) {
        if (reservation.notBelongTo(userId)) {
            throw new InvalidReservationUserException();
        }
    }
}
