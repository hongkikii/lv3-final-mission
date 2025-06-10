package finalmission.domain.reservation.presentation;

import finalmission.common.dto.SuccessResponse;
import finalmission.domain.reservation.dto.CreateReservationRequest;
import finalmission.domain.reservation.dto.CreateReservationResponse;
import finalmission.domain.reservation.application.ReservationCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(("/api/v1/reservations"))
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationCommandService reservationCommandService;

    @PostMapping
    public ResponseEntity<SuccessResponse<CreateReservationResponse>> createReservation(
            @RequestBody CreateReservationRequest request) {

        return SuccessResponse.from(reservationCommandService.createReservation(request))
                .asHttp(HttpStatus.CREATED);
    }
}
