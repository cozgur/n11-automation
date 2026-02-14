# CLAUDE.md - AI Assistant Guide for n11-automation

## Project Overview

This is a **Selenium-based BDD test automation framework** using Cucumber with Gherkin syntax. It automates browser-based UI testing with a Page Object Model pattern that stores element locators in JSON files. The project name references "n11" but currently targets Amazon.com as its test application.

- **Language:** Java 8
- **Build System:** Apache Maven
- **BDD Framework:** Cucumber 1.2.5 (`info.cukes`)
- **Browser Automation:** Selenium WebDriver 3.13.0
- **Test Runners:** JUnit 4.12, TestNG 6.14.3
- **Logging:** Log4j 1.2.17

## Repository Structure

```
n11-automation/
├── pom.xml                          # Maven build config and dependencies
├── README.md                        # Minimal project readme
├── CLAUDE.md                        # This file
├── tools/
│   └── drivers/
│       ├── chromedriver             # Chrome WebDriver binary (Linux/Mac)
│       └── geckodriver              # Firefox WebDriver binary (Linux/Mac)
└── src/test/
    ├── features/
    │   └── smoke.feature            # Gherkin feature files (BDD scenarios)
    ├── properties/
    │   └── log4j.properties         # Logging configuration
    └── java/
        ├── main/
        │   └── testRunner.java      # Cucumber JUnit test runner entry point
        ├── stepDefinitions/
        │   ├── seleniumStepDefinitions.java   # Browser interaction steps
        │   └── commonStepDefinitions.java     # Shared utility steps (e.g., wait)
        └── resources/
            ├── config/
            │   ├── driver/
            │   │   ├── browserManager.java    # Browser/driver initialization
            │   │   └── selectDecision.java    # Selenium selector strategy resolver
            │   └── utils/
            │       └── jsonParser.java        # JSON page object file reader
            ├── dictionary/
            │   └── pages.json                 # Page Object Model definitions (URLs + element XPaths)
            └── logger/
                └── LoggerManagement.java      # Centralized Log4j logger instance
```

## Build & Run Commands

```bash
# Install dependencies
mvn clean install

# Run all tests (excludes @smoke-tagged scenarios by default)
mvn test

# Run with a specific Cucumber tag
mvn test -Dcucumber.options="--tags @smoke"

# Compile only (no tests)
mvn compile
```

## Architecture & Key Patterns

### Test Runner (`src/test/java/main/testRunner.java`)
- Entry point: `testRunner` class annotated with `@RunWith(Cucumber.class)`
- Features path: `src/test/`
- Step definition glue: `stepDefinitions` package
- Default tag exclusion: `~@smoke` (smoke tests are skipped unless explicitly requested)

### Page Object Model via JSON (`src/test/java/resources/dictionary/pages.json`)
- Pages are defined as top-level JSON keys (e.g., `"n11"`)
- Each page has a `"url"` field and an `"elements"` object
- Elements map human-readable names to XPath selectors
- Step definitions reference elements by their JSON key names in Gherkin scenarios

### Browser Manager (`src/test/java/resources/config/driver/browserManager.java`)
- Package declared as `config` (note: differs from directory path `resources.config.driver`)
- Detects OS (Windows, Mac, Linux/Unix) to select correct driver binary
- Supports Chrome and Firefox browsers
- Chrome options: fullscreen, incognito, no-sandbox, test-type
- Firefox options: fullscreen, test-type
- Driver binaries expected at `tools/drivers/` (`.exe` suffix for Windows)

### Selector Strategy (`src/test/java/resources/config/driver/selectDecision.java`)
- Resolves a string selector type to a Selenium `By` object
- Supported types: `id`, `name`, `xpath`, `className`, `cssSelector`, `tagName`, `linkText`, `partialLinkText`
- Used in step definitions via `selectDecision.main(by, selectorType, locator)`

### Step Definitions

**`seleniumStepDefinitions.java`** - Primary browser interaction steps:
| Gherkin Pattern | Action |
|----------------|--------|
| `Given I use <browser> driver` | Initialize browser (chrome/firefox) |
| `When I open <page> page` | Navigate to URL from pages.json |
| `Then I refresh the page` | Refresh current page |
| `Then I see webpage title equals "<text>"` | Verify exact page title |
| `Then I see webpage title contains "<text>"` | Verify page title contains text |
| `Then I fill by <selector>` | Fill form fields (with DataTable) |
| `Then I see <element> equals to "<value>"` | Assert element text equals value |
| `When I wait for page` | Implicit wait (10 seconds) |
| `Then I open new tab` | Open and switch to new tab |
| `Then I close the tab` | Close current tab |
| `Then I switch the iFrame "<name>"` | Switch to iframe |
| `When I click <element>, link: <selector> opened in new tab` | Click link in new tab |
| `Then I select <option> dropdown option on element <element>` | Select dropdown value |

**`commonStepDefinitions.java`** - Utility steps:
| Gherkin Pattern | Action |
|----------------|--------|
| `When I wait for <N> seconds` | Thread.sleep for N seconds |

### Lifecycle Hooks (in `seleniumStepDefinitions.java`)
- `@Before`: Logs scenario name and increments counter
- `@After`: Quits the browser on success; tracks failed scenario count; does NOT quit browser on failure (for debugging)

## Writing Feature Files

Feature files go in `src/test/features/` using Gherkin syntax. Example:

```gherkin
Feature: Amazon search

  @smoke
  Scenario: Search for a product
    Given I use chrome driver
    When I open n11 page
    And I wait for page
    Then I see webpage title contains "Amazon"
    Then I fill by xpath
      | search field | laptop |
```

- The page key (e.g., `n11`) must match a key in `pages.json`
- Element keys in DataTables (e.g., `search field`) must match keys in the page's `"elements"` object
- The selector type in `I fill by <type>` must be one of: `id`, `name`, `xpath`, `className`, `cssSelector`, `tagName`, `linkText`, `partialLinkText`

## Adding New Pages

1. Add a new entry to `src/test/java/resources/dictionary/pages.json`:
```json
{
  "myPage": {
    "url": "https://example.com",
    "elements": {
      "loginButton": "//button[@id='login']",
      "usernameField": "//*[@id='username']"
    }
  }
}
```
2. Reference it in feature files: `When I open myPage page`

## Code Conventions

- **Class naming:** camelCase starting with lowercase (e.g., `testRunner`, `browserManager`, `jsonParser`) - this project does NOT follow standard Java PascalCase convention
- **Logger usage:** Always import `static resources.logger.LoggerManagement.LOGGER` and use `LOGGER.info()` for step tracing
- **Element locators:** Stored externally in JSON, not hardcoded in Java
- **Selector strategy:** Passed as a Gherkin step parameter, resolved at runtime via `selectDecision`
- **No assertions in some verification steps:** Some "I see" steps only log without asserting (e.g., `webpageTitleEquals` logs but does not throw on mismatch — while `webpageTitleNotEqual` does throw)

## Dependencies (from pom.xml)

| Dependency | Version | Purpose |
|-----------|---------|---------|
| selenium-* | 3.13.0 | Browser automation (chrome, firefox, safari, opera, edge, support, remote, api) |
| cucumber-java | 1.2.5 | Cucumber step definitions |
| cucumber-junit | 1.2.5 | JUnit Cucumber runner |
| cucumber-testng | 1.2.5 | TestNG Cucumber runner |
| testng | 6.14.3 | Test framework |
| junit | 4.12 | Test framework |
| log4j | 1.2.17 | Logging |
| gson | (transitive) | JSON parsing for page objects |

## Known Considerations

- The `browserManager` class has package `config` which does not match its directory path (`resources.config.driver`). The step definitions import it as `config.browserManager`.
- WebDriver binaries in `tools/drivers/` are for Linux/Mac only. Windows requires `.exe` variants.
- Chrome launches in incognito + fullscreen + no-sandbox mode by default.
- The `@After` hook does NOT call `webDriver.quit()` when a scenario fails, keeping the browser open for debugging.
- Cucumber version 1.2.5 uses the legacy `info.cukes` group ID (modern Cucumber uses `io.cucumber`).
- The `smoke.feature` file is currently empty (contains only `Feature:` with no scenarios).
- There is no CI/CD pipeline configured.
- There are no code style enforcement tools (no Checkstyle, SpotBugs, or similar).
