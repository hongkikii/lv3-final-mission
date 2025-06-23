package finalmission.domain.reservation.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ModifyReservationRequest(@NotNull Long reservationId,
                                       @NotNull Long userId,
                                       @NotNull LocalDate date,
                                       @NotNull Long timeId) {
}
