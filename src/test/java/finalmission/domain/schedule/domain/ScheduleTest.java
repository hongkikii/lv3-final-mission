package finalmission.domain.schedule.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ScheduleTest {

    @DisplayName("레스토랑 이용이 불가한 일정일 경우 true를 반환한다")
    @Test
    void test1() {
        //given
        Schedule notAvailableSchedule = Schedule.builder()
                .isAvailable(false)
                .build();

        //when
        boolean result = notAvailableSchedule.isNotAvailable();

        //then
        assertThat(result).isTrue();
    }

    @DisplayName("레스토랑 이용이 가능한 일정일 경우 false를 반환한다")
    @Test
    void test2() {
        //given
        Schedule availableSchedule = Schedule.builder()
                .isAvailable(true)
                .build();

        //when
        boolean result = availableSchedule.isNotAvailable();

        //then
        assertThat(result).isFalse();
    }
}
