package finalmission.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

    // RestaurantTime
    RESTAURANT_TIME_NOT_EXISTED(HttpStatus.NOT_FOUND, "RESTAURANT_TIME_NOT_EXISTED", "예약 데이터가 유효하지 않습니다."),

    // Reservation
    RESERVATION_ALREADY_EXISTED(HttpStatus.CONFLICT, "RESERVATION_ALREADY_EXISTED", "이미 예약이 존재합니다."),
    PAST_DATE(HttpStatus.BAD_REQUEST, "PAST_DATE", "예약은 오늘 이후로만 가능합니다."),
    HOLIDAY(HttpStatus.BAD_REQUEST, "HOLIDAY" , "공휴일은 예약 불가입니다."),
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "RESERVATION_NOT_FOUND", "예약이 존재하지 않습니다."),
    INVALID_RESERVATION_USER(HttpStatus.BAD_REQUEST, "INVALID_RESERVATION_USER", "예약한 사용자가 아닙니다."),

    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_NOT_FOUND" , "사용자가 존재하지 않습니다."),

    // RESTAURANT
    RESTAURANT_NOT_AVAILABLE(HttpStatus.BAD_REQUEST, "RESTAURANT_NOT_AVAILABLE", "해당 시간대에 식당 이용이 불가능합니다."), ;



    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
