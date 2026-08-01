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

public class GetToken {

    @Autowired
    private MainResponseStorage mainResponseStorage;

    @Autowired
    private PayloadBuilder payloadBuilder;

    @Value("${URL}")
    private String baseUrl;

    @Value("${TOKEN}")
    private String getTokenEndpoint;

    @When("I send a POST request to the get-token endpoint")
    public void iSendAPOSTRequestToTheGetTokenEndpoint() {

        String payload = mainResponseStorage.getPayload();

        Response response =
                io.restassured.RestAssured.given()
                        .baseUri(baseUrl)
                        .contentType(ContentType.JSON)
                        .accept(ContentType.JSON)
                        .body(payload)
                        .log().all()
                .when()
                        .post(getTokenEndpoint)
                .then()
                        .log().all()
                        .extract()
                        .response();

        mainResponseStorage.setResponse(response);

        if(response.statusCode() >= 200 && response.statusCode() < 300) {
            String token = response.jsonPath().getString("token");
            mainResponseStorage.setBearerToken(token);
        }
    }

    @And("the response should contain the generated token")
    public void theResponseShouldContainTheGeneratedToken() {
        Assert.assertNotNull("Response does not contain token",
                mainResponseStorage
                        .getResponse()
                        .jsonPath()
                        .getString("token"));
    }
}
