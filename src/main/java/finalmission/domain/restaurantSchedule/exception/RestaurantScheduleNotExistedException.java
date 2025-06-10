package finalmission.domain.restaurantSchedule.exception;

import finalmission.common.exception.BusinessException;
import finalmission.common.exception.ErrorCode;

public class RestaurantScheduleNotExistedException extends BusinessException {

    public RestaurantScheduleNotExistedException() {
        super(ErrorCode.RESTAURANT_SCHEDULE_NOT_EXISTED);
    }
}
