package finalmission.domain.reservation.domain;

import finalmission.domain.schedule.domain.Schedule;
import finalmission.domain.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reservations")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Reservation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "restaurant_schedule_id")
    private Schedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    public Reservation(Schedule schedule, User user) {
        this.schedule = schedule;
        this.user = user;
        this.createdAt = LocalDateTime.now();
    }

    public boolean notBelongTo(final long userId) {
        return user.isNotSameBy(userId);
    }

    public void changeSchedule(Schedule schedule) {
        this.schedule = schedule;
    }
}
