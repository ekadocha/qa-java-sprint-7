package ru.praktikum_services.qa_scooter.utils;

import java.util.Random;

public class Utils {

    public static String randomString() {
        return randomString(10);
    }

    public static String randomString(int length) {
        Random random = new Random();
        int leftLimit = 97;
        int rightLimit = 122;
        StringBuilder buffer = new StringBuilder(length);

        for(int i = 0; i < length; ++i) {
            int randomLimitedInt = leftLimit + (int)(random.nextFloat() * (float)(rightLimit - leftLimit + 1));
            buffer.append(Character.toChars(randomLimitedInt));
        }

        return buffer.toString();
    }

    public static String randomPhoneNumber() {
        Random random = new Random();
        StringBuilder buffer = new StringBuilder();
        buffer.append("7");
        buffer.append(900 + random.nextInt(100));
        for (int i = 0; i < 7; i++) {
            buffer.append(random.nextInt(10));
        }
        return buffer.toString();
    }

    public static int randomDigit() {
        Random random = new Random();
        return random.nextInt(10);
    }
}