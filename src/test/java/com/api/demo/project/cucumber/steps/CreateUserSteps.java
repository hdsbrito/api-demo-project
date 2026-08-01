package com.api.demo.project.cucumber.steps;

import com.api.demo.project.helpers.PayloadBuilder;
import com.api.demo.project.storage.MainResponseStorage;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import static io.restassured.RestAssured.given;

public class CreateUserSteps {

    @Autowired
    private PayloadBuilder payloadBuilder;

    @Autowired
    private MainResponseStorage mainResponseStorage;

    @Value("${URL}")
    private String baseUrl;

    @Value("${USERS}")
    private String usersEndpoint;
    
    @When("I send a POST request to the users endpoint")
    public void iSendAPOSTRequestToTheUsersEndpoint() {

        String payload = mainResponseStorage.getPayload();

        Response response =
               given()
                    .baseUri(baseUrl)
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .header("Authorization", "Bearer " + mainResponseStorage.getBearerToken())
                    .body(payload)
                    .log().all()
                .when()
                    .post(usersEndpoint)
                .then()
                    .log().all()
                    .extract()
                    .response();

        mainResponseStorage.setResponse(response);

        if(response.statusCode() >= 200 && response.statusCode() < 300) {
            String userId = response.jsonPath().getString("id");
            mainResponseStorage.setUserId(userId);
        }
    }

    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int arg0) {
        int actualStatusCode = mainResponseStorage.getResponse().getStatusCode();
        if (actualStatusCode != arg0) {
            throw new AssertionError("Expected status code: " + arg0 + ", but got: " + actualStatusCode);
        }
    }

    @And("the response should contain the created user id")
    public void theResponseShouldContainTheCreatedUserId() {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @And("the response should contain the correct user name")
    public void theResponseShouldContainTheCorrectUserName() {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @And("the response should contain the correct user email")
    public void theResponseShouldContainTheCorrectUserEmail() {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @And("the response should no contain the password")
    public void theResponseShouldNoContainThePassword() {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }


}
