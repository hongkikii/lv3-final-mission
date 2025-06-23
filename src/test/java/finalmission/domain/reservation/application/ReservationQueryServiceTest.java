package finalmission.domain.reservation.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import finalmission.domain.reservation.domain.Reservation;
import finalmission.domain.reservation.domain.ReservationFixture;
import finalmission.domain.reservation.exception.ReservationNotFoundException;
import finalmission.domain.reservation.infrastructure.ReservationRepository;
import finalmission.domain.restaurant.domain.Restaurant;
import finalmission.domain.restaurant.domain.RestaurantFixture;
import finalmission.domain.schedule.domain.Schedule;
import finalmission.domain.schedule.domain.ScheduleFixture;
import finalmission.domain.time.domain.Time;
import finalmission.domain.time.domain.TimeFixture;
import finalmission.domain.user.domain.User;
import finalmission.domain.user.domain.UserFixture;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReservationQueryServiceTest {

    @InjectMocks
    private ReservationQueryService reservationQueryService;

    @Mock
    private ReservationRepository reservationRepository;

    private final Long reservationId = 1L;
    private final Long scheduleId = 1L;

    private Reservation reservation;

    @BeforeEach
    void setUp() {
        User user = UserFixture.createUser(1L, "mimi");
        Time time = TimeFixture.createTime(1L, LocalTime.of(10, 0));
        Restaurant restaurant =  RestaurantFixture.createRestaurant(1L, "restaurant");
        Schedule schedule = ScheduleFixture.createSchedule(scheduleId, LocalDate.of(2025, 6, 20), time, restaurant);
        reservation = ReservationFixture.createReservation(reservationId, user, schedule);
    }

    @DisplayName("스케줄 id에 해당하는 예약이 존재하는 경우 true를 반환한다")
    @Test
    void test1() {
        // given
        when(reservationRepository.existsBySchedule_Id(scheduleId)).thenReturn(true);

        // when
        boolean result = reservationQueryService.isAlreadyExisted(scheduleId);

        // then
        assertThat(result).isTrue();
        verify(reservationRepository).existsBySchedule_Id(scheduleId);
    }

    @DisplayName("모든 예약 목록을 반환한다")
    @Test
    void test2() {
        // given
        List<Reservation> reservations = List.of(reservation);
        when(reservationRepository.findAll()).thenReturn(reservations);

        // when
        List<Reservation> result = reservationQueryService.findAll();

        // then
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.getFirst()).isEqualTo(reservation);
        verify(reservationRepository).findAll();
    }

    @DisplayName("예약 id로 조회에 성공하면 예약 정보를 반환한다")
    @Test
    void test3() {
        // given
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        // when
        Reservation result = reservationQueryService.getBy(reservationId);

        // then
        assertThat(result).isEqualTo(reservation);
        verify(reservationRepository).findById(reservationId);
    }

    @DisplayName("예약 id로 조회에 실패하면 예외가 발생한다")
    @Test
    void test4() {
        // given
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reservationQueryService.getBy(reservationId))
                .isInstanceOf(ReservationNotFoundException.class);

        verify(reservationRepository).findById(reservationId);
    }
}
