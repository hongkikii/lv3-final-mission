package finalmission.domain.schedule.domain;

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
}
