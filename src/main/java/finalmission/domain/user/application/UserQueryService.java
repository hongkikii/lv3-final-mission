package finalmission.domain.user.application;

import finalmission.domain.user.domain.User;
import finalmission.domain.user.exception.UserNotFoundException;
import finalmission.domain.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserQueryService {

    private final UserRepository userRepository;

    public User getBy(Long id) {
        return userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }
}
