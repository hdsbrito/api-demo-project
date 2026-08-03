package com.api.demo.project.cucumber.steps;

import com.api.demo.project.helpers.PayloadBuilder;
import com.api.demo.project.storage.MainResponseStorage;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class UpdateUser {
    @Autowired
    private MainResponseStorage mainResponseStorage;

    @Autowired
    private PayloadBuilder payloadBuilder;

    @Value("${URL}")
    private String baseUrl;

    @Value("${USERS_BY_ID}")
    private String userByIdEndpoint;


    @And("I store the first user id from the response")
    public void iStoreTheFirstUserIdFromTheResponse() {
       Response response = mainResponseStorage.getResponse();

       response.then().statusCode(200);

       int usersCount = response
               .jsonPath()
               .getList("$")
               .size();

       if (usersCount == 0 ) {
           throw new IllegalStateException(
                   "The users list is empty. There is no user available to update"
           );
       }

       Integer userId = response
               .jsonPath()
               .getInt("[0].id");

       if (userId <= 0) {
           throw new IllegalStateException(
                   "The users response does not contain a valid first user id"
           );
       }

       mainResponseStorage.setUserId(userId);

    }

    @When("I send a PUT request to the stored user id")
    public void iSendAPUTRequestToTheStoredUserId() {
        Integer userId = mainResponseStorage.getUserId();
        String token = mainResponseStorage.getBearerToken();
        String payload = mainResponseStorage.getPayload();

        if (userId == null || userId <= 0) {
            throw new IllegalStateException(
                    "User ID is not available in MainResponseStorage"
            );
        }

        if (token == null || token.isBlank()) {
            throw new IllegalStateException(
                    "Bearer token is not available in the MainResponseStorage"
            );
        }

        String endpoint = userByIdEndpoint.replace(
                "<user_id>",
                userId.toString()
        );

        Response response =
                RestAssured.given()
                        .baseUri(baseUrl)
                        .contentType(ContentType.JSON)
                        .accept(ContentType.JSON)
                        .auth()
                        .oauth2(token)
                        .body(payload)
                        .log().all()
                .when()
                        .put(endpoint)
                .then()
                        .log().all()
                        .extract()
                        .response();

        mainResponseStorage.setResponse(response);
    }

    @And("the response {string} should be {string}")
    public void theResponseMessageShouldBe(String fieldName, String expectedValue) {
        String actualMessage = mainResponseStorage
                .getResponse()
                .jsonPath()
                .getString(fieldName);

        Assertions.assertNotNull(
                actualMessage,
                "The response does not contain the field '"
                        + fieldName
                        + "'. Response body"
                        + mainResponseStorage.getResponse().asString()
        );

        Assertions.assertEquals(
                expectedValue,
                actualMessage,
                "The value of response field '"
                        + fieldName
                        + "'does not match the expected value."
                );
    }


    @And("the response user name should match the name sent in the request")
    public void theResponseUserNameShouldMatchTheNameSentInTheRequest() {
       String expectedName = mainResponseStorage.getExpectedUserName();

       String actualName = mainResponseStorage
               .getResponse()
               .jsonPath()
               .getString("name");

       Assertions.assertNotNull(
               expectedName,
               "Expected username was not stored"
       );

       Assertions.assertEquals(
               expectedName,
               actualName,
               "User name in response does not match the name in the resquest"
       );
    }


}
