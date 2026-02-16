package com.company.qa.core.config;

import org.testng.annotations.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ConfigReaderTest {

    @Test
    public void load_withValidYamlFile_returnsNonNullMap() {
        Map<String, Object> config = ConfigReader.load("config/default.yaml");
        assertThat(config).isNotNull();
        assertThat(config).isNotEmpty();
    }

    @Test
    public void load_withNonExistentFile_throwsRuntimeException() {
        assertThatThrownBy(() -> ConfigReader.load("config/nonexistent.yaml"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Config file not found");
    }

    @Test
    public void getString_withSimpleKey_returnsValue() {
        Map<String, Object> config = ConfigReader.load("config/default.yaml");
        // "appium" is a top-level key containing a map, so test a nested simple value
        Map<String, Object> retrySection = ConfigReader.getSection(config, "retry");
        assertThat(retrySection).isNotNull();

        // Use getString with dot notation for a simple nested value
        String maxRetry = ConfigReader.getString(config, "retry.max");
        assertThat(maxRetry).isNotNull();
        assertThat(maxRetry).isEqualTo("2");
    }

    @Test
    public void getString_withNestedDotNotationKey_returnsValue() {
        Map<String, Object> config = ConfigReader.load("config/default.yaml");
        String appiumUrl = ConfigReader.getString(config, "appium.url");
        assertThat(appiumUrl).isEqualTo("http://127.0.0.1:4723");
    }

    @Test
    public void getString_withNonExistentKey_returnsNull() {
        Map<String, Object> config = ConfigReader.load("config/default.yaml");
        String value = ConfigReader.getString(config, "nonexistent.key");
        assertThat(value).isNull();
    }

    @Test
    public void getString_withDefault_returnsDefaultWhenKeyMissing() {
        Map<String, Object> config = ConfigReader.load("config/default.yaml");
        String value = ConfigReader.getString(config, "missing.key", "fallback");
        assertThat(value).isEqualTo("fallback");
    }

    @Test
    public void getString_withDefault_returnsActualValueWhenKeyExists() {
        Map<String, Object> config = ConfigReader.load("config/default.yaml");
        String value = ConfigReader.getString(config, "appium.url", "fallback");
        assertThat(value).isEqualTo("http://127.0.0.1:4723");
    }

    @Test
    public void getSection_withValidSectionKey_returnsSubMap() {
        Map<String, Object> config = ConfigReader.load("config/default.yaml");
        Map<String, Object> appiumSection = ConfigReader.getSection(config, "appium");
        assertThat(appiumSection).isNotNull();
        assertThat(appiumSection).containsKey("url");
        assertThat(appiumSection.get("url")).isEqualTo("http://127.0.0.1:4723");
    }

    @Test
    public void getSection_withNonExistentSection_returnsNull() {
        Map<String, Object> config = ConfigReader.load("config/default.yaml");
        Map<String, Object> section = ConfigReader.getSection(config, "nonexistent");
        assertThat(section).isNull();
    }

    @Test
    public void getString_withNullConfig_returnsNull() {
        String value = ConfigReader.getString(null, "some.key");
        assertThat(value).isNull();
    }

    @Test
    public void getString_withNullKey_returnsNull() {
        Map<String, Object> config = ConfigReader.load("config/default.yaml");
        String value = ConfigReader.getString(config, null);
        assertThat(value).isNull();
    }

    @Test
    public void getSection_withNullConfig_returnsNull() {
        Map<String, Object> section = ConfigReader.getSection(null, "appium");
        assertThat(section).isNull();
    }

    @Test
    public void load_testYaml_returnsOverriddenValues() {
        Map<String, Object> config = ConfigReader.load("config/test.yaml");
        assertThat(config).isNotNull();
        String appiumUrl = ConfigReader.getString(config, "appium.url");
        assertThat(appiumUrl).isEqualTo("http://test-server:4723");
    }

    @Test
    public void getString_withDeepNestedKey_returnsCorrectValue() {
        Map<String, Object> config = ConfigReader.load("config/default.yaml");
        String implicitTimeout = ConfigReader.getString(config, "timeout.implicit");
        assertThat(implicitTimeout).isEqualTo("10");

        String explicitTimeout = ConfigReader.getString(config, "timeout.explicit");
        assertThat(explicitTimeout).isEqualTo("15");
    }
}
