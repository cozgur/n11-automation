@mobile
Feature: Mobile smoke tests across devices

  # ---------------------------------------------------------------
  # Scenario Outline: run the same test on multiple devices in parallel.
  # Each row in the Examples table becomes a separate scenario that
  # can execute on its own thread with its own ThreadLocal driver.
  # ---------------------------------------------------------------

  Scenario Outline: Verify app launches on <platform> - <deviceName>
    Given I use <platform> mobile driver on device "<deviceName>" version "<platformVersion>" with app "<app>"
    When I launch the mobile app
    Then I see orientation is portrait

    Examples: Android devices
      | platform | deviceName          | platformVersion | app                          |
      | android  | Pixel 6             | 13.0            | /path/to/app-debug.apk       |
      | android  | Samsung Galaxy S22  | 12.0            | /path/to/app-debug.apk       |
      | android  | Nexus 5X            | 11.0            | /path/to/app-debug.apk       |

    Examples: iOS devices
      | platform | deviceName  | platformVersion | app                          |
      | ios      | iPhone 14   | 16.0            | /path/to/app.ipa             |
      | ios      | iPhone 13   | 15.0            | /path/to/app.ipa             |

  # ---------------------------------------------------------------
  # DataTable capabilities: full control over desired capabilities
  # from the feature file. Any key-value pair is passed through.
  # ---------------------------------------------------------------

  Scenario: Launch Android app with custom capabilities
    Given I use android mobile driver with capabilities
      | deviceName      | Pixel 6              |
      | platformVersion | 13.0                 |
      | app             | /path/to/app.apk     |
      | appPackage      | com.example.app      |
      | appActivity     | .MainActivity        |
      | noReset         | true                 |
    When I launch the mobile app
    Then I see current activity is ".MainActivity"

  Scenario: Launch iOS app with custom capabilities
    Given I use ios mobile driver with capabilities
      | deviceName      | iPhone 14            |
      | platformVersion | 16.0                 |
      | app             | /path/to/app.ipa     |
      | bundleId        | com.example.app      |
      | noReset         | true                 |
    When I launch the mobile app
    Then I see orientation is portrait

  Scenario: Launch on remote Appium server with capabilities
    Given I use android mobile driver with capabilities
      | deviceName      | emulator-5554                      |
      | platformVersion | 13.0                               |
      | app             | /path/to/app.apk                   |
      | appiumUrl       | http://remote-server:4723/wd/hub   |
    When I launch the mobile app
    Then I see orientation is portrait

  # ---------------------------------------------------------------
  # Scenario Outline with capabilities: parameterize entire
  # capability sets across device farms.
  # ---------------------------------------------------------------

  Scenario Outline: Search on <deviceName>
    Given I use <platform> mobile driver on device "<deviceName>" version "<platformVersion>" with app "<app>"
    When I launch the mobile app
    And I tap element with accessibility id "searchField"
    And I type "n11" on element with accessibility id "searchField"
    And I press the enter key
    Then I see mobile element with accessibility id "searchResults" is displayed

    Examples:
      | platform | deviceName         | platformVersion | app                      |
      | android  | Pixel 6            | 13.0            | /path/to/app-debug.apk   |
      | android  | Samsung Galaxy S22 | 12.0            | /path/to/app-debug.apk   |
      | ios      | iPhone 14          | 16.0            | /path/to/app.ipa         |
