package com.project.autoExerciseAPI.api;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class ProductsApi {
    private final RequestSpecification requestSpec;

    public ProductsApi(RequestSpecification requestSpec) {
        this.requestSpec = requestSpec;
    }

    public Response getAllProducts() {
        return given()
                .spec(requestSpec)
                .when()
                .get("/api/productsList");
    }

    public Response postAllProducts() {
        return given()
                .spec(requestSpec)
                .when()
                .post("/api/productsList");
    }

    public Response searchProduct(String searchQuery) {
        var request = given().spec(requestSpec);

        if (searchQuery != null && !searchQuery.isBlank()) {
            request.formParam("search_product", searchQuery);
        }

        return request.when()
                .post("/api/searchProduct");
    }
}
