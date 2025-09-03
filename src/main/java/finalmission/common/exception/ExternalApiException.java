package finalmission.common.exception;

public class ExternalApiException extends BusinessException {

    public ExternalApiException() {
        super(ErrorCode.INTERNAL_SERVER_ERROR);
    }
}
