package com.company.qa.core.config;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

public class EnvironmentConfigTest {

    @BeforeMethod
    public void resetSingleton() throws Exception {
        // Use reflection to reset the singleton instance before each test
        Field instanceField = EnvironmentConfig.class.getDeclaredField("instance");
        instanceField.setAccessible(true);
        instanceField.set(null, null);
    }

    @Test
    public void getInstance_returnsNonNull() {
        EnvironmentConfig config = EnvironmentConfig.getInstance();
        assertThat(config).isNotNull();
    }

    @Test
    public void getInstance_returnsSameInstance_singleton() {
        EnvironmentConfig first = EnvironmentConfig.getInstance();
        EnvironmentConfig second = EnvironmentConfig.getInstance();
        assertThat(first).isSameAs(second);
    }

    @Test
    public void getAppiumUrl_returnsDefaultValue() {
        // With test resources providing config/default.yaml, this should return the YAML value
        // or the hardcoded default if no config is found
        EnvironmentConfig config = EnvironmentConfig.getInstance();
        String appiumUrl = config.getAppiumUrl();
        assertThat(appiumUrl).isNotNull();
        assertThat(appiumUrl).isNotEmpty();
        // The default.yaml in test resources has "http://127.0.0.1:4723"
        assertThat(appiumUrl).isEqualTo("http://127.0.0.1:4723");
    }

    @Test
    public void getImplicitTimeout_returnsValue() {
        EnvironmentConfig config = EnvironmentConfig.getInstance();
        int timeout = config.getImplicitTimeout();
        // default.yaml has implicit: 10
        assertThat(timeout).isEqualTo(10);
    }

    @Test
    public void getExplicitTimeout_returnsValue() {
        EnvironmentConfig config = EnvironmentConfig.getInstance();
        int timeout = config.getExplicitTimeout();
        // default.yaml has explicit: 15
        assertThat(timeout).isEqualTo(15);
    }

    @Test
    public void getRetryMax_returnsValue() {
        EnvironmentConfig config = EnvironmentConfig.getInstance();
        int retryMax = config.getRetryMax();
        // default.yaml has max: 2
        assertThat(retryMax).isEqualTo(2);
    }

    @Test
    public void isScreenshotOnFailure_returnsValue() {
        EnvironmentConfig config = EnvironmentConfig.getInstance();
        boolean screenshot = config.isScreenshotOnFailure();
        // default.yaml has onFailure: true
        assertThat(screenshot).isTrue();
    }

    @Test
    public void get_withExistingKey_returnsValue() {
        EnvironmentConfig config = EnvironmentConfig.getInstance();
        String value = config.get("appium.url");
        assertThat(value).isEqualTo("http://127.0.0.1:4723");
    }

    @Test
    public void get_withMissingKey_returnsNull() {
        EnvironmentConfig config = EnvironmentConfig.getInstance();
        String value = config.get("nonexistent.key");
        assertThat(value).isNull();
    }

    @Test
    public void get_withDefaultForMissingKey_returnsDefault() {
        EnvironmentConfig config = EnvironmentConfig.getInstance();
        String value = config.get("nonexistent.key", "myDefault");
        assertThat(value).isEqualTo("myDefault");
    }

    @Test
    public void get_withDefaultForExistingKey_returnsActualValue() {
        EnvironmentConfig config = EnvironmentConfig.getInstance();
        String value = config.get("appium.url", "myDefault");
        assertThat(value).isEqualTo("http://127.0.0.1:4723");
    }

    @Test
    public void getRetryDelayMs_returnsValue() {
        EnvironmentConfig config = EnvironmentConfig.getInstance();
        long delayMs = config.getRetryDelayMs();
        // default.yaml has delayMs: 1000
        assertThat(delayMs).isEqualTo(1000L);
    }
}
