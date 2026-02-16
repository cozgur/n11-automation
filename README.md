# N11 Automation

Multi-module mobile and web test automation framework built with **Appium**, **Cucumber 7**, and **TestNG**. Supports Android and iOS platforms with config-driven execution across multiple applications.

## Tech Stack

| Component | Version |
|-----------|---------|
| Java | 11 |
| Selenium | 4.35.0 |
| Appium | 10.0.0 |
| Cucumber | 7.15.0 |
| TestNG | 7.9.0 |
| REST Assured | 5.4.0 |
| Allure | 2.25.0 |
| Log4j2 | 2.22.1 |

## Prerequisites

- **Java 11** (JDK)
- **Maven 3.8+**
- **Appium 3.x** server installed and running
- **Android SDK** with platform-tools on PATH (for Android testing)
- **Xcode** on macOS (for iOS testing)
- A connected device or configured emulator/simulator

## Project Structure

```
n11-automation/
└── mobile-automation-parent/          # Main multi-module project
    ├── pom.xml                        # Parent POM with dependency management
    ├── checkstyle.xml                 # Code style rules
    │
    ├── automation-core/               # Reusable framework layer
    │   └── src/main/java/.../core/
    │       ├── action/                # MobileActions (gestures, taps, swipes)
    │       ├── api/                   # ApiClient (REST Assured wrapper)
    │       ├── base/                  # BaseScreen, BaseFlow (Page Object base)
    │       ├── config/                # ConfigReader, EnvironmentConfig (YAML)
    │       ├── context/               # TestContext (ThreadLocal state sharing)
    │       ├── data/                  # TestDataFactory (JavaFaker)
    │       ├── driver/                # MobileDriverManager, BrowserManager,
    │       │                          # CapabilityBuilder, SelectDecision
    │       ├── exception/             # FrameworkException, ConfigurationException,
    │       │                          # DriverInitializationException
    │       ├── retry/                 # RetryAnalyzer, RetryListener
    │       ├── util/                  # LogManager, ScreenshotHelper, JsonParser
    │       └── wait/                  # WaitHelper (explicit waits)
    │
    ├── apps/                          # App-specific modules
    │   ├── app-cloneai/               # CloneAI screens, flows, capabilities
    │   └── app-funnelfox/             # FunnelFox screens, flows, capabilities
    │
    └── automation-tests/              # Test execution layer
        └── src/test/
            ├── java/.../tests/
            │   ├── hooks/             # Cucumber hooks (screenshots, cleanup)
            │   ├── runners/           # MobileTestRunner, TestRunner
            │   └── steps/             # Step definitions (mobile/, web/, common)
            └── resources/
                ├── config/            # YAML configs (default, android, ios, env)
                ├── dictionary/        # apps.json, pages.json
                ├── features/          # Cucumber .feature files
                │   ├── cloneai/
                │   └── funnelfox/
                └── testng.xml         # TestNG suite definition
```

## Quick Start

```bash
# Build without running tests
cd mobile-automation-parent
mvn clean install -DskipTests

# Run CloneAI smoke tests on Android
mvn test -pl automation-tests -Dapp=cloneai -Dplatform=android

# Run FunnelFox tests on iOS
mvn test -pl automation-tests -Dapp=funnelfox -Dplatform=ios
```

## Running Tests

### System Properties

| Property | Description | Example |
|----------|-------------|---------|
| `-Dapp` | Target application | `cloneai`, `funnelfox`, `n11` |
| `-Dplatform` | Target platform | `android`, `ios` |
| `-DdeviceName` | Override device name | `"Pixel 6"`, `"iPhone 14"` |
| `-DplatformVersion` | Override OS version | `"13.0"`, `"16.0"` |

```bash
# Run with device override
mvn test -pl automation-tests -Dapp=cloneai -Dplatform=android -DdeviceName="Samsung Galaxy S22"

# Run full TestNG suite
mvn test -pl automation-tests -DsuiteXmlFile=src/test/resources/testng.xml
```

### Maven Profiles

| Profile | Purpose |
|---------|---------|
| `dev` (default) | Development environment |
| `staging` | Staging environment |
| `prod` | Production (read-only tests) |
| `smoke` | Smoke test suite only |
| `regression` | Full regression suite |

```bash
# Smoke tests against staging
mvn test -Psmoke,staging -Dapp=cloneai -Dplatform=android
```

## Configuration

### YAML Configs (`automation-tests/src/test/resources/config/`)

- **`default.yaml`** -- Appium URL, timeouts (implicit: 10s, explicit: 30s), retry policy (max: 3)
- **`android.yaml`** / **`ios.yaml`** -- Platform-specific capabilities
- **`dev.yaml`** / **`staging.yaml`** / **`prod.yaml`** -- Environment overrides

Configs are loaded hierarchically: `default.yaml` -> platform config -> environment config. Later values override earlier ones.

### App Registry (`automation-tests/src/test/resources/dictionary/apps.json`)

Maps app names to platform-specific capabilities (APK/IPA path, package/bundle ID, device defaults). The `-Dapp` system property selects which config to load at runtime.

## Architecture

### Key Design Patterns

- **Page Object Model** -- `BaseScreen` provides `find()`, `tap()`, `type()`, `getText()`, `isDisplayed()` for screen interactions
- **ThreadLocal Driver Management** -- Safe parallel execution with isolated driver instances per thread
- **Config-Driven Execution** -- App/platform/environment selection via system properties, no code changes needed
- **Fluent Capability Builder** -- `CapabilityBuilder` for chainable Appium capability setup
- **BDD with Cucumber** -- Gherkin feature files with reusable step definitions

### Module Dependencies

```
automation-tests  -->  app-cloneai / app-funnelfox  -->  automation-core
```

Each app module provides screens, flows, and capabilities specific to that application, all built on the shared `automation-core` framework.

## Reporting

```bash
# Generate and open Allure report in browser
mvn allure:serve

# Generate report to target/site/allure-maven-plugin/
mvn allure:report
```

Screenshots are captured automatically on test failure and attached to Allure reports.

## Static Analysis

```bash
# Run all checks
mvn checkstyle:check pmd:check spotbugs:check

# Individual
mvn checkstyle:check
mvn pmd:check
mvn spotbugs:check
```

## Unit Tests

The `automation-core` module includes unit tests for core framework components:

```bash
cd mobile-automation-parent
mvn test -pl automation-core
```

Covered components: `ConfigReader`, `EnvironmentConfig`, `CapabilityBuilder`, `SelectDecision`, `TestContext`, `TestDataFactory`.

## Adding a New App

1. Create a new module under `apps/` (e.g. `app-myapp`)
2. Add it as a `<module>` in `apps/pom.xml`
3. Define screens extending `BaseScreen`, flows extending `BaseFlow`, and a capabilities class
4. Add the app entry to `apps.json` with Android/iOS configs
5. Create feature files under `features/myapp/`
6. Write step definitions referencing your screens and flows
7. Run with `mvn test -pl automation-tests -Dapp=myapp -Dplatform=android`
