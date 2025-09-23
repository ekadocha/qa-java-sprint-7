package ru.praktikum_services.qa_scooter;

import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.praktikum_services.qa_scooter.model.*;
import ru.praktikum_services.qa_scooter.model.Order;
import ru.praktikum_services.qa_scooter.steps.Steps;

import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static ru.praktikum_services.qa_scooter.generators.CourierGenerator.randomCourier;
import static ru.praktikum_services.qa_scooter.generators.OrderGenerator.requiredParamsRandomOrder;

public class OrderListTests extends Steps {

    private String courierId;

    @BeforeEach
    public void createCourier() {
        Courier courier = randomCourier();

        Response response = sendPostRequestV1Courier(courier);

        response.then()
                .statusCode(201);

        Response loginResponse = sendPostRequestV1CourierLogin(courier);
        courierId = loginResponse.as(CourierCredsResponse.class).getId();

        loginResponse.then()
                .statusCode(200);

    }

    @Test
    @DisplayName("Получение списка заказов. Можно получить список всех заказов в системе")
    public void getFullOrderListTest() {
        Response response = sendGetRequestV1Orders();

        response.then()
                .statusCode(200)
                .and()
                .assertThat().body("orders", notNullValue())
                .and()
                .assertThat().body("pageInfo.page", equalTo(0))
                .assertThat().body("pageInfo.limit", equalTo(30))
                .and()
                .assertThat().body("availableStations", notNullValue());
    }

    @Test
    @DisplayName("Получение списка заказов. Можно получить пустой список заказов конкретного курьера")
    public void getEmptyOrderListByCourierIdTest() {
        Response response = sendGetRequestV1OrdersWithCourierIdParam(courierId);

        response.then()
                .statusCode(200)
                .and()
                .assertThat().body("orders", equalTo(Collections.emptyList()))
                .and()
                .assertThat().body("pageInfo.page", equalTo(0))
                .assertThat().body("pageInfo.limit", equalTo(30))
                .and()
                .assertThat().body("availableStations", equalTo(Collections.emptyList()));
    }

    @Test
    @DisplayName("Получение списка заказов. Можно получить список активных и завершенных заказов конкретного курьера")
    public void getNotEmptyOrderListByCourierIdTest() {
        //создать заказ 1 и 2
        Order order1 = requiredParamsRandomOrder();
        Response response_order1 = sendPostRequestV1Orders(order1);
        response_order1.then()
                .statusCode(201);
        String track1 = response_order1.as(OrderCreateResponse.class).getTrack();

        Response trackOrder1Response = sendGetRequestV1OrdersTrack(track1);
        int orderId1 = trackOrder1Response.as(TrackOrderResponse.class).getOrder().getId();

        Order order2 = requiredParamsRandomOrder();
        Response response_order2 = sendPostRequestV1Orders(order2);
        response_order2.then()
                .statusCode(201);
        String track2 = response_order2.as(OrderCreateResponse.class).getTrack();

        Response trackOrder2Response = sendGetRequestV1OrdersTrack(track2);
        int orderId2 = trackOrder2Response.as(TrackOrderResponse.class).getOrder().getId();

        //принять заказ 1 и 2
        Response response_accept1 = sendPutRequestV1OrdersAcceptId(orderId1, courierId);
        response_accept1.then()
                .statusCode(200);

        Response response_accept2 = sendPutRequestV1OrdersAcceptId(orderId2, courierId);
        response_accept2.then()
                .statusCode(200);
        //завершить заказ 1
        Response response_finish1 = sendPutRequestV1OrdersFinishId(orderId1);
        response_finish1.then()
                .statusCode(200);

        Response response = sendGetRequestV1OrdersWithCourierIdParam(courierId);

        response.then()
                .statusCode(200)
                .and()
                .assertThat().body("orders.id", hasItem(orderId1)) // не разобралась, почему в теле четыре записи
                .assertThat().body("orders.id", hasItem(orderId2))
                .and()
                .assertThat().body("pageInfo.page", equalTo(0))
                .assertThat().body("pageInfo.limit", equalTo(30))
                .and()
                .assertThat().body("availableStations", equalTo(Collections.emptyList()));
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 1, 10, 30, 31, 100})
    @DisplayName("Получение списка заказов. Можно задать количество заказов, отображаемых на странице")
    public void limitOrderListTest(int limit) {
        Response response = sendGetRequestV1OrdersWithLimitParam(limit);

        response.then()
                .statusCode(200)
                .and()
                .assertThat().body("orders.size()", is(limit))
                .and()
                .assertThat().body("pageInfo.page", equalTo(0))
                .assertThat().body("pageInfo.limit", equalTo(limit))
                .and()
                .assertThat().body("availableStations", notNullValue());
        // тест ловит ошибку: если нельзя задать лимит больше 30
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 1, 0, 10})
    @DisplayName("Получение списка заказов. Можно выбрать номер отображаемой страницы")
    public void changePageOrderListTest(int page) {
        Response response = sendGetRequestV1OrdersWithPageParam(page);

        response.then()
                .statusCode(200)
                .and()
                .assertThat().body("orders.size()", notNullValue())
                .and()
                .assertThat().body("pageInfo.page", equalTo(page))
                .assertThat().body("pageInfo.limit", equalTo(30))
                .and()
                .assertThat().body("availableStations", notNullValue());
    }

    @AfterEach
    public void tearDown() {
        sendDeleteRequestV1CourierId(courierId);
    }
}
