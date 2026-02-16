package com.company.qa.core.data;

import com.github.javafaker.Faker;

/**
 * Factory for generating random test data using JavaFaker.
 */
public class TestDataFactory {

    private static final Faker FAKER = new Faker();

    public static String randomEmail() {
        return FAKER.internet().emailAddress();
    }

    public static String randomName() {
        return FAKER.name().fullName();
    }

    public static String randomPhone() {
        return FAKER.phoneNumber().cellPhone();
    }

    public static String randomPassword() {
        return FAKER.internet().password(8, 20, true, true, true);
    }

    public static String randomText(int length) {
        return FAKER.lorem().characters(length);
    }

    public static int randomNumber(int min, int max) {
        return FAKER.number().numberBetween(min, max);
    }
}
