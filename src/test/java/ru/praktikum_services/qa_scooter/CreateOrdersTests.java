package ru.praktikum_services.qa_scooter;

import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.praktikum_services.qa_scooter.model.Order;
import ru.praktikum_services.qa_scooter.model.OrderCreateResponse;
import ru.praktikum_services.qa_scooter.steps.Steps;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.notNullValue;
import static ru.praktikum_services.qa_scooter.generators.OrderGenerator.*;

public class CreateOrdersTests extends Steps {

    private String track;

    @ParameterizedTest
    @DisplayName("Создание заказа. Можно создать заказ со всеми параметрами")
    @MethodSource("orderData")
    public void createOrderWithAllParamsTest(List<String> color) {
        Order order = allParamsOrder(color);

        Response response = sendPostRequestV1Orders(order);

        response.then()
                .statusCode(201);

        track = response.as(OrderCreateResponse.class).getTrack();

        Response trackOrderResponse = sendGetRequestV1OrdersTrack(track);

        trackOrderResponse.then()
                .statusCode(200);
    }

    private static Stream<Arguments> orderData() {

        List<String> blackColor = new ArrayList<>();
        blackColor.add("BLACK");

        List<String> greyColor = new ArrayList<>();
        greyColor.add("GREY");

        List<String> twoColors = new ArrayList<>();
        twoColors.add("GREY");
        twoColors.add("BLACK");

        List<String> noColor = new ArrayList<>();

        return Stream.of(
                Arguments.of(blackColor),
                Arguments.of(greyColor),
                Arguments.of(twoColors),
                Arguments.of(noColor)
        );
    }

    @Test
    @DisplayName("Создание заказа. Можно создать заказ с обязательными параметрами")
    public void createOrderWithRequiredParamsTest() {
        Order order = requiredParamsRandomOrder();

        Response response = sendPostRequestV1Orders(order);

        response.then()
                .statusCode(201);

        track = response.as(OrderCreateResponse.class).getTrack();

        Response trackOrderResponse = sendGetRequestV1OrdersTrack(track);

        trackOrderResponse.then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Создание заказа. В ответ на создание заказа приходит его трек-номер")
    public void createOrderReturnsTrackTest() {
        Order order = requiredParamsRandomOrder();

        Response response = sendPostRequestV1Orders(order);

        response.then()
                .statusCode(201)
                .and()
                .assertThat().body("track", notNullValue());

        track = response.as(OrderCreateResponse.class).getTrack();
    }

    @AfterEach
    public void tearDown() {
        sendPutRequestV1OrdersCancel(track);
    }
}
