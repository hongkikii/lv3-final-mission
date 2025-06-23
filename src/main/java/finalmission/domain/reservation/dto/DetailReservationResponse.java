package finalmission.domain.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import finalmission.domain.reservation.domain.Reservation;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record DetailReservationResponse(Long reservationId,
                                        String restaurantName,
                                        LocalDate date,
                                        @JsonFormat(pattern = "HH:mm") LocalTime startAt,
                                        String userName,
                                        @JsonFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime createdAt) {

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
