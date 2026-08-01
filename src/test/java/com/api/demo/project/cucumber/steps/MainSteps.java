package com.api.demo.project.cucumber.steps;

import com.api.demo.project.helpers.PayloadBuilder;
import com.api.demo.project.storage.MainResponseStorage;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

public class MainSteps {

    @Autowired
    private PayloadBuilder payloadBuilder;

    @Autowired
    private MainResponseStorage mainResponseStorage;

    @Given("I prepare the request payload {string}")
    public void given_IPrepareTheRequestPayload(String fileName) {
        String payload = payloadBuilder.prepareRequestPayload(fileName);

        // Here I am storing the payload for future use
        mainResponseStorage.setPayload(payload);
    }

    // Create the methods for GET, POST, PUT and DELETE using rest assured
}