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
import static org.hamcrest.Matchers.*;

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

        String token = mainResponseStorage.getBearerToken();

        System.out.println("Token recebido no create user: " + token);

        if (token == null || token.isBlank()) {
            throw new IllegalStateException(
                    "O token não está disponível no MainResponseStorage."
            );
        }

        Response response =
               given()
                    .baseUri(baseUrl)
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .auth()
                    .oauth2(token)
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
        mainResponseStorage
             .getResponse()
             .then()
             .body("id", notNullValue())
             .body("id", greaterThan(0));
    }

    @And("the response should contain the correct user name")
    public void theResponseShouldContainTheCorrectUserName() {
        mainResponseStorage
                .getResponse()
                .then()
                .body("name", notNullValue())
                .body("name", greaterThan(""));
    }

    @And("the response should contain the correct user email")
    public void theResponseShouldContainTheCorrectUserEmail() {
        mainResponseStorage
                .getResponse()
                .then()
                .body("email", notNullValue())
                .body("email", greaterThan(""));
    }

    @And("the response should no contain the password")
    public void theResponseShouldNoContainThePassword() {
        mainResponseStorage
                .getResponse()
                .then()
                .body("$", not(hasKey("password")));
    }
}
