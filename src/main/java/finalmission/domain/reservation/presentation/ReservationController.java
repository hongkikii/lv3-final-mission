package finalmission.domain.reservation.presentation;

import finalmission.common.dto.SuccessResponse;
import finalmission.domain.reservation.dto.CreateReservationRequest;
import finalmission.domain.reservation.dto.DetailReservationResponse;
import finalmission.domain.reservation.dto.ModifyReservationRequest;
import finalmission.domain.reservation.dto.ReservationResponse;
import finalmission.domain.reservation.application.ReservationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(("/api/v1/reservations"))
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<SuccessResponse<ReservationResponse>> create(
            @RequestBody CreateReservationRequest request
    ) {
        return SuccessResponse.from(reservationService.create(request))
                .asHttp(HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<SuccessResponse<List<ReservationResponse>>> getAll() {

        return SuccessResponse.from(reservationService.getAll())
                .asHttp(HttpStatus.OK);
    }

    @GetMapping("/detail")
    public ResponseEntity<SuccessResponse<DetailReservationResponse>> getDetailBy(
            @RequestParam long reservationId, @RequestParam long userId
    ) {
        return SuccessResponse.from(reservationService.getDetail(reservationId, userId))
                .asHttp(HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<SuccessResponse<ReservationResponse>> update(
            @RequestBody ModifyReservationRequest request
    ) {
        return SuccessResponse.from(reservationService.modify(request))
                .asHttp(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(
            @RequestParam long reservationId, @RequestParam long userId
    ) {
        reservationService.delete(reservationId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }
}
