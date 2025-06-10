package finalmission.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient holidayRestClient() {
        return RestClient.builder()
                .baseUrl("http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo")
                .build();
    }
}
