package finalmission.domain.reservation.domain;

import finalmission.domain.schedule.domain.Schedule;
import finalmission.domain.user.domain.User;
import java.time.LocalDateTime;

public class ReservationFixture {
    public static Reservation createReservation(Long id, User user, Schedule schedule) {
        return Reservation.builder()
                .id(id)
                .user(user)
                .schedule(schedule)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
