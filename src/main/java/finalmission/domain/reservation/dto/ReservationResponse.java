package finalmission.domain.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import finalmission.domain.reservation.domain.Reservation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationResponse(@NotNull Long reservationId,
                                  @NotBlank String restaurantName,
                                  @NotNull LocalDate date,
                                  @NotNull @JsonFormat(pattern = "HH:mm") LocalTime startAt) {

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(reservation.getId(),
                reservation.getSchedule().getRestaurant().getName(),
                reservation.getSchedule().getDate(),
                reservation.getSchedule().getTime().getStartAt());
    }
}
