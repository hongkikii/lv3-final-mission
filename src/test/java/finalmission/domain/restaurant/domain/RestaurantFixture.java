package finalmission.domain.restaurant.domain;

public class RestaurantFixture {
    public static Restaurant createRestaurant(Long id,  String name) {
        return Restaurant.builder()
                .id(id)
                .name(name)
                .build();
    }
}
