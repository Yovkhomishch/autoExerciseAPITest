package com.project.autoExerciseAPITest.tests;

import com.project.autoExerciseAPI.api.BaseApiTestConfig;
import com.project.autoExerciseAPI.dto.ApiResponseDto;
import com.project.autoExerciseAPI.dto.BrandDto;
import com.project.autoExerciseAPI.dto.BrandListResponseDto;
import org.testng.Assert;
import org.testng.annotations.Test;

public class BrandsApiTest extends BaseApiTestConfig {

    @Test(description = "API 3: Get All Brands List using DTO")
    public void testGetAllBrandsList() {
        BrandListResponseDto responseDto = brandsApi.getAllBrands()
                .then()
                .statusCode(200)
                .extract()
                .as(BrandListResponseDto.class);

        Assert.assertEquals(responseDto.getResponseCode(), 200, "Response code should be 200");
        Assert.assertNotNull(responseDto.getBrands(), "Brands list should not be null");
        Assert.assertFalse(responseDto.getBrands().isEmpty(), "Brands list should not be empty");

        for (BrandDto brand : responseDto.getBrands()) {
            Assert.assertNotNull(brand.getBrand(), "Brand name should not be null for ID: " + brand.getId());
            Assert.assertFalse(brand.getBrand().isBlank(), "Brand name should not be blank for ID: " + brand.getId());
        }
    }

    @Test(description = "API 4: PUT To All Brands List (Unsupported Method)")
    public void testPutToAllBrandsList() {
        ApiResponseDto responseDto = brandsApi.updateBrandsNotAllowed()
                .then()
                .statusCode(200)
                .extract()
                .as(ApiResponseDto.class);

        Assert.assertEquals(responseDto.getResponseCode(), 405, "Response code should indicate unsupported method (405)");
        Assert.assertEquals(responseDto.getMessage(), "This request method is not supported.", "Error message should match");
    }
}
