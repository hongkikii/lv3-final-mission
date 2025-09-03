package finalmission.domain.schedule.exception;

import finalmission.common.exception.BusinessException;
import finalmission.common.exception.ErrorCode;

public class ScheduleNotExistedException extends BusinessException {

    public ScheduleNotExistedException() {
        super(ErrorCode.RESTAURANT_SCHEDULE_NOT_EXISTED);
    }
}
