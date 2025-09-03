package finalmission.domain.reservation.application;

import java.time.LocalDate;

public interface HolidayApiClient {
    boolean isHoliday(LocalDate date);
}
