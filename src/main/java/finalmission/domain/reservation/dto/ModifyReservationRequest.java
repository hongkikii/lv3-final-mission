package finalmission.domain.reservation.dto;

import java.time.LocalDate;

public record ModifyReservationRequest(long reservationId,
                                       long userId,
                                       LocalDate date,
                                       long timeId) {
}
