Feature: User Creation

  Background:
    Given I prepare the request payload "auth.json"
    When I send a POST request to the get-token endpoint

  Scenario: Create a new user successfully
    Given I prepare the request payload "create-user.json"
    When I send a POST request to the users endpoint
    Then the response status code should be 201
    And the response should contain the created user id
    And the response should contain the correct user name
    And the response should contain the correct user email
    And the response should no contain the password
