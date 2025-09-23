package ru.praktikum_services.qa_scooter;

import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.praktikum_services.qa_scooter.model.Courier;
import ru.praktikum_services.qa_scooter.model.CourierCredsResponse;
import ru.praktikum_services.qa_scooter.steps.Steps;

import static org.hamcrest.Matchers.equalTo;
import static ru.praktikum_services.qa_scooter.generators.CourierGenerator.*;
import static ru.praktikum_services.qa_scooter.utils.Utils.randomString;

public class CourierDeleteTests extends Steps {

    private String id;

    @Test
    @DisplayName("Удаление курьера. Можно удалить курьера по его идентификатору")
    public void deleteCourierWithExistIdTest() {
        Courier courier = randomCourier();

        Response response = sendPostRequestV1Courier(courier);

        response.then()
                .statusCode(201);

        Response loginResponse = sendPostRequestV1CourierLogin(courier);
        id = loginResponse.as(CourierCredsResponse.class).getId();

        loginResponse.then()
                .statusCode(200);

        Response deleteResponse = sendDeleteRequestV1CourierId(id);

        deleteResponse.then()
                .statusCode(200)
                .and()
                .assertThat().body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Удаление курьера. Нельзя удалить курьера без указания идентификатора")
    public void deleteCourierWithoutIdTest() {
        Response deleteResponse = sendDeleteRequestV1CourierId("");

        deleteResponse.then()
                .statusCode(404)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для удаления курьера"));
        // тест ловит неправильный текст ошибки
        // по документации должен быть "Недостаточно данных для удаления курьера"
        // фактически - "Not Found"
    }

    @Test
    @DisplayName("Удаление курьера. Нельзя удалить уже удаленного курьера")
    public void deleteCourierWithNonexistentIdTest() {
        Courier courier = randomCourier();

        Response response = sendPostRequestV1Courier(courier);

        response.then()
                .statusCode(201);

        Response loginResponse = sendPostRequestV1CourierLogin(courier);
        id = loginResponse.as(CourierCredsResponse.class).getId();

        loginResponse.then()
                .statusCode(200);

        Response deleteResponse = sendDeleteRequestV1CourierId(id);

        deleteResponse.then()
                .statusCode(200);

        deleteResponse = sendDeleteRequestV1CourierId(id);

        deleteResponse.then()
                .statusCode(404)
                .and()
                .assertThat().body("message", equalTo("Курьера с таким id нет"));
        // тест ловит ошибку, т.к. по документации в теле ответа должно быть сообщение "Курьера с таким id нет" (без точки)
        // фактически - "Курьера с таким id нет." (с точкой)
    }

    @Test
    @DisplayName("Удаление курьера. Нельзя удалить курьера, если не существует курьера с указанным идентификатором")
    public void deleteCourierWithRandomStringIdTest() {
        Response deleteResponse = sendDeleteRequestV1CourierId(randomString(3));

        deleteResponse.then()
                .statusCode(404)
                .and()
                .assertThat().body("message", equalTo("Курьера с таким id нет"));
        // тест ловит ошибку 500, хотя в документации для удаления курьера нет ограничения по символам для id.
        // в delete указан тип id как string, а при создании курьера возвращается id как int (при отправке запроса из постман пишет, что ошибается int)
        // тогда в документации для удаления курьера тоже надо принимать int
    }
}
