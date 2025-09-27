package ru.praktikum_services.qa_scooter.generators;

import ru.praktikum_services.qa_scooter.model.Order;

import java.time.LocalDate;
import java.util.List;

import static ru.praktikum_services.qa_scooter.utils.Utils.*;

public class OrderGenerator {
    public static Order allParamsOrder(List<String> color) {
        return new Order()
                .setFirstName(randomString())
                .setLastName(randomString())
                .setAddress(randomString())
                .setMetroStation("Сокол") // для метро тоже можно создать рандомайзер на основе списка станций метро, но пока так
                .setPhone(randomPhoneNumber())
                .setRentTime(randomDigit())
                .setDeliveryDate(LocalDate.now().plusDays(randomDigit()).toString())
                .setComment(randomString())
                .setColor(color);

    }

    public static Order requiredParamsRandomOrder() {
        return new Order()
                .setFirstName(randomString())
                .setLastName(randomString())
                .setAddress(randomString())
                .setMetroStation("Сокол")
                .setPhone(randomPhoneNumber())
                .setRentTime(randomDigit())
                .setDeliveryDate(LocalDate.now().plusDays(randomDigit()).toString())
                .setComment(randomString());

    }

    /*
    Строки, которые можно заполнить рандомом, заполнить так
    В параметризацию отдать только цвет



     */
}
