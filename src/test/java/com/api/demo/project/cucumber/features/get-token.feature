Feature: Authentication Token Generation

  Scenario: Generate a new authentication token successfully
    Given I prepare the request payload "auth.json"
    When I send a POST request to the get-token endpoint
    Then the response status code should be 200
    And the response should contain the generated token