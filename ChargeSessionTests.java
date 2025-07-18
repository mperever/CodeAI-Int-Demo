package com.example.tests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@ExtendWith(MockitoExtension.class)
public class ChargeSessionTests {

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:8080"; // Replace with your actual base URI
    }

    @Test
    void testGetChargingSessionById_Success() {
        // GIVEN - A valid Charge Session ID
        int sessionId = 1; // Replace with a valid session ID

        // WHEN - The GET request is made to retrieve the Charge Session by ID
        given().get("/api/chargesession/{id}", sessionId)
                .then()
                .statusCode(200)
                .body("id", equalTo(sessionId))
                .body("vin", notNullValue())
                .body("wattage", greaterThanOrEqualTo(0));
    }

    @Test
    void testGetChargingSessionById_NotFound() {
        // GIVEN - An invalid Charge Session ID
        int sessionId = 9999; // Replace with an invalid session ID

        // WHEN - The GET request is made to retrieve the Charge Session by ID
        given().get("/api/chargesession/{id}", sessionId)
                .then()
                .statusCode(404);
    }

    @Test
    void testGetAllChargeSessions_Success() {
        // GIVEN - No filter parameters are provided
        // WHEN - The GET request is made to retrieve all Charge Sessions
        given().get("/api/chargesession")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(1)); // Assert at least one session exists
    }

    @Test
    void testCreateChargeSession_Success() {
        // GIVEN - A valid Charge Session object
        String vin = "ABC12345";
        int wattage = 5;
        boolean completed = false;

        // WHEN - The POST request is made to create a new Charge Session
        given().body("{\"vin\": \"" + vin + "\", \"wattage\": " + wattage + ", \"completed\": " + completed + "}")
                .contentType("application/json")
                .post("/api/chargesession")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("vin", equalTo(vin))
                .body("wattage", equalTo(wattage))
                .body("completed", equalTo(completed));
    }

    @Test
    void testDeleteAllChargeSessions_Success() {
        // GIVEN - No filter parameters are provided
        // WHEN - The DELETE request is made to delete all Charge Sessions
        given().delete("/api/chargesession")
                .then()
                .statusCode(204); // 204 No Content indicates successful deletion
    }

}
