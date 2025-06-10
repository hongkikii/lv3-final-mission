package finalmission.domain.user.exception;

import finalmission.common.exception.BusinessException;
import finalmission.common.exception.ErrorCode;

public class UserNotFoundException extends BusinessException {

    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }
}
