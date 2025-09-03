package finalmission.domain.restaurant.exception;

import finalmission.common.exception.BusinessException;
import finalmission.common.exception.ErrorCode;

public class RestaurantNotAvailableException extends BusinessException {

    public RestaurantNotAvailableException() {
        super(ErrorCode.RESTAURANT_NOT_AVAILABLE);
    }
}
