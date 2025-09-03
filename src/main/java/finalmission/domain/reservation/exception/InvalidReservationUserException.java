package finalmission.domain.reservation.exception;

import finalmission.common.exception.BusinessException;
import finalmission.common.exception.ErrorCode;

public class InvalidReservationUserException extends BusinessException {

    public InvalidReservationUserException() {
        super(ErrorCode.INVALID_RESERVATION_USER);
    }
}
