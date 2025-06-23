package finalmission.domain.schedule.domain;

import finalmission.domain.restaurant.domain.Restaurant;
import finalmission.domain.time.domain.Time;
import finalmission.domain.time.domain.TimeFixture;
import java.time.LocalDate;

public class ScheduleFixture {
    public static Schedule createSchedule(Long id) {
        return Schedule.builder()
                .id(id)
                .date(LocalDate.now())
                .time(TimeFixture.createTime(1L))
                .build();
    }

    public static Schedule createSchedule(Long id, LocalDate date, Time time, Restaurant restaurant) {
        return Schedule.builder()
                .id(id)
                .date(date)
                .time(time)
                .restaurant(restaurant)
                .isAvailable(true)
                .build();
    }

    public static Schedule createUnavailableSchedule(Long id, LocalDate date, Time time, Restaurant restaurant) {
        return Schedule.builder()
                .id(id)
                .date(date)
                .time(time)
                .restaurant(restaurant)
                .isAvailable(false)
                .build();
    }
}
