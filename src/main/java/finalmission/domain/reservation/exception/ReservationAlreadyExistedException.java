package finalmission.domain.reservation.exception;

import finalmission.common.exception.BusinessException;
import finalmission.common.exception.ErrorCode;

public class ReservationAlreadyExistedException extends BusinessException {

    public ReservationAlreadyExistedException() {
        super(ErrorCode.RESERVATION_ALREADY_EXISTED);
    }
}
