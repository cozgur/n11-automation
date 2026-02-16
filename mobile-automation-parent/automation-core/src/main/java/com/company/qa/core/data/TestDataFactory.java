package com.company.qa.core.data;

import com.github.javafaker.Faker;

/**
 * Factory for generating random test data using JavaFaker.
 *
 * <p>Provides convenient static methods for creating realistic random data
 * suitable for test scenarios such as user registration, form filling, and
 * data-driven testing.</p>
 */
public class TestDataFactory {

    private static final Faker FAKER = new Faker();

    /**
     * Generates a random email address.
     *
     * @return a random email address string (e.g. {@code "john.doe@example.com"})
     */
    public static String randomEmail() {
        return FAKER.internet().emailAddress();
    }

    /**
     * Generates a random full name (first and last name).
     *
     * @return a random full name string (e.g. {@code "Jane Smith"})
     */
    public static String randomName() {
        return FAKER.name().fullName();
    }

    /**
     * Generates a random cell phone number.
     *
     * @return a random phone number string
     */
    public static String randomPhone() {
        return FAKER.phoneNumber().cellPhone();
    }

    /**
     * Generates a random password with uppercase, special characters, and digits.
     *
     * @return a random password string between 8 and 20 characters
     */
    public static String randomPassword() {
        return FAKER.internet().password(8, 20, true, true, true);
    }

    /**
     * Generates a random alphanumeric text of the specified length.
     *
     * @param length the desired character count
     * @return a random text string of exactly {@code length} characters
     */
    public static String randomText(int length) {
        return FAKER.lorem().characters(length);
    }

    /**
     * Generates a random integer between {@code min} (inclusive) and {@code max} (exclusive).
     *
     * @param min the lower bound (inclusive)
     * @param max the upper bound (exclusive)
     * @return a random integer in the range {@code [min, max)}
     */
    public static int randomNumber(int min, int max) {
        return FAKER.number().numberBetween(min, max);
    }
}
