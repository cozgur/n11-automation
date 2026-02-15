# Test Coverage Analysis

## Executive Summary

This project is a **Selenium-based BDD test automation framework** using Cucumber and Java. After a thorough review, the most critical finding is that **the framework has zero executable test scenarios**. The feature file (`smoke.feature`) is empty, meaning no tests actually run. Beyond that, the framework infrastructure itself has several bugs, missing assertions, and architectural gaps that need to be addressed before it can serve as a reliable test suite.

---

## Current State

| Component | Files | Lines | Test Scenarios |
|-----------|-------|-------|----------------|
| Feature files | 1 (`smoke.feature`) | 1 (empty) | **0** |
| Step definitions | 2 | ~311 | N/A (definitions exist, but nothing invokes them) |
| Utility classes | 4 | ~140 | **0 unit tests** |
| Page data (JSON) | 1 | 25 | 1 page defined ("n11" pointing to amazon.com) |

### What exists
- Step definitions for: browser setup, page navigation, element fill, element click, text assertions, tab/frame management, dropdown selection, hover actions
- Browser management for Chrome and Firefox across Windows/Mac/Linux
- JSON-based element locator storage
- A selector strategy helper (`selectDecision`)
- Basic logging via Log4j

### What does not exist
- Any actual test scenario (the feature file is blank)
- Unit tests for any utility class
- Assertions in several step definitions that should be validating behavior
- Error handling or retry logic for flaky elements
- Explicit wait strategies
- CI/CD pipeline configuration
- Multi-page or multi-environment support

---

## Critical Bugs Found

### 1. Missing assertions in validation steps

**`seleniumStepDefinitions.java:84-100`** — `webpageTitleEquals` and `webpageTitleContains` only log when the condition is true but do nothing when it is false. These are supposed to be assertion steps but they silently pass on failure:

```java
// webpageTitleEquals — no failure path
if(expectedHeader.equals(currentHeader)) {
    LOGGER.info(...); // logs success
}
// If NOT equal, nothing happens — test silently passes
```

**Fix**: Add `Assert.assertEquals()` / `Assert.assertTrue()` calls.

### 2. Inverted logic in `webpageTitleContains`

**`seleniumStepDefinitions.java:97`** — The check is backwards:

```java
if(expectedHeader.contains(currentHeader))  // WRONG: checks if expected contains actual
```

Should be:
```java
if(currentHeader.contains(expectedHeader))  // CORRECT: checks if actual contains expected
```

### 3. Inverted logic in `webpageTitleNotEqual`

**`seleniumStepDefinitions.java:102-108`** — Throws an error when titles are NOT equal, but the step name says "does not equal" — meaning it should *pass* when they differ:

```java
// Throws when NOT equal — but the step says "I see title does not equal"
if(!expectedHeader.equals(currentHeader))
    throw new AssertionError(...);
```

### 4. Browser leak on test failure

**`seleniumStepDefinitions.java:46-49`** — When a scenario fails, `webDriver.quit()` is never called:

```java
if(scenerio.isFailed()){
    ++failedSceneriosCounter;
}else
    webDriver.quit();  // Only quits on SUCCESS
```

This leaks browser processes on every failure.

### 5. Unused parameter in `iSwitchFrame`

**`seleniumStepDefinitions.java:213-218`** — The regex captures a parameter `"([^"]*)"` but the method signature takes no arguments, so the frame identifier is ignored:

```java
@Then("^I switch the iFrame \"([^\"]*)\"$")
public void iSwitchFrame() {  // No parameter — always switches to first iframe
```

### 6. Error type mismatch in `selectDecision`

**`selectDecision.java:28-31`** — Throws `java.lang.Error` but catches `AssertionError`, so the catch block will never execute for the thrown error:

```java
throw new java.lang.Error("Not a valid selector type: %s" + decisionVariable);
// ...
} catch ( AssertionError e ) {  // Will NOT catch java.lang.Error
```

---

## Coverage Gaps & Recommendations

### Priority 1: Write actual test scenarios

The most urgent gap is that `smoke.feature` is empty. The framework has step definitions but nothing invokes them.

**Recommended feature files to create:**

| Feature File | Purpose | Scenarios |
|---|---|---|
| `smoke.feature` | Basic site availability | Open page, verify title, verify key elements present |
| `search.feature` | Search functionality | Search for items, verify results, pagination |
| `authentication.feature` | Login flow | Valid login, invalid credentials, empty fields |
| `cart.feature` | Shopping cart | Add to cart, remove from cart, verify quantities |
| `wishlist.feature` | Wishlist operations | Add to list, remove from list, verify items |

### Priority 2: Fix the assertion bugs

Several step definitions silently pass when they should fail. Every "I see" / "I verify" step must have a corresponding assertion that fails the test on mismatch. The specific bugs are listed above.

### Priority 3: Add unit tests for utility classes

None of the utility classes have unit tests. These are pure Java classes that can be tested without a browser:

| Class | What to test |
|---|---|
| `selectDecision` | All 8 selector types return correct `By` instances; invalid selector throws meaningful error |
| `jsonParser` | Valid JSON parses correctly; missing file throws meaningful error; malformed JSON handled |
| `browserManager` | OS detection logic; browser name matching; null return on unknown browser |

### Priority 4: Add a generic click step definition

The framework has `iFillBy` for filling elements and `iMouseHover` for hovering, but there is no standalone click step. The only click is `iClickedNewTab` which always opens in a new tab. A simple click step is fundamental:

```gherkin
Then I click on <element> by <selector>
```

### Priority 5: Replace implicit waits with explicit waits

**`seleniumStepDefinitions.java:207`** uses `implicitlyWait` which applies globally and can mask timing issues. The `commonStepDefinitions.java:10` uses `Thread.sleep()` which is a hard wait.

Both should be replaced with `WebDriverWait` + `ExpectedConditions` for reliable, targeted waits:

```java
new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(by));
```

### Priority 6: Implement Page Object Model properly

Currently all locators live in a flat JSON file (`pages.json`). This doesn't scale. Each page should have its own JSON file or, better, its own Page Object class that encapsulates elements and actions.

### Priority 7: Add negative / edge-case test scenarios

The framework has no tests for error conditions:
- What happens with invalid credentials?
- What happens when an element is not found?
- What happens with empty form submissions?
- What happens on network timeout?

### Priority 8: Add screenshot-on-failure capability

When a test fails, there is no screenshot captured. Adding `TakesScreenshot` support in the `@After` hook would make debugging failures much easier:

```java
@After
public void afterScenario(Scenario scenario) {
    if (scenario.isFailed()) {
        byte[] screenshot = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.BYTES);
        scenario.embed(screenshot, "image/png");
    }
    webDriver.quit(); // Always quit to prevent browser leaks
}
```

### Priority 9: Add multi-environment support

The test data is hardcoded to a single URL (`https://www.amazon.com`). The framework should support environment switching (dev, staging, prod) via Maven profiles or environment variables.

### Priority 10: Modernize dependencies

| Dependency | Current | Latest Stable | Risk |
|---|---|---|---|
| Selenium | 3.13.0 | 4.x | Missing modern features, W3C compliance, BiDi protocol |
| Cucumber | 1.2.5 (`info.cukes`) | 7.x (`io.cucumber`) | `info.cukes` group is deprecated and unmaintained |
| JUnit | 4.12 | 5.x | Missing parameterized tests, nested tests, better assertions |
| Log4j | 1.2.17 | 2.x | Log4j 1.x is end-of-life with known security vulnerabilities |

---

## Summary of Recommended Actions

| # | Action | Impact | Effort |
|---|--------|--------|--------|
| 1 | Write smoke test scenarios in feature files | Critical — currently 0 tests run | Medium |
| 2 | Fix assertion bugs in step definitions | Critical — tests silently pass on failure | Low |
| 3 | Fix browser leak in `@After` hook | High — orphaned browser processes | Low |
| 4 | Add unit tests for `selectDecision`, `jsonParser` | High — core utilities untested | Low |
| 5 | Add standalone click step definition | High — basic missing functionality | Low |
| 6 | Replace Thread.sleep / implicit waits with explicit waits | Medium — test reliability | Medium |
| 7 | Add screenshot-on-failure | Medium — debuggability | Low |
| 8 | Implement proper Page Object Model | Medium — maintainability at scale | Medium |
| 9 | Add negative/edge-case scenarios | Medium — coverage depth | Medium |
| 10 | Upgrade deprecated dependencies | Medium — security + features | High |
