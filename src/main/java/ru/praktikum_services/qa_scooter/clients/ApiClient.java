package ru.praktikum_services.qa_scooter.clients;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import ru.praktikum_services.qa_scooter.model.Courier;
import ru.praktikum_services.qa_scooter.model.CourierCreds;
import ru.praktikum_services.qa_scooter.model.Order;

import java.util.List;

import static io.restassured.RestAssured.given;

public class ApiClient {

    private static final String API_V1_COURIER = "/api/v1/courier";
    private static final String API_V1_COURIER_LOGIN = "/api/v1/courier/login";
    private static final String API_V1_ORDERS = "/api/v1/orders";
    private static final String API_V1_ORDERS_CANCEL ="/api/v1/orders/cancel";
    private static final String API_V1_ORDERS_TRACK ="/api/v1/orders/track";
    private static final String API_V1_ORDERS_ACCEPT ="/api/v1/orders/accept";
    private static final String API_V1_ORDERS_FINISH ="/api/v1/orders/finish";

    public ApiClient() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    public Response createCourier(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post(API_V1_COURIER);
    }

    public Response loginCourier(CourierCreds creds) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(creds)
                .when()
                .post(API_V1_COURIER_LOGIN);
    }

    public Response deleteCourier(String id) {
        return given()
                .header("Content-type", "application/json")
                .when()
                .delete(API_V1_COURIER + "/" + id);
    }

    public Response createOrder(Order order) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .post(API_V1_ORDERS);
    }

    public Response cancelOrder(String track) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(track)
                .when()
                .put(API_V1_ORDERS_CANCEL);
    }

    public Response trackOrder(String track) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .param("t", track)
                .when()
                .get(API_V1_ORDERS_TRACK);
    }

    public Response fullOrderList() {
        return given()
                .header("Content-type", "application/json")
                .when()
                .get(API_V1_ORDERS);
    }

    public Response orderListByCourierId(String courierId) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .param("courierId", courierId)
                .when()
                .get(API_V1_ORDERS);
    }

    public Response orderListLimit(int limit) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .param("limit", limit)
                .when()
                .get(API_V1_ORDERS);
    }

    public Response orderListPage(int page) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .param("page", page)
                .when()
                .get(API_V1_ORDERS);
    }

    public Response orderListByNearestStation(List<Integer> nearestStation) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .param("nearestStation", nearestStation)
                .when()
                .get(API_V1_ORDERS);
    }

    public Response acceptOrder(int id, String courierId) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .param("courierId", courierId)
                .when()
                .put(API_V1_ORDERS_ACCEPT + "/" + id);
    }

    public Response finishOrder(int id) {
        return given()
                .header("Content-type", "application/json")
                .when()
                .put(API_V1_ORDERS_FINISH + "/" + id);
    }

}
