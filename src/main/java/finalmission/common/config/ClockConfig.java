package finalmission.common.config;

import jakarta.annotation.PostConstruct;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.TimeZone;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClockConfig {

    @Bean
    public Clock clock() {
        Instant now = Instant.now(Clock.fixed(
                Instant.parse("2025-06-10T10:00:00Z"),
                ZoneOffset.UTC
        ));
        return Clock.fixed(now, ZoneOffset.UTC);
    }

    @PostConstruct
    void start() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }
}
