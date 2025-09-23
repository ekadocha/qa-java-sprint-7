package ru.praktikum_services.qa_scooter;

import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.praktikum_services.qa_scooter.model.Courier;
import ru.praktikum_services.qa_scooter.model.CourierCredsResponse;
import ru.praktikum_services.qa_scooter.steps.Steps;

import static org.hamcrest.Matchers.*;
import static ru.praktikum_services.qa_scooter.generators.CourierGenerator.*;

public class CourierCreateTests extends Steps {

    private String id;

    @Test
    @DisplayName("Создание курьера. Можно создать курьера со всеми параметрами")
    public void createCourierWithAllParamsTest() {
        Courier courier = randomCourier();

        Response response = sendPostRequestV1Courier(courier);

        response.then()
                .statusCode(201)
                .and()
                .assertThat().body("ok", equalTo(true));

        Response loginResponse = sendPostRequestV1CourierLogin(courier);
        id = loginResponse.as(CourierCredsResponse.class).getId();

        loginResponse.then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Создание курьера. Нельзя создать курьера с логином, который уже занят")
    public void createCourierWithExistLoginTest() {
        Courier courier = randomCourier();

        Response response1 = sendPostRequestV1Courier(courier);

        response1.then()
                .statusCode(201)
                .and()
                .assertThat().body("ok", equalTo(true));

        Response response2 = sendPostRequestV1Courier(courier);

        response2.then()
                .statusCode(409)
                .and()
                .assertThat().body("message", equalTo("Этот логин уже используется"));
        // тест ловит ошибку, т.к. текст валидации отличается от документации.
        // по документации должно быть "Этот логин уже используется"
        // фактически - "Этот логин уже используется. Попробуйте другой."
    }

    @Test
    @DisplayName("Создание курьера. Можно создать курьера с обязательными параметрами")
    public void createCourierWithRequiredParamsTest() {
        Courier courier = requiredParamsCourier();

        Response response = sendPostRequestV1Courier(courier);

        response.then()
                .statusCode(201)
                .and()
                .assertThat().body("ok", equalTo(true));

        Response loginResponse = sendPostRequestV1CourierLogin(courier);
        id = loginResponse.as(CourierCredsResponse.class).getId();

        loginResponse.then()
                .statusCode(200);

    }

    @Test
    @DisplayName("Создание курьера. Нельзя создать курьера без заполнения обязательных полей")
    public void createCourierWithoutRequiredParamLoginTest() {
        Courier courier = noLoginCourier();

        Response response = sendPostRequestV1Courier(courier);

        response.then()
                .statusCode(400)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));

    }

    @Test
    @DisplayName("Создание курьера. Нельзя создать курьера без указания пароля")
    public void createCourierWithoutRequiredParamPasswordTest() {
        Courier courier = noPasswordCourier();

        Response response = sendPostRequestV1Courier(courier);

        response.then()
                .statusCode(400)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
        
    }

    @AfterEach
    public void tearDown() {
        sendDeleteRequestV1CourierId(id);
    }
}
