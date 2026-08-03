Feature: Delete an existing user by user ID

  Background:
    Given I prepare the request payload "auth.json"
    When I send a POST request to the get-token endpoint

  Scenario: delete the user By ID successfully
    When I send a GET request to the users endpoint
    And I store the first user id from the response
    When I send a DELETE request to the stored user id
    Then the response status code should be 200
    And the response "message" should be "User deleted successfully"
    When I send a GET request to the users endpoint with the user ID
    Then the response status code should be 404
    And the response "error" should be "User not found"


