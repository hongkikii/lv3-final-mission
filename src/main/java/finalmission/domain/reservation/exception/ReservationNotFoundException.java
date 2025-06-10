package finalmission.domain.reservation.exception;

import finalmission.common.exception.BusinessException;
import finalmission.common.exception.ErrorCode;

public class ReservationNotFoundException extends BusinessException {

    public ReservationNotFoundException() {
        super(ErrorCode.RESERVATION_NOT_FOUND);
    }
}
