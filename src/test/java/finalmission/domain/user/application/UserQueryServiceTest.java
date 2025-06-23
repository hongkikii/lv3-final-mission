package finalmission.domain.user.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import finalmission.domain.user.domain.User;
import finalmission.domain.user.domain.UserFixture;
import finalmission.domain.user.exception.UserNotFoundException;
import finalmission.domain.user.infrastructure.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserQueryServiceTest {

    @InjectMocks
    private UserQueryService userQueryService;

    @Mock
    private UserRepository userRepository;

    private final Long userId = 1L;
    private User user;

    @BeforeEach
    void setUp() {
        user = UserFixture.createUser(userId, "mimi");
    }

    @DisplayName("사용자 id로 정상적으로 조회한다")
    @Test
    void test1() {
        // given
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when
        User result = userQueryService.getBy(userId);

        // then
        assertThat(result).isEqualTo(user);
        verify(userRepository).findById(userId);
    }

    @DisplayName("사용자 id로 조회에 실패하면 예외가 발생한다")
    @Test
    void test2() {
        // given
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userQueryService.getBy(userId))
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository).findById(userId);
    }
}
