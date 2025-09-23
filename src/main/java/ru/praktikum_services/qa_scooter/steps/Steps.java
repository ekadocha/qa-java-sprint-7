package ru.praktikum_services.qa_scooter.steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.praktikum_services.qa_scooter.clients.ApiClient;
import ru.praktikum_services.qa_scooter.model.Courier;
import ru.praktikum_services.qa_scooter.model.Order;

import static ru.praktikum_services.qa_scooter.model.CourierCreds.fromCourier;

public class Steps {

    private ApiClient apiClient = new ApiClient();

    @Step("Отправить POST запрос на /api/v1/courier")
    public Response sendPostRequestV1Courier(Courier courier) {
        return apiClient.createCourier(courier);
    }

    @Step("Отправить POST запрос на /api/v1/courier/login")
    public Response sendPostRequestV1CourierLogin(Courier courier) {
        return apiClient.loginCourier(fromCourier(courier));
    }

    @Step("Отправить DELETE запрос на /api/v1/courier/:id")
    public Response sendDeleteRequestV1CourierId(String id) {
        return apiClient.deleteCourier(id);
    }

    @Step("Отправить GET запрос на /api/v1/orders")
    public Response sendGetRequestV1Orders() {
        return apiClient.fullOrderList();
    }

    @Step("Отправить GET запрос на /api/v1/orders с параметром courierId")
    public Response sendGetRequestV1OrdersWithCourierIdParam(String courierId) {
        return apiClient.orderListByCourierId(courierId);
    }

    @Step("Отправить POST запрос на /api/v1/orders")
    public Response sendPostRequestV1Orders(Order order) {
        return apiClient.createOrder(order);
    }

    @Step("Отправить GET запрос на /api/v1/orders/track")
    public Response sendGetRequestV1OrdersTrack(String track) {
        return apiClient.trackOrder(track);
    }

    @Step("Отправить PUT запрос на /api/v1/orders/accept/:id")
    public Response sendPutRequestV1OrdersAcceptId(int orderId, String courierId) {
        return apiClient.acceptOrder(orderId, courierId);
    }

    @Step("Отправить PUT запрос на /api/v1/orders/finish/:id")
    public Response sendPutRequestV1OrdersFinishId(int orderId) {
        return apiClient.finishOrder(orderId);
    }

    @Step("Отправить GET запрос на /api/v1/orders с параметром limit")
    public Response sendGetRequestV1OrdersWithLimitParam(int limit) {
        return apiClient.orderListLimit(limit);
    }

    @Step("Отправить GET запрос на /api/v1/orders с параметром page")
    public Response sendGetRequestV1OrdersWithPageParam(int page) {
        return apiClient.orderListPage(page);
    }

    @Step("Отправить PUT запрос на /api/v1/orders/cancel")
    public Response sendPutRequestV1OrdersCancel(String track) {
        return apiClient.cancelOrder(track);
    }
}
