package finalmission.domain.user.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserTest {

    @DisplayName("id가 다를 경우 true를 반환한다")
    @Test
    void test1() {
        //given
        Long id = 1L;
        Long differentId = 2L;
        User user1 = User.builder()
                .id(id)
                .build();

        //when
        boolean result = user1.isNotSameBy(differentId);

        //then
        assertThat(result).isTrue();
    }

    @DisplayName("id가 같을 경우 false를 반환한다")
    @Test
    void test2() {
        //given
        Long id = 1L;
        User user1 = User.builder()
                .id(id)
                .build();

        //when
        boolean result = user1.isNotSameBy(id);

        //then
        assertThat(result).isFalse();
    }
}
