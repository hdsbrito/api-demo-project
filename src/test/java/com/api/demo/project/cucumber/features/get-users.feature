Feature: Get Users from the endpoint

    Background:
        Given I prepare the request payload "auth.json"
        When I send a POST request to the get-token endpoint

    Scenario: Get all users successfully
        When I send a GET request to the users endpoint
        Then the response status code should be 200
        And the response should contain a list of users

    Scenario: Get a Specific user by ID successfully
        When I send a GET request to the users endpoint with the user ID
        Then the response status code should be 200
        And the response should contain the correct user details for the specified ID