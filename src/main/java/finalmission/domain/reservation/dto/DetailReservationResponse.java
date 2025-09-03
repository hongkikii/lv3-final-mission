package finalmission.domain.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import finalmission.domain.reservation.domain.Reservation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record DetailReservationResponse(@NotNull Long reservationId,
                                        @NotBlank String restaurantName,
                                        @NotNull LocalDate date,
                                        @NotNull @JsonFormat(pattern = "HH:mm") LocalTime startAt,
                                        @NotBlank String userName,
                                        @NotNull @JsonFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime createdAt) {

    public static DetailReservationResponse from(final Reservation reservation) {
        return new DetailReservationResponse(
                reservation.getId(),
                reservation.getSchedule().getRestaurant().getName(),
                reservation.getSchedule().getDate(),
                reservation.getSchedule().getTime().getStartAt(),
                reservation.getUser().getName(),
                reservation.getCreatedAt()
        );
    }
}
