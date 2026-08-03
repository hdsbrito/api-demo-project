package com.api.demo.project.cucumber.steps;

import com.api.demo.project.helpers.PayloadBuilder;
import com.api.demo.project.storage.MainResponseStorage;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class DeleteUser {

    @Autowired
    private MainResponseStorage mainResponseStorage;

    @Autowired
    private PayloadBuilder payloadBuilder;

    @Value("${URL}")
    private String baseUrl;

    @Value("${USERS_BY_ID}")
    private String userByIdEndpoint;

    @When("I send a DELETE request to the stored user id")
    public void iSendADELETERequestToTheStoredUserId() {

        String token = mainResponseStorage.getBearerToken();
        Integer userId = mainResponseStorage.getUserId();

        if (userId == null || userId <= 0) {
            throw new IllegalStateException(
                    "User ID is not available in MainResponseStorage"
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
                   .log().all()
               .when()
                   .delete(endpoint)
               .then()
                   .log().all()
                   .extract()
                   .response();

           mainResponseStorage.setResponse(response);
        }
}
