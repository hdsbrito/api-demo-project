package com.api.demo.project.storage;

import com.api.demo.project.annotation.LazyComponent;
import groovy.lang.GString;
import io.restassured.response.Response;
import lombok.Data;
import lombok.Getter;
import org.springframework.stereotype.Component;

/**
 * Singleton storage component used to temporarily hold data
 * The data stored here will be available during the entire run
 **/
@Data
@LazyComponent
public class MainResponseStorage {

    // For example, store here the bearer token, it will then be available for all the scenarios
    // You can then use it like "mainResponseStorage.getBearerToken()"
    private String bearerToken;
    private String payload;
    private Integer userId;
    private Response response;
    private String expectedUserName;
    
    public String getPayload() {
        return payload;
    }
    
    public void setPayload(String payload) {
        this.payload = payload;
    }
    public String getBearerToken() {
        return bearerToken;
    }
    
    public void setBearerToken(String token) {
        this.bearerToken = token;
    }
    
    public Integer getUserId() {
        return userId;
    }
    
    public void setUserId(Integer userId){
        this.userId = userId;
    }
    
    public Response getResponse() {
        return response;
    }
    public void setResponse(Response response) {
        this.response = response;
    }

    public String getExpectedUserName() {
        return expectedUserName;
    }

    public void setExpectedUserName(String expectedUserName) {
        this.expectedUserName = expectedUserName;
    }
    
}