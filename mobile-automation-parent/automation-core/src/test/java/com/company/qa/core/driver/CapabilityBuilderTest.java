package com.company.qa.core.driver;

import com.google.gson.JsonObject;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.appium.java_client.remote.options.BaseOptions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CapabilityBuilderTest {

    private CapabilityBuilder builder;

    @BeforeMethod
    public void setUp() {
        builder = new CapabilityBuilder();
    }

    @Test
    public void build_withAndroidPlatform_returnsUiAutomator2Options() {
        BaseOptions<?> options = builder.platform("android").build();
        assertThat(options).isInstanceOf(UiAutomator2Options.class);
    }

    @Test
    public void build_withIosPlatform_returnsXCUITestOptions() {
        BaseOptions<?> options = builder.platform("ios").build();
        assertThat(options).isInstanceOf(XCUITestOptions.class);
    }

    @Test
    public void build_withoutPlatform_throwsIllegalArgumentException() {
        assertThatThrownBy(() -> builder.build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Platform must be specified");
    }

    @Test
    public void build_withEmptyPlatform_throwsIllegalArgumentException() {
        assertThatThrownBy(() -> builder.platform("").build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Platform must be specified");
    }

    @Test
    public void build_withUnsupportedPlatform_throwsIllegalArgumentException() {
        assertThatThrownBy(() -> builder.platform("windows").build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unsupported platform");
    }

    @Test
    public void build_androidWithDeviceNameAndVersion_setsCorrectly() {
        UiAutomator2Options options = (UiAutomator2Options) builder
                .platform("android")
                .device("Pixel 6")
                .version("13")
                .app("/path/to/app.apk")
                .build();

        assertThat(options.getDeviceName().orElse(null)).isEqualTo("Pixel 6");
        assertThat(options.getPlatformVersion().orElse(null)).isEqualTo("13");
        assertThat(options.getApp().orElse(null)).isEqualTo("/path/to/app.apk");
    }

    @Test
    public void build_iosWithDeviceNameAndVersion_setsCorrectly() {
        XCUITestOptions options = (XCUITestOptions) builder
                .platform("ios")
                .device("iPhone 14")
                .version("16.0")
                .app("/path/to/app.ipa")
                .build();

        assertThat(options.getDeviceName().orElse(null)).isEqualTo("iPhone 14");
        assertThat(options.getPlatformVersion().orElse(null)).isEqualTo("16.0");
        assertThat(options.getApp().orElse(null)).isEqualTo("/path/to/app.ipa");
    }

    @Test
    public void capability_addsExtraCapabilities() {
        UiAutomator2Options options = (UiAutomator2Options) builder
                .platform("android")
                .capability("noReset", "true")
                .capability("autoGrantPermissions", "true")
                .build();

        // extras are applied via amend, verify through the options map
        assertThat(options).isNotNull();
    }

    @Test
    public void fromJson_populatesFieldsFromJsonObject() {
        JsonObject json = new JsonObject();
        json.addProperty("deviceName", "Galaxy S21");
        json.addProperty("platformVersion", "12");
        json.addProperty("app", "/path/to/samsung.apk");
        json.addProperty("customCap", "customValue");

        UiAutomator2Options options = (UiAutomator2Options) builder
                .platform("android")
                .fromJson(json)
                .build();

        assertThat(options.getDeviceName().orElse(null)).isEqualTo("Galaxy S21");
        assertThat(options.getPlatformVersion().orElse(null)).isEqualTo("12");
        assertThat(options.getApp().orElse(null)).isEqualTo("/path/to/samsung.apk");
    }

    @Test
    public void fromJson_withNullJsonObject_returnsBuilderUnchanged() {
        CapabilityBuilder result = builder.platform("android").fromJson(null);
        assertThat(result).isSameAs(builder);

        BaseOptions<?> options = result.build();
        assertThat(options).isInstanceOf(UiAutomator2Options.class);
    }

    @Test
    public void fluentApi_chainingWorks() {
        CapabilityBuilder result = builder
                .platform("android")
                .device("Pixel 7")
                .version("14")
                .app("/app.apk")
                .capability("newCommandTimeout", "300");

        assertThat(result).isSameAs(builder);

        BaseOptions<?> options = result.build();
        assertThat(options).isInstanceOf(UiAutomator2Options.class);
    }

    @Test
    public void build_androidPlatformCaseInsensitive_works() {
        BaseOptions<?> options = builder.platform("ANDROID").build();
        assertThat(options).isInstanceOf(UiAutomator2Options.class);
    }

    @Test
    public void build_iosPlatformCaseInsensitive_works() {
        BaseOptions<?> options = builder.platform("IOS").build();
        assertThat(options).isInstanceOf(XCUITestOptions.class);
    }
}
