package finalmission.domain.reservation.infrastructure;

import finalmission.domain.reservation.application.HolidayApiClient;
import java.time.LocalDate;
import org.springframework.stereotype.Component;

@Component
public class HolidayApiRestClient implements HolidayApiClient {

    @Override
    public boolean isHoliday(final LocalDate date) {
        return false;
    }
}
