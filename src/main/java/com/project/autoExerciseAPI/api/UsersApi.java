package com.project.autoExerciseAPI.api;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class UsersApi {
    private final RequestSpecification requestSpec;

    public UsersApi(RequestSpecification requestSpec) {
        this.requestSpec = requestSpec;
    }

    public Response verifyLogin(String email, String password) {
        var request = given().spec(requestSpec);
        if (email != null) request.formParam("email", email);
        if (password != null) request.formParam("password", password);

        return request.when()
                .post("/api/verifyLogin");
    }

    public Response verifyLogin(Map<String, String> params) {
        var request = given().spec(requestSpec);
        if (params != null) {
            request.formParams(params);
        }
        return request.when()
                .post("/api/verifyLogin");
    }

    public Response deleteVerifyLogin() {
        return given()
                .spec(requestSpec)
                .when()
                .delete("/api/verifyLogin");
    }

    public Response createAccount(Map<String, String> accountDetails) {
        return given()
                .spec(requestSpec)
                .formParams(accountDetails)
                .when()
                .post("/api/createAccount");
    }

    public Response updateAccount(Map<String, String> accountDetails) {
        return given()
                .spec(requestSpec)
                .formParams(accountDetails)
                .when()
                .put("/api/updateAccount");
    }

    public Response getUserDetailByEmail(String email) {
        var request = given().spec(requestSpec);
        if (email != null) {
            request.queryParam("email", email);
        }
        return request.when()
                .get("/api/getUserDetailByEmail");
    }

    public Response deleteAccount(String email, String password) {
        var request = given().spec(requestSpec);
        if (email != null) request.formParam("email", email);
        if (password != null) request.formParam("password", password);

        return request.when()
                .delete("/api/deleteAccount");
    }
}