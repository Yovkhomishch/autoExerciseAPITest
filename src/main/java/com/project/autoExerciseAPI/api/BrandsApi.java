package com.project.autoExerciseAPI.api;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class BrandsApi {
    private final RequestSpecification requestSpec;

    public BrandsApi(RequestSpecification requestSpec) {
        this.requestSpec = requestSpec;
    }

    public Response getAllBrands() {
        return given()
                .spec(requestSpec)
                .when()
                .get("/api/brandsList");
    }

    public Response updateBrandsNotAllowed() {
        return given()
                .spec(requestSpec)
                .when()
                .put("/api/brandsList");
    }
}
