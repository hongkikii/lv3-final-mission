package finalmission.domain.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import finalmission.domain.reservation.domain.Reservation;
import java.time.LocalTime;

public record ReservationResponse(long id,
                                  String restaurantName,
                                  @JsonFormat(pattern = "HH:mm") LocalTime reservationTime) {

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(reservation.getId(),
                reservation.getRestaurantSchedule().getRestaurant().getName(),
                reservation.getRestaurantSchedule().getTime().getStartAt());
    }
}
