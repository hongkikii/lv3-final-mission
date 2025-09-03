package finalmission.domain.reservation.exception;

import finalmission.common.exception.BusinessException;
import finalmission.common.exception.ErrorCode;

public class HolidayException extends BusinessException {

    public HolidayException() {
        super(ErrorCode.HOLIDAY);
    }
}
