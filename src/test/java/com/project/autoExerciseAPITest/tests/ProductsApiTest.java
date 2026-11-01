package com.project.autoExerciseAPITest.tests;

import com.project.autoExerciseAPI.api.BaseApiTestConfig;
import com.project.autoExerciseAPI.dto.ApiResponseDto;
import com.project.autoExerciseAPI.dto.ProductDto;
import com.project.autoExerciseAPI.dto.ProductListResponseDto;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Automation Exercise API")
@Feature("Product Management")
public class ProductsApiTest extends BaseApiTestConfig {

    @Test(description = "API 1: Get All Products List using DTO")
    @Story("Get Products")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetAllProductsList() {
        ProductListResponseDto responseDto = productsApi.getAllProducts()
                .then()
                .statusCode(200)
                .extract()
                .as(ProductListResponseDto.class);

        Assert.assertEquals(responseDto.getResponseCode(), 200, "Response code should be 200");
        Assert.assertNotNull(responseDto.getProducts(), "Products list should not be null");
        Assert.assertFalse(responseDto.getProducts().isEmpty(), "Products list should not be empty");

        for (ProductDto product : responseDto.getProducts()) {
            verifyProductFields(product);
        }
    }

    @Test(description = "API 2: POST To All Products List (Unsupported Method)")
    @Story("Get Products")
    @Severity(SeverityLevel.NORMAL)
    public void testPostToAllProductsList() {
        ApiResponseDto responseDto = productsApi.postAllProducts()
                .then()
                .statusCode(200)
                .extract()
                .as(ApiResponseDto.class);

        Assert.assertEquals(responseDto.getResponseCode(), 405);
        Assert.assertEquals(responseDto.getMessage(),
                "This request method is not supported.");
    }

    @Test(description = "API 5: POST To Search Product")
    @Story("Search Products")
    @Severity(SeverityLevel.CRITICAL)
    public void testSearchProduct() {
        String searchQuery = "tshirt";
        ProductListResponseDto responseDto = productsApi.searchProduct(searchQuery)
                .then()
                .statusCode(200)
                .extract()
                .as(ProductListResponseDto.class);

        Assert.assertEquals(responseDto.getResponseCode(), 200);
        Assert.assertNotNull(responseDto.getProducts(), "Search results should not be null");
        Assert.assertFalse(responseDto.getProducts().isEmpty(), "Search results should not be empty");

        for (ProductDto product : responseDto.getProducts()) {
            verifyProductFields(product);
        }
    }

    @Test(description = "API 6: POST To Search Product without search_product parameter")
    @Story("Search Products")
    @Severity(SeverityLevel.NORMAL)
    public void testSearchProductWithoutParam() {
        ApiResponseDto responseDto = productsApi.searchProduct(null)
                .then()
                .statusCode(200)
                .extract()
                .as(ApiResponseDto.class);

        Assert.assertEquals(responseDto.getResponseCode(), 400);
        Assert.assertEquals(responseDto.getMessage(), "Bad request, search_product parameter is missing in POST request.");
    }

    private void verifyProductFields(ProductDto product) {
        Assert.assertNotNull(product, "Product should not be null");

        Assert.assertNotNull(product.getName(), "Product name should not be null");
        Assert.assertFalse(product.getName().isBlank(), "Product name should not be blank");

        Assert.assertNotNull(product.getPrice(), "Product price should not be null");
        Assert.assertFalse(product.getPrice().isBlank(), "Product price should not be blank");
    }
}
