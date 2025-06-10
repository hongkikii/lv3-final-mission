package finalmission.domain.reservation.infrastructure.holiday;

public record HolidayResponse(String resultCode,
                              String resultMsg,
                              Item Item,
                              String numOfRows,
                              String pageNo,
                              String totalCount) {
}
