package finalmission.domain.user.domain;

public class UserFixture {

    public static User createUser(Long id, String name) {
        return User.builder()
                .id(id)
                .name(name)
                .build();
    }
}
