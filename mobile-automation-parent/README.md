# Mobile Automation Parent

A multi-module Maven project for mobile test automation using Appium, Cucumber, and TestNG. Supports multiple apps (CloneAI, FunnelFox, N11) across Android and iOS platforms with config-driven execution.

## Prerequisites

- **Java 11** (JDK)
- **Maven 3.8+**
- **Appium 2.x** server installed and running
- **Android SDK** (for Android testing) with platform-tools on PATH
- **Xcode** (for iOS testing, macOS only)
- A real device connected via USB or an emulator/simulator configured

## Quick Start

```bash
# Install dependencies
mvn clean install -DskipTests

# Run CloneAI smoke tests on Android
mvn test -pl automation-tests -Dapp=cloneai -Dplatform=android

# Run FunnelFox tests on iOS
mvn test -pl automation-tests -Dapp=funnelfox -Dplatform=ios
```

## Module Structure

| Module | Description |
|---|---|
| `automation-core` | Shared framework: driver management, config loader, base classes, utilities |
| `automation-tests` | Test implementations: step definitions, runners, feature files, test configs |

## Running Tests

### Basic usage

```bash
# Specify app and platform via system properties
mvn test -Dapp=cloneai -Dplatform=android

# Override device
mvn test -Dapp=cloneai -Dplatform=android -DdeviceName="Samsung Galaxy S22"
```

### Maven Profiles

```bash
# Run only smoke-tagged tests
mvn test -Psmoke -Dapp=cloneai -Dplatform=android

# Run against staging environment
mvn test -Pstaging -Dapp=cloneai -Dplatform=android
```

### TestNG Suite

```bash
# Run the full TestNG suite
mvn test -DsuiteXmlFile=src/test/resources/testng.xml
```

## Configuration

### YAML Configs (`automation-tests/src/test/resources/config/`)

- `default.yaml` -- Default timeouts, retry policy, Appium server URL
- `android.yaml` -- Android-specific capabilities (UiAutomator2)
- `ios.yaml` -- iOS-specific capabilities (XCUITest)
- `dev.yaml` -- Dev environment overrides
- `staging.yaml` -- Staging environment overrides
- `prod.yaml` -- Prod environment overrides (read-only tests)

### App Dictionary (`automation-tests/src/test/resources/dictionary/apps.json`)

Maps each app name to its platform-specific capabilities (APK/IPA path, package/bundle ID, device defaults). The `-Dapp=` system property selects which app config to load at runtime.

## Reporting

### Allure Reports

```bash
# Generate and open Allure report after test run
mvn allure:serve

# Or generate to a directory
mvn allure:report
# Report output: target/site/allure-maven-plugin/
```

Screenshots are captured automatically on test failure and attached to Allure reports.

## Static Analysis

```bash
# Run all static analysis checks
mvn checkstyle:check pmd:check spotbugs:check

# Individual checks
mvn checkstyle:check
mvn pmd:check
mvn spotbugs:check
```

## Project Structure

```
mobile-automation-parent/
├── pom.xml                          # Parent POM
├── checkstyle.xml                   # Checkstyle rules
├── .gitignore
├── README.md
├── automation-core/
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/company/qa/core/
│       │   ├── config/              # Config loader, YAML parsing
│       │   ├── driver/              # DriverManager, DriverFactory
│       │   ├── pages/               # BasePage, page utilities
│       │   ├── retry/               # RetryAnalyzer, RetryListener
│       │   ├── steps/               # Shared step definitions
│       │   └── utils/               # WaitUtils, ScreenshotUtils, etc.
│       └── resources/
│           └── log4j2.xml           # Logging configuration
├── automation-tests/
│   ├── pom.xml
│   └── src/test/
│       ├── java/com/company/qa/tests/
│       │   ├── runners/             # TestNG + Cucumber runners
│       │   └── steps/               # App-specific step definitions
│       └── resources/
│           ├── config/              # YAML environment configs
│           ├── dictionary/          # apps.json, pages.json
│           ├── features/            # Cucumber .feature files
│           │   ├── cloneai/
│           │   └── funnelfox/
│           ├── testng.xml           # TestNG suite definition
│           └── allure.properties    # Allure report settings
```
