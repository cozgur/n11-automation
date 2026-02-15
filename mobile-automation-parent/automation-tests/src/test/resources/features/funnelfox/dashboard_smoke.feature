@mobile
Feature: FunnelFox Dashboard smoke tests

  # Usage:
  #   mvn test -Dapp=funnelfox -Dplatform=android
  #   mvn test -Dapp=funnelfox -Dplatform=ios

  @android @ios
  Scenario: Dashboard loads successfully
    Given I use mobile driver from config
    When I launch the mobile app
    Then I see orientation is portrait

  @android @ios
  Scenario: App survives backgrounding
    Given I use mobile driver from config
    When I launch the mobile app
    And I send the mobile app to background for 3 seconds
    Then I see orientation is portrait
