package finalmission.domain.reservation.integration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import finalmission.domain.reservation.application.HolidayApiClient;
import finalmission.domain.reservation.dto.CreateReservationRequest;
import finalmission.domain.reservation.dto.ModifyReservationRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationIntegrationTest {

    @LocalServerPort
    private int port;

    @MockitoBean
    HolidayApiClient holidayApiClient;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        when(holidayApiClient.isHoliday(any())).thenReturn(false);
    }

    @DisplayName("예약을 생성한다")
    @Test
    void test1() {
        // given
        CreateReservationRequest request = new CreateReservationRequest(
                1L,
                2L,
                1L,
                LocalDate.of(2025, 6, 12)
        );

        // when & then
        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/reservations")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("data.reservationId", equalTo(4));
    }

    @DisplayName("모든 예약을 조회한다")
    @Test
    void test2() {
        // when & then
        given()
                .when()
                .get("/api/v1/reservations/all")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("data.size()", equalTo(3));
    }

    @DisplayName("예약을 수정한다")
    @Test
    void test4() {
        // given
        ModifyReservationRequest request = new ModifyReservationRequest(
                3L,
                2L,
                LocalDate.of(2025, 6, 14),
                1L
        );

        // when & then
        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .patch("/api/v1/reservations")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("data.date", equalTo("2025-06-14"));
    }

    @DisplayName("예약을 삭제한다")
    @Test
    void test5() {
        // when & then
        given()
                .queryParam("reservationId", 3L)
                .queryParam("userId", 2L)
                .when()
                .delete("/api/v1/reservations")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
