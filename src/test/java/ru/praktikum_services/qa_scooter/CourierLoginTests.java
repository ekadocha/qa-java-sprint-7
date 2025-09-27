package ru.praktikum_services.qa_scooter;

import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.praktikum_services.qa_scooter.model.Courier;
import ru.praktikum_services.qa_scooter.model.CourierCredsResponse;
import ru.praktikum_services.qa_scooter.steps.Steps;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static ru.praktikum_services.qa_scooter.generators.CourierGenerator.*;
import static ru.praktikum_services.qa_scooter.utils.Utils.randomString;

public class CourierLoginTests extends Steps {

    private String id;

    @Test
    @DisplayName("Логин курьера. Курьер может авторизоваться с правильным логином и паролем")
    public void loginCourierWithCorrectCredentialsReturnsIdTest() {
        Courier courier = requiredParamsCourier();

        Response response = sendPostRequestV1Courier(courier);

        response.then()
                .statusCode(201);

        Response loginResponse = sendPostRequestV1CourierLogin(courier);
        id = loginResponse.as(CourierCredsResponse.class).getId();

        loginResponse.then()
                .statusCode(200)
                .and()
                .assertThat().body("id", notNullValue());
    }

    @Test // по таймауту отбивается с 504
    @DisplayName("Логин курьера. Нельзя авторизоваться курьером без логина и пароля")
    public void loginCourierWithEmptyBodyTest() {
        Courier courier = emptyLoginAndPasswordCourier();

        Response loginResponse = sendPostRequestV1CourierLogin(courier);

        loginResponse.then()
                .statusCode(400) // по таймауту отбивается с 504
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Логин курьера. Нельзя авторизоваться курьером с паролем, но без логина")
    public void loginCourierWithoutLoginTest() {
        Courier courier = noLoginCourier();

        Response loginResponse = sendPostRequestV1CourierLogin(courier);

        loginResponse.then()
                .statusCode(400)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Логин курьера. Нельзя авторизоваться курьером с логином, но без пароля")
    public void loginCourierWithoutPasswordTest() {
        Courier courier = noPasswordCourier();

        Response loginResponse = sendPostRequestV1CourierLogin(courier);

        loginResponse.then()
                .statusCode(400)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для входа"));
        // по таймауту отбивается с 504
    }

    @Test
    @DisplayName("Логин курьера. Нельзя авторизоваться курьером с логином, но неподходящим паролем")
    public void loginCourierWithWrongPasswordTest() {
        Courier courier = randomCourier();

        Response response = sendPostRequestV1Courier(courier);

        response.then()
                .statusCode(201);

        Response loginResponse = sendPostRequestV1CourierLogin(courier.setPassword(randomString())); // смена пароля
        id = loginResponse.as(CourierCredsResponse.class).getId();

        loginResponse.then()
                .statusCode(404)
                .and()
                .assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Логин курьера. Нельзя авторизоваться курьером, которого нет в системе")
    public void loginNonexistentCourierTest() {
        Courier courier = randomCourier();

        Response loginResponse = sendPostRequestV1CourierLogin(courier); // смена пароля

        loginResponse.then()
                .statusCode(404)
                .and()
                .assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    @AfterEach
    public void tearDown() {
        sendDeleteRequestV1CourierId(id);
    }
}
