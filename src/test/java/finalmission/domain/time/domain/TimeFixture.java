package finalmission.domain.time.domain;

import java.time.LocalTime;

public class TimeFixture {
    public static Time createTime(Long id) {
        return Time.builder()
                .id(id)
                .startAt(LocalTime.now())
                .build();
    }

    public static Time createTime(Long id, LocalTime startAt) {
        return Time.builder()
                .id(id)
                .startAt(startAt)
                .build();
    }
}
