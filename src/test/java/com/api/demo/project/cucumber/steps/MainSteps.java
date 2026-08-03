package com.api.demo.project.cucumber.steps;

import com.api.demo.project.helpers.PayloadBuilder;
import com.api.demo.project.storage.MainResponseStorage;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import io.restassured.path.json.JsonPath;

import java.util.UUID;

public class MainSteps {

    private final Faker faker = new Faker();

    @Autowired
    private PayloadBuilder payloadBuilder;

    @Autowired
    private MainResponseStorage mainResponseStorage;

    @Given("I prepare the request payload {string}")
    public void given_IPrepareTheRequestPayload(String fileName) {
        String payload = payloadBuilder.prepareRequestPayload(fileName);

        payload = replaceDynamicValues(payload);

        // Here I am storing the payload for future use
        mainResponseStorage.setPayload(payload);

        String name = JsonPath.from(payload).getString("name");

        if(name != null && !name.isBlank()) {
            mainResponseStorage.setExpectedUserName(name);
        }
    }

    private String replaceDynamicValues(String payload) {
        if (payload.contains("{{randomName}}")) {
            payload = payload.replace(
                    "{{randomName}}",
                    faker.name().fullName()
            );
        }
        if (payload.contains("{{randomEmail}}")) {
            payload = payload.replace(
                    "{{randomEmail}}",
                    "test." + UUID.randomUUID() + "@example.com"
            );
        }
        return payload;
    }

    // Create the methods for GET, POST, PUT and DELETE using rest assured
}