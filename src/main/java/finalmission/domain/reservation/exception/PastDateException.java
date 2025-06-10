package finalmission.domain.reservation.exception;

import finalmission.common.exception.BusinessException;
import finalmission.common.exception.ErrorCode;

public class PastDateException extends BusinessException {

    public PastDateException() {
        super(ErrorCode.PAST_DATE);
    }
}
