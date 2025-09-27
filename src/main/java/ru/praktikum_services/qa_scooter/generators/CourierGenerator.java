package ru.praktikum_services.qa_scooter.generators;

import ru.praktikum_services.qa_scooter.model.Courier;

import static ru.praktikum_services.qa_scooter.utils.Utils.randomString;

public class CourierGenerator {
    public static Courier randomCourier() {
        return new Courier()
                .setLogin(randomString())
                .setPassword(randomString())
                .setFirstName(randomString());
    }

    public static Courier requiredParamsCourier() {
        return new Courier()
                .setLogin(randomString())
                .setPassword(randomString());
    }

    public static Courier noLoginCourier() {
        return new Courier()
                .setPassword(randomString())
                .setFirstName(randomString());
    }

    public static Courier noPasswordCourier() {
        return new Courier()
                .setLogin(randomString())
                .setFirstName(randomString());
    }

    public static Courier emptyLoginAndPasswordCourier() {
        return new Courier()
                .setLogin("")
                .setFirstName("");
    }
}
