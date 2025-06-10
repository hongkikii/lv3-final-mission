package finalmission.domain.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import finalmission.domain.reservation.domain.Reservation;
import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationResponse(long reservationId,
                                  String restaurantName,
                                  LocalDate date,
                                  @JsonFormat(pattern = "HH:mm") LocalTime startAt) {

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(reservation.getId(),
                reservation.getRestaurantSchedule().getRestaurant().getName(),
                reservation.getRestaurantSchedule().getDate(),
                reservation.getRestaurantSchedule().getTime().getStartAt());
    }
}
