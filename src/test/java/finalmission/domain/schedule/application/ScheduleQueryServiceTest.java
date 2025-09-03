package finalmission.domain.schedule.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import finalmission.domain.restaurant.domain.Restaurant;
import finalmission.domain.restaurant.domain.RestaurantFixture;
import finalmission.domain.restaurant.exception.RestaurantNotAvailableException;
import finalmission.domain.schedule.domain.Schedule;
import finalmission.domain.schedule.domain.ScheduleFixture;
import finalmission.domain.schedule.exception.ScheduleNotExistedException;
import finalmission.domain.schedule.infrastructure.ScheduleRepository;
import finalmission.domain.time.domain.Time;
import finalmission.domain.time.domain.TimeFixture;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ScheduleQueryServiceTest {

    @InjectMocks
    private ScheduleQueryService scheduleQueryService;

    @Mock
    private ScheduleRepository scheduleRepository;

    private final long restaurantId = 1L;
    private final long timeId = 1L;
    private final LocalDate date = LocalDate.of(2025, 6, 20);

    private Schedule availableSchedule;
    private Schedule unavailableSchedule;

    @BeforeEach
    void setUp() {
        Time time = TimeFixture.createTime(timeId, LocalTime.of(10, 0));
        Restaurant restaurant = RestaurantFixture.createRestaurant(restaurantId, "restaurant");
        availableSchedule = ScheduleFixture.createSchedule(1L, date, time, restaurant);
        unavailableSchedule = ScheduleFixture.createUnavailableSchedule(2L, date, time, restaurant);
    }

    @DisplayName("식당 id, 시간 id, 날짜로 스케줄을 정상적으로 조회한다")
    @Test
    void test1() {
        // given
        when(scheduleRepository.findByRestaurant_idAndTime_idAndDate(restaurantId, timeId, date))
                .thenReturn(Optional.of(availableSchedule));

        // when
        Schedule result = scheduleQueryService.getBy(restaurantId, timeId, date);

        // then
        assertThat(result).isEqualTo(availableSchedule);
        verify(scheduleRepository).findByRestaurant_idAndTime_idAndDate(restaurantId, timeId, date);
    }

    @DisplayName("식당 id, 시간 id, 날짜로 조회한 스케줄이 존재하지 않을 시 예외가 발생한다")
    @Test
    void test2() {
        // given
        when(scheduleRepository.findByRestaurant_idAndTime_idAndDate(restaurantId, timeId, date))
                .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> scheduleQueryService.getBy(restaurantId, timeId, date))
                .isInstanceOf(ScheduleNotExistedException.class);

        verify(scheduleRepository).findByRestaurant_idAndTime_idAndDate(restaurantId, timeId, date);
    }

    @DisplayName("식당 id, 시간 id, 날짜로 조회한 스케줄을 이용할 수 없는 경우 예외가 발생한다")
    @Test
    void test3() {
        // given
        when(scheduleRepository.findByRestaurant_idAndTime_idAndDate(restaurantId, timeId, date))
                .thenReturn(Optional.of(unavailableSchedule));

        // when & then
        assertThatThrownBy(() -> scheduleQueryService.getBy(restaurantId, timeId, date))
                .isInstanceOf(RestaurantNotAvailableException.class);

        verify(scheduleRepository).findByRestaurant_idAndTime_idAndDate(restaurantId, timeId, date);
    }

    @DisplayName("시간 id와 날짜로 스케줄을 정상적으로 조회한다")
    @Test
    void test4() {
        // given
        when(scheduleRepository.findByTime_idAndDate(timeId, date))
                .thenReturn(Optional.of(availableSchedule));

        // when
        Schedule result = scheduleQueryService.getBy(timeId, date);

        // then
        assertThat(result).isEqualTo(availableSchedule);
        verify(scheduleRepository).findByTime_idAndDate(timeId, date);
    }

    @DisplayName("시간 id와 날짜에 해당하는 스케줄이 존재하지 않으면 예외가 발생한다")
    @Test
    void test5() {
        // given
        when(scheduleRepository.findByTime_idAndDate(timeId, date))
                .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> scheduleQueryService.getBy(timeId, date))
                .isInstanceOf(ScheduleNotExistedException.class);

        verify(scheduleRepository).findByTime_idAndDate(timeId, date);
    }

    @DisplayName("시간 id와 날짜로 조회한 스케줄을 이용할 수 없는 경우 예외가 발생한다")
    @Test
    void test6() {
        // given
        when(scheduleRepository.findByTime_idAndDate(timeId, date))
                .thenReturn(Optional.of(unavailableSchedule));

        // when & then
        assertThatThrownBy(() -> scheduleQueryService.getBy(timeId, date))
                .isInstanceOf(RestaurantNotAvailableException.class);

        verify(scheduleRepository).findByTime_idAndDate(timeId, date);
    }
}
