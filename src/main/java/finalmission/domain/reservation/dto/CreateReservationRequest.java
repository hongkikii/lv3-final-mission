package finalmission.domain.reservation.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record CreateReservationRequest(@NotNull Long userId,
                                       @NotNull Long restaurantId,
                                       @NotNull Long timeId,
                                       @NotNull LocalDate date) {
}
