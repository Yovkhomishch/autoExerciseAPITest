package com.project.autoExerciseAPI.dto;

public class CategoryDto {
    private String category;
    private UserTypeDto usertype;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public UserTypeDto getUsertype() {
        return usertype;
    }

    public void setUsertype(UserTypeDto usertype) {
        this.usertype = usertype;
    }
}
