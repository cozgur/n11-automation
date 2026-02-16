package com.company.qa.core.data;

import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class TestDataFactoryTest {

    @Test
    public void randomEmail_returnsValidEmailFormat() {
        String email = TestDataFactory.randomEmail();
        assertThat(email).isNotNull();
        assertThat(email).isNotEmpty();
        assertThat(email).contains("@");
        assertThat(email).contains(".");
    }

    @Test
    public void randomName_returnsNonEmptyString() {
        String name = TestDataFactory.randomName();
        assertThat(name).isNotNull();
        assertThat(name).isNotEmpty();
    }

    @Test
    public void randomPhone_returnsNonEmptyString() {
        String phone = TestDataFactory.randomPhone();
        assertThat(phone).isNotNull();
        assertThat(phone).isNotEmpty();
    }

    @Test
    public void randomPassword_returnsStringWithMinLength8() {
        String password = TestDataFactory.randomPassword();
        assertThat(password).isNotNull();
        assertThat(password.length()).isGreaterThanOrEqualTo(8);
    }

    @Test
    public void randomPassword_returnsStringWithMaxLength20() {
        String password = TestDataFactory.randomPassword();
        assertThat(password.length()).isLessThanOrEqualTo(20);
    }

    @Test
    public void randomText_returnsStringOfSpecifiedLength() {
        String text = TestDataFactory.randomText(10);
        assertThat(text).isNotNull();
        assertThat(text).hasSize(10);
    }

    @Test
    public void randomText_withDifferentLengths() {
        assertThat(TestDataFactory.randomText(1)).hasSize(1);
        assertThat(TestDataFactory.randomText(50)).hasSize(50);
        assertThat(TestDataFactory.randomText(100)).hasSize(100);
    }

    @Test
    public void randomNumber_returnsNumberInRange() {
        int number = TestDataFactory.randomNumber(1, 100);
        assertThat(number).isGreaterThanOrEqualTo(1);
        assertThat(number).isLessThan(100);
    }

    @Test
    public void randomNumber_withSmallRange() {
        int number = TestDataFactory.randomNumber(5, 6);
        assertThat(number).isEqualTo(5);
    }

    @Test
    public void multipleCalls_returnDifferentValues_email() {
        Set<String> emails = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            emails.add(TestDataFactory.randomEmail());
        }
        // With 10 random emails, we expect at least 2 unique values
        assertThat(emails.size()).isGreaterThan(1);
    }

    @Test
    public void multipleCalls_returnDifferentValues_name() {
        Set<String> names = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            names.add(TestDataFactory.randomName());
        }
        assertThat(names.size()).isGreaterThan(1);
    }

    @Test
    public void multipleCalls_returnDifferentValues_number() {
        Set<Integer> numbers = new HashSet<>();
        for (int i = 0; i < 20; i++) {
            numbers.add(TestDataFactory.randomNumber(1, 10000));
        }
        // With range 1-10000 and 20 calls, we expect at least 2 unique values
        assertThat(numbers.size()).isGreaterThan(1);
    }

    @Test
    public void randomEmail_multipleCallsAlwaysContainAt() {
        for (int i = 0; i < 5; i++) {
            String email = TestDataFactory.randomEmail();
            assertThat(email).contains("@");
        }
    }
}
