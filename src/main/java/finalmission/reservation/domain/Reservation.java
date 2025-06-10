package finalmission.reservation.domain;

import finalmission.restaurantTime.domain.RestaurantTime;
import finalmission.user.domain.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "restaurant_time_id")
    private RestaurantTime restaurantTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
