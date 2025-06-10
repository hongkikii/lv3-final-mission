package finalmission.domain.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import finalmission.domain.reservation.domain.Reservation;
import java.time.LocalTime;

public record CreateReservationResponse(long id,
                                        String restaurantName,
                                        @JsonFormat(pattern = "HH:mm") LocalTime reservationTime) {

    public static CreateReservationResponse from(Reservation reservation) {
        return new CreateReservationResponse(reservation.getId(),
                reservation.getRestaurantSchedule().getRestaurant().getName(),
                reservation.getRestaurantSchedule().getTime().getStartAt());
    }
}
