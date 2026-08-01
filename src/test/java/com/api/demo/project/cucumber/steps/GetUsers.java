package com.api.demo.project.cucumber.steps;

import com.api.demo.project.helpers.PayloadBuilder;
import com.api.demo.project.storage.MainResponseStorage;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Optional;

public class GetUsers {


    @Autowired
    private MainResponseStorage mainResponseStorage;

    @Autowired
    private PayloadBuilder payloadBuilder;

    @Value("${URL}")
    private String baseUrl;

    @Value("${USERS}")
    private String getUsersEndpoint;

    @When("I send a GET request to the users endpoint")
    public void iSendAGETRequestToTheUsersEndpoint() {
        String token = mainResponseStorage.getBearerToken();

        Response response =
            io.restassured.RestAssured.given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .auth()
                .oauth2(token)
                .log().all()
            .when()
                .get(getUsersEndpoint)
            .then()
                .log().all()
                .extract()
                .response();

        mainResponseStorage.setResponse(response);
        Assert.assertEquals(200, response.statusCode());

        Integer userId = response
                .jsonPath()
                .getInt("[0].id");

        mainResponseStorage.setUserId(userId);
    }

    @And("the response should contain a list of users")
    public void theResponseShouldContainAListOfUsers() {
        Response response = mainResponseStorage.getResponse();
        Assert.assertNotNull("Response is null", response);
        Assert.assertFalse("Response does not contain a list of users", response.jsonPath().getList("$").isEmpty());
    }

    @When("I send a GET request to the users endpoint with the user ID")
    public void iSendAGETRequestToTheUsersEndpointWithTheUserID() {
        String token = mainResponseStorage.getBearerToken();

        Integer userId = mainResponseStorage.getUserId();
        if (userId == null) {
            throw new IllegalStateException("User ID is not available in MainResponseStorage.");
        }

        Response response =
            io.restassured.RestAssured.given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .log().all()
                .auth()
                .oauth2(token)
            .when()
                .get(getUsersEndpoint + "/" + userId)
            .then()
                .log().all()
                .extract()
                .response();

        mainResponseStorage.setResponse(response);
        Assert.assertEquals(200, response.statusCode());
        mainResponseStorage.setUserId(userId);
    }

    @And("the response should contain the correct user details for the specified ID")
    public void theResponseShouldContainTheCorrectUserDetailsForTheSpecifiedID() {
        Integer userId = mainResponseStorage.getUserId();

        Response response = mainResponseStorage.getResponse();
        Assert.assertNotNull("User ID is null", userId);
        Assert.assertNotNull("Response is null", response);
    }
}
