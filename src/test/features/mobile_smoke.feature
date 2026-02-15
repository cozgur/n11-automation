@mobile
Feature: Mobile smoke tests

  # ---------------------------------------------------------------
  # Config-driven: app and platform come from -D system properties.
  # Feature files stay clean — no hardcoded device/app details.
  #
  # Usage:
  #   mvn test -Dapp=cloneai -Dplatform=ios
  #   mvn test -Dapp=cloneai -Dplatform=android -DdeviceName="Samsung Galaxy S22"
  #   mvn test -Dapp=n11 -Dplatform=android -Dcucumber.options="--tags @android"
  # ---------------------------------------------------------------

  @android @ios
  Scenario: App launches successfully
    Given I use mobile driver from config
    When I launch the mobile app
    Then I see orientation is portrait

  @android @ios
  Scenario: Search for a product
    Given I use mobile driver from config
    When I launch the mobile app
    And I tap element with accessibility id "searchField"
    And I type "test" on element with accessibility id "searchField"
    And I press the enter key
    Then I see mobile element with accessibility id "searchResults" is displayed

  @android
  Scenario: Verify Android activity
    Given I use mobile driver from config
    When I launch the mobile app
    Then I see current activity is ".MainActivity"

  @ios
  Scenario: Verify portrait orientation on iOS
    Given I use mobile driver from config
    When I launch the mobile app
    Then I see orientation is portrait
    When I rotate to landscape
    Then I see orientation is landscape

  # ---------------------------------------------------------------
  # You can still use inline capabilities for one-off scenarios
  # or Scenario Outlines for multi-device when needed.
  # ---------------------------------------------------------------

  @android @ios
  Scenario: App survives backgrounding
    Given I use mobile driver from config
    When I launch the mobile app
    And I send the mobile app to background for 3 seconds
    Then I see orientation is portrait

  Scenario Outline: Launch on specific device override
    Given I use <platform> mobile driver on device "<deviceName>" version "<platformVersion>" with app "<app>"
    When I launch the mobile app
    Then I see orientation is portrait

    Examples:
      | platform | deviceName | platformVersion | app                        |
      | android  | Pixel 6    | 13.0            | /path/to/app-debug.apk     |
      | ios      | iPhone 14  | 16.0            | /path/to/app.ipa           |
