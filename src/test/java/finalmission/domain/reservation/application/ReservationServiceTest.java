package finalmission.domain.reservation.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import finalmission.domain.reservation.domain.Reservation;
import finalmission.domain.reservation.domain.ReservationFixture;
import finalmission.domain.reservation.dto.CreateReservationRequest;
import finalmission.domain.reservation.dto.DetailReservationResponse;
import finalmission.domain.reservation.dto.ModifyReservationRequest;
import finalmission.domain.reservation.dto.ReservationResponse;
import finalmission.domain.reservation.exception.HolidayException;
import finalmission.domain.reservation.exception.InvalidReservationUserException;
import finalmission.domain.reservation.exception.PastDateException;
import finalmission.domain.reservation.exception.ReservationAlreadyExistedException;
import finalmission.domain.reservation.infrastructure.ReservationRepository;
import finalmission.domain.restaurant.domain.Restaurant;
import finalmission.domain.restaurant.domain.RestaurantFixture;
import finalmission.domain.schedule.application.ScheduleQueryService;
import finalmission.domain.schedule.domain.Schedule;
import finalmission.domain.schedule.domain.ScheduleFixture;
import finalmission.domain.time.domain.Time;
import finalmission.domain.time.domain.TimeFixture;
import finalmission.domain.user.application.UserQueryService;
import finalmission.domain.user.domain.User;
import finalmission.domain.user.domain.UserFixture;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ScheduleQueryService scheduleQueryService;

    @Mock
    private ReservationQueryService reservationQueryService;

    @Mock
    private UserQueryService userQueryService;

    @Mock
    private HolidayApiClient holidayApiClient;

    @Mock
    private Clock clock;

    private final Long userId = 1L;
    private final Long restaurantId = 1L;
    private final Long reservationId = 1L;
    private final Long timeId = 1L;
    private final Long scheduleId = 1L;
    private final String userName = "mimi";
    private final String restaurantName = "restaurant";
    private final LocalDate today = LocalDate.of(2025, 6, 10);
    private final LocalDate validReservationDate = LocalDate.of(2025, 6, 20);
    private final LocalTime validTime = LocalTime.of(10, 0);

    private User user;
    private Schedule schedule;
    private Reservation reservation;
    private Time time;
    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        user = UserFixture.createUser(userId, userName);

        time = TimeFixture.createTime(timeId, validTime);

        restaurant = RestaurantFixture.createRestaurant(restaurantId,  restaurantName);

        schedule = ScheduleFixture.createSchedule(scheduleId, validReservationDate, time, restaurant);

        reservation = ReservationFixture.createReservation(reservationId, user, schedule);
    }

    @DisplayName("예약을 정상적으로 생성한다")
    @Test
    void test1() {
        // given
        CreateReservationRequest request = new CreateReservationRequest(
                userId, restaurantId, timeId, validReservationDate);

        when(holidayApiClient.isHoliday(validReservationDate)).thenReturn(false);
        when(userQueryService.getBy(userId)).thenReturn(user);
        when(scheduleQueryService.getBy(restaurantId, timeId, validReservationDate)).thenReturn(schedule);
        when(reservationQueryService.isAlreadyExisted(scheduleId)).thenReturn(false);
        when(reservationRepository.save(ArgumentMatchers.any())).thenReturn(reservation);
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());
        when(clock.instant()).thenReturn(today.atStartOfDay(ZoneId.systemDefault()).toInstant());

        // when
        ReservationResponse response = reservationService.create(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.reservationId()).isEqualTo(reservationId);
        assertThat(response.restaurantName()).isEqualTo(restaurantName);
        assertThat(response.date()).isEqualTo(validReservationDate);
        assertThat(response.startAt()).isEqualTo(validTime);

        verify(holidayApiClient).isHoliday(validReservationDate);
        verify(userQueryService).getBy(userId);
        verify(scheduleQueryService).getBy(restaurantId, timeId, validReservationDate);
        verify(reservationQueryService).isAlreadyExisted(scheduleId);
        verify(reservationRepository).save(ArgumentMatchers.any());
    }

    @DisplayName("과거 날짜로 예약 시 예외가 발생한다")
    @Test
    void test2() {
        // given
        LocalDate pastDate = today.minusDays(1);
        CreateReservationRequest request = new CreateReservationRequest(
                userId, restaurantId, timeId, pastDate);
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());
        when(clock.instant()).thenReturn(today.atStartOfDay(ZoneId.systemDefault()).toInstant());

        // when & then
        assertThatThrownBy(() -> reservationService.create(request))
                .isInstanceOf(PastDateException.class);

        verify(holidayApiClient, never()).isHoliday(any());
        verify(userQueryService, never()).getBy(anyLong());
        verify(scheduleQueryService, never()).getBy(anyLong(), anyLong(), any());
    }

    @DisplayName("오늘 날짜로 예약 시 예외가 발생한다")
    @Test
    void test3() {
        // given
        CreateReservationRequest request = new CreateReservationRequest(
                userId, restaurantId, timeId, today);
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());
        when(clock.instant()).thenReturn(today.atStartOfDay(ZoneId.systemDefault()).toInstant());

        // when & then
        assertThatThrownBy(() -> reservationService.create(request))
                .isInstanceOf(PastDateException.class);

        verify(holidayApiClient, never()).isHoliday(any());
        verify(userQueryService, never()).getBy(anyLong());
    }

    @DisplayName("공휴일에 예약 시 예외가 발생한다")
    @Test
    void test4() {
        // given
        CreateReservationRequest request = new CreateReservationRequest(
                userId, restaurantId, timeId, validReservationDate);

        when(clock.getZone()).thenReturn(ZoneId.systemDefault());
        when(clock.instant()).thenReturn(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
        when(holidayApiClient.isHoliday(validReservationDate)).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> reservationService.create(request))
                .isInstanceOf(HolidayException.class);

        verify(holidayApiClient).isHoliday(validReservationDate);
        verify(userQueryService, never()).getBy(anyLong());
        verify(scheduleQueryService, never()).getBy(anyLong(), anyLong(), any());
    }

    @DisplayName("이미 예약된 스케줄에 예약 시 예외가 발생한다")
    @Test
    void test5() {
        // given
        CreateReservationRequest request = new CreateReservationRequest(
                userId, restaurantId, timeId, validReservationDate);

        when(clock.getZone()).thenReturn(ZoneId.systemDefault());
        when(clock.instant()).thenReturn(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
        when(holidayApiClient.isHoliday(validReservationDate)).thenReturn(false);
        when(userQueryService.getBy(userId)).thenReturn(user);
        when(scheduleQueryService.getBy(restaurantId, timeId, validReservationDate)).thenReturn(schedule);
        when(reservationQueryService.isAlreadyExisted(scheduleId)).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> reservationService.create(request))
                .isInstanceOf(ReservationAlreadyExistedException.class);

        verify(holidayApiClient).isHoliday(validReservationDate);
        verify(userQueryService).getBy(userId);
        verify(scheduleQueryService).getBy(restaurantId, timeId, validReservationDate);
        verify(reservationQueryService).isAlreadyExisted(scheduleId);
        verify(reservationRepository, never()).save(any());
    }

    @DisplayName("모든 예약을 정상적으로 조회한다")
    @Test
    void test6() {
        // given
        List<Reservation> reservations = Arrays.asList(
                reservation,
                ReservationFixture.createReservation(2L, user, schedule)
        );
        when(reservationQueryService.findAll()).thenReturn(reservations);

        // when
        List<ReservationResponse> responses = reservationService.getAll();

        // then
        assertThat(responses.size()).isEqualTo(2);

        verify(reservationQueryService).findAll();
    }

    @DisplayName("예약 상세 조회를 정상적으로 수행한다")
    @Test
    void test7() {
        // given
        when(reservationQueryService.getBy(reservationId)).thenReturn(reservation);

        // when
        DetailReservationResponse response = reservationService.getDetail(reservationId, userId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.reservationId()).isEqualTo(reservationId);
        assertThat(response.userName()).isEqualTo(userName);

        verify(reservationQueryService).getBy(reservationId);
    }

    @DisplayName("다른 사용자의 예약 상세 조회 시 예외가 발생한다")
    @Test
    void test8() {
        // given
        long invalidUserId = 999L;

        Reservation anotherUserReservation = spy(reservation);
        when(anotherUserReservation.notBelongTo(invalidUserId)).thenReturn(true);
        when(reservationQueryService.getBy(reservationId)).thenReturn(anotherUserReservation);

        // when & then
        assertThatThrownBy(() -> reservationService.getDetail(reservationId, invalidUserId))
                .isInstanceOf(InvalidReservationUserException.class);

        verify(reservationQueryService).getBy(reservationId);
        verify(anotherUserReservation).notBelongTo(invalidUserId);
    }

    @DisplayName("예약 수정을 정상적으로 수행한다")
    @Test
    void test9() {
        // given
        LocalDate newDate = validReservationDate.plusDays(1);
        ModifyReservationRequest request = new ModifyReservationRequest(
                reservationId, userId, newDate, timeId);

        long newScheduleId = 2L;
        Schedule newSchedule = ScheduleFixture.createSchedule(newScheduleId, newDate, time, restaurant);

        when(holidayApiClient.isHoliday(newDate)).thenReturn(false);
        when(reservationQueryService.getBy(reservationId)).thenReturn(reservation);
        when(scheduleQueryService.getBy(timeId, newDate)).thenReturn(newSchedule);
        when(reservationQueryService.isAlreadyExisted(newScheduleId)).thenReturn(false);
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());
        when(clock.instant()).thenReturn(today.atStartOfDay(ZoneId.systemDefault()).toInstant());

        // when
        ReservationResponse response = reservationService.modify(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.date()).isEqualTo(newDate);

        verify(holidayApiClient).isHoliday(newDate);
        verify(reservationQueryService).getBy(reservationId);
        verify(scheduleQueryService).getBy(timeId, newDate);
        verify(reservationQueryService).isAlreadyExisted(newScheduleId);
    }

    @DisplayName("다른 사용자가 예약 수정을 시도할 시 예외가 발생한다")
    @Test
    void test10() {
        // given
        LocalDate newDate = validReservationDate.plusDays(1);
        long invalidUserId = 999L;
        ModifyReservationRequest request = new ModifyReservationRequest(
                reservationId, invalidUserId, newDate, timeId);

        Reservation anotherUserReservation = spy(reservation);
        when(anotherUserReservation.notBelongTo(invalidUserId)).thenReturn(true);

        when(holidayApiClient.isHoliday(newDate)).thenReturn(false);
        when(reservationQueryService.getBy(reservationId)).thenReturn(anotherUserReservation);
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());
        when(clock.instant()).thenReturn(today.atStartOfDay(ZoneId.systemDefault()).toInstant());

        // when & then
        assertThatThrownBy(() -> reservationService.modify(request))
                .isInstanceOf(InvalidReservationUserException.class);

        verify(holidayApiClient).isHoliday(newDate);
        verify(reservationQueryService).getBy(reservationId);
        verify(anotherUserReservation).notBelongTo(invalidUserId);
        verify(scheduleQueryService, never()).getBy(anyLong(), any());
    }

    @DisplayName("이미 예약된 스케줄로 수정 시 예외가 발생한다")
    @Test
    void test13() {
        // given
        LocalDate newDate = validReservationDate.plusDays(1);
        ModifyReservationRequest request = new ModifyReservationRequest(
                reservationId, userId, newDate, timeId);

        long newScheduleId = 2L;
        Schedule newSchedule = ScheduleFixture.createSchedule(newScheduleId, newDate, time, restaurant);

        when(holidayApiClient.isHoliday(newDate)).thenReturn(false);
        when(reservationQueryService.getBy(reservationId)).thenReturn(reservation);
        when(scheduleQueryService.getBy(timeId, newDate)).thenReturn(newSchedule);
        when(reservationQueryService.isAlreadyExisted(newScheduleId)).thenReturn(true);
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());
        when(clock.instant()).thenReturn(today.atStartOfDay(ZoneId.systemDefault()).toInstant());

        // when & then
        assertThatThrownBy(() -> reservationService.modify(request))
                .isInstanceOf(ReservationAlreadyExistedException.class);

        verify(holidayApiClient).isHoliday(newDate);
        verify(reservationQueryService).getBy(reservationId);
        verify(scheduleQueryService).getBy(timeId, newDate);
        verify(reservationQueryService).isAlreadyExisted(newScheduleId);
    }

    @DisplayName("예약을 정상적으로 삭제한다")
    @Test
    void test11() {
        // given
        when(reservationQueryService.getBy(reservationId)).thenReturn(reservation);

        // when
        reservationService.delete(reservationId, userId);

        // then
        verify(reservationQueryService).getBy(reservationId);
        verify(reservationRepository).delete(reservation);
    }

    @Test
    @DisplayName("다른 사용자가 예약 삭제를 시도할 시 예외가 발생한다")
    void test12() {
        // given
        long invalidUserId = 999L;

        Reservation anotherUserReservation = spy(reservation);
        when(anotherUserReservation.notBelongTo(invalidUserId)).thenReturn(true);
        when(reservationQueryService.getBy(reservationId)).thenReturn(anotherUserReservation);

        // when & then
        assertThatThrownBy(() -> reservationService.delete(reservationId, invalidUserId))
                .isInstanceOf(InvalidReservationUserException.class);

        verify(reservationQueryService).getBy(reservationId);
        verify(anotherUserReservation).notBelongTo(invalidUserId);
        verify(reservationRepository, never()).delete(any());
    }
}
