Feature: Update User By Id

  Background:
    Given I prepare the request payload "auth.json"
    When I send a POST request to the get-token endpoint

  Scenario: Update the name of an existing user successfully
    When I send a GET request to the users endpoint
    And I store the first user id from the response
    Given I prepare the request payload "update-user.json"
    When I send a PUT request to the stored user id
    Then the response status code should be 200
    And the response "message" should be "User updated successfully"
    When I send a GET request to the users endpoint with the user ID
    And the response user name should match the name sent in the request
