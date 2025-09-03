package finalmission.domain.reservation.domain;

import static org.assertj.core.api.Assertions.*;

import finalmission.domain.schedule.domain.Schedule;
import finalmission.domain.schedule.domain.ScheduleFixture;
import finalmission.domain.user.domain.User;
import finalmission.domain.user.domain.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTest {

    @DisplayName("예약한 사용자 id가 다를 경우 true를 반환한다")
    @Test
    void test1() {
        //given
        User mimi = UserFixture.createUser(1L, "mimi");
        User cookie = UserFixture.createUser(2L, "cookie");
        Reservation mimiReservation = Reservation.builder()
                .id(1L)
                .user(mimi)
                .build();

        //when
        boolean result = mimiReservation.notBelongTo(cookie.getId());

        //then
        assertThat(result).isTrue();
    }

    @DisplayName("예약한 사용자 id가 같을 경우 false를 반환한다")
    @Test
    void test2() {
        //given
        User mimi = UserFixture.createUser(1L, "mimi");
        Reservation mimiReservation = Reservation.builder()
                .id(1L)
                .user(mimi)
                .build();

        //when
        boolean result = mimiReservation.notBelongTo(mimi.getId());

        //then
        assertThat(result).isFalse();
    }

    @DisplayName("스케줄을 정상적으로 변경한다")
    @Test
    void test3() {
        //given
        Schedule originalSchedule = ScheduleFixture.createSchedule(1L);
        Schedule newSchedule = ScheduleFixture.createSchedule(2L);
        Reservation reservation = Reservation.builder()
                .schedule(originalSchedule)
                .build();

        //when
        reservation.changeSchedule(newSchedule);

        //then
        assertThat(reservation.getSchedule()).isEqualTo(newSchedule);
    }
}
