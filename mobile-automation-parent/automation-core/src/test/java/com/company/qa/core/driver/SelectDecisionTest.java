package com.company.qa.core.driver;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SelectDecisionTest {

    @Test
    public void resolve_id_returnsByIdLocator() {
        By result = SelectDecision.resolve("id", "myElement");
        assertThat(result).isEqualTo(By.id("myElement"));
    }

    @Test
    public void resolve_xpath_returnsByXpathLocator() {
        By result = SelectDecision.resolve("xpath", "//div[@id='test']");
        assertThat(result).isEqualTo(By.xpath("//div[@id='test']"));
    }

    @Test
    public void resolve_name_returnsByNameLocator() {
        By result = SelectDecision.resolve("name", "username");
        assertThat(result).isEqualTo(By.name("username"));
    }

    @Test
    public void resolve_className_returnsByClassNameLocator() {
        By result = SelectDecision.resolve("className", "btn-primary");
        assertThat(result).isEqualTo(By.className("btn-primary"));
    }

    @Test
    public void resolve_cssSelector_returnsByCssSelectorLocator() {
        By result = SelectDecision.resolve("cssSelector", "div.content > p");
        assertThat(result).isEqualTo(By.cssSelector("div.content > p"));
    }

    @Test
    public void resolve_tagName_returnsByTagNameLocator() {
        By result = SelectDecision.resolve("tagName", "input");
        assertThat(result).isEqualTo(By.tagName("input"));
    }

    @Test
    public void resolve_linkText_returnsByLinkTextLocator() {
        By result = SelectDecision.resolve("linkText", "Click Here");
        assertThat(result).isEqualTo(By.linkText("Click Here"));
    }

    @Test
    public void resolve_partialLinkText_returnsByPartialLinkTextLocator() {
        By result = SelectDecision.resolve("partialLinkText", "Click");
        assertThat(result).isEqualTo(By.partialLinkText("Click"));
    }

    @Test
    public void resolve_accessibilityId_returnsAppiumByAccessibilityId() {
        By result = SelectDecision.resolve("accessibilityId", "loginButton");
        assertThat(result).isEqualTo(AppiumBy.accessibilityId("loginButton"));
    }

    @Test
    public void resolve_invalidSelectorType_throwsIllegalArgumentException() {
        assertThatThrownBy(() -> SelectDecision.resolve("invalid", "value"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Not a valid selector type");
    }

    @Test
    public void resolve_emptySelectorType_throwsIllegalArgumentException() {
        assertThatThrownBy(() -> SelectDecision.resolve("", "value"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Not a valid selector type");
    }
}
