package finalmission.domain.reservation.dto;

import java.time.LocalDate;

public record CreateReservationRequest(long userId, long restaurantId, long timeId, LocalDate date) {
}
