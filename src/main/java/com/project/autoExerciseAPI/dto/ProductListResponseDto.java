package com.project.autoExerciseAPI.dto;

import java.util.List;

public class ProductListResponseDto {
    private int responseCode;
    private List<ProductDto> products;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public List<ProductDto> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDto> products) {
        this.products = products;
    }
}
