package finalmission.domain.reservation.infrastructure.holiday;

import finalmission.domain.reservation.application.HolidayApiClient;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Component
@RequiredArgsConstructor
@Slf4j
public class HolidayApiClientImpl implements HolidayApiClient {

    @Value("${api.holiday.secret-key}")
    private String secretKey;

    private final RestClient holidayRestClient;

    @Override
    public boolean isHoliday(final LocalDate date) {
        return false;
    }


    public HolidayResponse getHoliday(LocalDate date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM");
            String month = date.format(formatter);
            return holidayRestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("solYear", date.getYear())
                            .queryParam("solMonth", month)
                            .queryParam("ServiceKey", secretKey)
                            .queryParam("_type", "json")
                            .build())
                    .retrieve()
                    .body(HolidayResponse.class);
        }
        catch (RestClientResponseException e) {
            log.error(e.getResponseBodyAsString());
            throw new ExternalApiException();
        }
    }
}

