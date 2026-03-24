# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Repository Overview

This repository contains two projects:

1. **n11-automation** — Java-based multi-module mobile/web test automation framework (Appium + Cucumber + TestNG)
2. **FocusFlora** — React Native (Expo) mobile app with TypeScript and NativeWind (Tailwind CSS)

## n11-automation (Java)

### Build & Test Commands

```bash
# Build without tests
cd mobile-automation-parent && mvn clean install -DskipTests

# Run core framework unit tests
cd mobile-automation-parent && mvn test -pl automation-core

# Run app-specific tests
mvn test -pl automation-tests -Dapp=cloneai -Dplatform=android
mvn test -pl automation-tests -Dapp=funnelfox -Dplatform=ios

# Static analysis
cd mobile-automation-parent && mvn checkstyle:check pmd:check spotbugs:check

# Allure reporting
cd mobile-automation-parent && mvn allure:serve
```

### Architecture

- **Java 11, Maven multi-module** under `mobile-automation-parent/`
- Module dependency chain: `automation-tests` → `apps/*` → `automation-core`
- **automation-core**: Shared framework — driver management (ThreadLocal), Page Object base classes (`BaseScreen`, `BaseFlow`), config loading (hierarchical YAML: default → platform → environment), retry logic, wait helpers, REST Assured API client
- **apps/**: Per-application modules (e.g., `app-cloneai`, `app-funnelfox`) each with screens, flows, and capability configs
- **automation-tests**: Cucumber BDD test execution — step definitions (mobile/web/common), runners, hooks, feature files, and YAML/JSON config
- Config-driven execution: `-Dapp`, `-Dplatform`, `-DdeviceName`, `-DplatformVersion` system properties select app/platform at runtime
- App registry in `automation-tests/src/test/resources/dictionary/apps.json` maps app names to platform capabilities

### Adding a New App Module

Create module under `apps/`, register in `apps/pom.xml`, define screens/flows/capabilities, add entry to `apps.json`, write features under `features/<appname>/`.

## FocusFlora (React Native / Expo)

### Build & Dev Commands

```bash
cd FocusFlora
npx expo start          # Start dev server
npx expo start --ios    # iOS simulator
npx expo start --android # Android emulator
npm test                # Run Jest tests
npx expo lint           # Lint
```

### Architecture

- **Expo SDK** with TypeScript, file-based routing via `expo-router`
- **NativeWind** (Tailwind CSS) for styling
- Source layout: `src/components/`, `src/screens/`, `src/hooks/`, `src/services/`
- Navigation via React Navigation (expo-router)
- Main feature: circular timer with isometric garden visualization
