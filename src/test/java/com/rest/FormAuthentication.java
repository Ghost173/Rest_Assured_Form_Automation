package com.rest;

import io.restassured.RestAssured;
import io.restassured.authentication.FormAuthConfig;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.session.SessionFilter;
import io.restassured.response.Response;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;


public class FormAuthentication {

    @BeforeClass
    public void beforeClass() {
        throw new SkipException("Skipping all tests due to filterAi class logic.");

//        requestSpecification = new RequestSpecBuilder()
//                .setRelaxedHTTPSValidation()  // this will skip if there are no SSL
//                .setBaseUri("https://localhost:8443")
//                .build();
    }

    @Test
    public void from_authentication_using_csrf_token() {
        SessionFilter filterS = new SessionFilter();

                 given().
                auth().form("dan", "dan123", new FormAuthConfig("/signin", "txtUsername", "txtPassword")).
                filter(filterS).
                log().all().
                when().
                get("/login")
                .then().
                 assertThat()
                .statusCode(200)
                .extract().response();

        // Check if the session filter also captured the session
        System.out.println("Session ID from filter: " + filterS.getSessionId());

        given()
                .sessionId(filterS.getSessionId())
                .log().all()
                .get("/profile/index")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200);
    }
}
