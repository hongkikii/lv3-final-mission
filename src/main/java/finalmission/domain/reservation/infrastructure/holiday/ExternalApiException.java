package finalmission.domain.reservation.infrastructure.holiday;

import finalmission.common.exception.BusinessException;
import finalmission.common.exception.ErrorCode;

public class ExternalApiException extends BusinessException {

    public ExternalApiException() {
        super(ErrorCode.INTERNAL_SERVER_ERROR);
    }
}
