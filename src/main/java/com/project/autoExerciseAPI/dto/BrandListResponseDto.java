package com.project.autoExerciseAPI.dto;

import java.util.List;

public class BrandListResponseDto {
    private int responseCode;
    private List<BrandDto> brands;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public List<BrandDto> getBrands() {
        return brands;
    }

    public void setBrands(List<BrandDto> brands) {
        this.brands = brands;
    }
}
