package com.project.autoExerciseAPITest.tests;

import com.project.autoExerciseAPI.api.BaseApiTestConfig;
import com.project.autoExerciseAPI.dto.ApiResponseDto;
import com.project.autoExerciseAPI.dto.UserDetailResponseDto;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Epic("Automation Exercise API")
@Feature("User Management")
public class UsersApiTest extends BaseApiTestConfig {

    private static final String DEFAULT_PASSWORD = "password123";

    @Test(description = "API 7: POST To Verify Login with valid details")
    @Story("User Login Verification")
    @Severity(SeverityLevel.CRITICAL)
    public void testVerifyLoginValidDetails() {
        String uniqueEmail = registerNewTestUser();

        try {
            ApiResponseDto responseDto = usersApi.verifyLogin(uniqueEmail, DEFAULT_PASSWORD)
                    .then()
                    .statusCode(200)
                    .extract()
                    .as(ApiResponseDto.class);

            Assert.assertEquals(responseDto.getResponseCode(), 200);
            Assert.assertEquals(responseDto.getMessage(), "User exists!");
        } finally {
            safeDelete(uniqueEmail, DEFAULT_PASSWORD);
        }
    }

    @Test(description = "API 8: POST To Verify Login without email parameter")
    @Story("User Login Verification")
    @Severity(SeverityLevel.NORMAL)
    public void testVerifyLoginWithoutEmail() {
        Map<String, String> params = new HashMap<>();
        params.put("password", DEFAULT_PASSWORD);

        ApiResponseDto responseDto = usersApi.verifyLogin(params)
                .then()
                .statusCode(200)
                .extract()
                .as(ApiResponseDto.class);

        Assert.assertEquals(responseDto.getResponseCode(), 400);
        Assert.assertEquals(responseDto.getMessage(), "Bad request, email or password parameter is missing in POST request.");
    }

    @Test(description = "API 9: DELETE To Verify Login (Unsupported Method)")
    @Story("User Login Verification")
    @Severity(SeverityLevel.NORMAL)
    public void testDeleteVerifyLogin() {
        ApiResponseDto responseDto = usersApi.deleteVerifyLogin()
                .then()
                .statusCode(200)
                .extract()
                .as(ApiResponseDto.class);

        Assert.assertEquals(responseDto.getResponseCode(), 405);
        Assert.assertEquals(responseDto.getMessage(), "This request method is not supported.");
    }

    @Test(description = "API 10: POST To Verify Login with invalid details")
    @Story("User Login Verification")
    @Severity(SeverityLevel.CRITICAL)
    public void testVerifyLoginInvalidDetails() {
        ApiResponseDto responseDto = usersApi.verifyLogin("nonexistent_user_99@example.com", "wrongpass")
                .then()
                .statusCode(200)
                .extract()
                .as(ApiResponseDto.class);

        Assert.assertEquals(responseDto.getResponseCode(), 404);
        Assert.assertEquals(responseDto.getMessage(), "User not found!");
    }

    @Test(description = "API 11: POST To Create/Register User Account with unique email")
    @Story("User Account Management")
    @Severity(SeverityLevel.CRITICAL)
    public void testCreateAccount() {
        String uniqueEmail = generateUniqueEmail();
        Map<String, String> userParams = createTestUserData(uniqueEmail);
        boolean isCreated = false;

        try {
            ApiResponseDto responseDto = usersApi.createAccount(userParams)
                    .then()
                    .statusCode(200)
                    .extract()
                    .as(ApiResponseDto.class);

            isCreated = (responseDto.getResponseCode() == 201);

            Assert.assertEquals(responseDto.getResponseCode(), 201);
            Assert.assertEquals(responseDto.getMessage(), "User created!");
        } finally {
            if (isCreated) {
                safeDelete(uniqueEmail, DEFAULT_PASSWORD);
            }
        }
    }

    @Test(description = "API 13: PUT To Update User Account and verify actual change")
    @Story("User Account Management")
    @Severity(SeverityLevel.NORMAL)
    public void testUpdateAccount() {
        String uniqueEmail = registerNewTestUser();

        Map<String, String> updateParams = createTestUserData(uniqueEmail);
        updateParams.put("firstname", "UpdatedFirstName");

        try {
            ApiResponseDto updateResponse = usersApi.updateAccount(updateParams)
                    .then()
                    .statusCode(200)
                    .extract()
                    .as(ApiResponseDto.class);

            Assert.assertEquals(updateResponse.getResponseCode(), 200);
            Assert.assertEquals(updateResponse.getMessage(), "User updated!");

            UserDetailResponseDto detailResponse = usersApi.getUserDetailByEmail(uniqueEmail)
                    .then()
                    .statusCode(200)
                    .extract()
                    .as(UserDetailResponseDto.class);

            Assert.assertEquals(detailResponse.getResponseCode(), 200);
            Assert.assertNotNull(detailResponse.getUser(), "User object should not be null");
            Assert.assertEquals(detailResponse.getUser().getFirstname(), "UpdatedFirstName",
                    "User firstname should be updated in the database");
        } finally {
            safeDelete(uniqueEmail, DEFAULT_PASSWORD);
        }
    }

    @Test(description = "API 14: GET user detail by email")
    @Story("User Account Management")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetUserDetailByEmail() {
        String uniqueEmail = registerNewTestUser();

        try {
            UserDetailResponseDto responseDto = usersApi.getUserDetailByEmail(uniqueEmail)
                    .then()
                    .statusCode(200)
                    .extract()
                    .as(UserDetailResponseDto.class);

            Assert.assertEquals(responseDto.getResponseCode(), 200);
            Assert.assertNotNull(responseDto.getUser(), "User object should not be null");
            Assert.assertEquals(responseDto.getUser().getEmail(), uniqueEmail);
            Assert.assertNotNull(responseDto.getUser().getName(), "User name should not be null");
            Assert.assertFalse(responseDto.getUser().getName().isBlank(), "User name should not be blank");
        } finally {
            safeDelete(uniqueEmail, DEFAULT_PASSWORD);
        }
    }

    @Test(description = "API 12: Self-contained DELETE User Account test")
    @Story("User Account Management")
    @Severity(SeverityLevel.CRITICAL)
    public void testDeleteAccount() {
        String uniqueEmail = generateUniqueEmail();
        Map<String, String> userParams = createTestUserData(uniqueEmail);
        boolean isCreated = false;

        try {
            ApiResponseDto createResponse = usersApi.createAccount(userParams)
                    .then()
                    .statusCode(200)
                    .extract()
                    .as(ApiResponseDto.class);

            isCreated = (createResponse.getResponseCode() == 201);
            Assert.assertEquals(createResponse.getResponseCode(), 201, "Setup: User should be created for deletion test");

            ApiResponseDto deleteResponse = usersApi.deleteAccount(uniqueEmail, DEFAULT_PASSWORD)
                    .then()
                    .statusCode(200)
                    .extract()
                    .as(ApiResponseDto.class);

            Assert.assertEquals(deleteResponse.getResponseCode(), 200);
            Assert.assertEquals(deleteResponse.getMessage(), "Account deleted!");
            isCreated = false;
        } finally {
            if (isCreated) {
                safeDelete(uniqueEmail, DEFAULT_PASSWORD);
            }
        }
    }

    private String registerNewTestUser() {
        String uniqueEmail = generateUniqueEmail();
        Map<String, String> userParams = createTestUserData(uniqueEmail);

        ApiResponseDto createResponse = usersApi.createAccount(userParams)
                .then()
                .statusCode(200)
                .extract()
                .as(ApiResponseDto.class);

        Assert.assertEquals(createResponse.getResponseCode(), 201, "Setup failed: User was not created successfully");
        Assert.assertEquals(createResponse.getMessage(), "User created!");

        return uniqueEmail;
    }

    private void safeDelete(String email, String password) {
        try {
            ApiResponseDto deleteResponse = usersApi.deleteAccount(email, password)
                    .then()
                    .statusCode(200)
                    .extract()
                    .as(ApiResponseDto.class);

            if (deleteResponse.getResponseCode() != 200) {
                log.warn("Failed to delete test user {}. Response message: {}",
                        email, deleteResponse.getMessage());
            }
        } catch (Exception e) {
            log.error("Exception occurred while deleting test user {}", email, e);
        }
    }

    private String generateUniqueEmail() {
        return "user_test_" + UUID.randomUUID() + "@example.com";
    }

    private Map<String, String> createTestUserData(String email) {
        Map<String, String> user = new HashMap<>();
        user.put("name", "Test User");
        user.put("email", email);
        user.put("password", DEFAULT_PASSWORD);
        user.put("title", "Mr");
        user.put("birth_date", "10");
        user.put("birth_month", "05");
        user.put("birth_year", "1990");
        user.put("firstname", "Kormin");
        user.put("lastname", "Tester");
        user.put("company", "QA Testing");
        user.put("address1", "Main Street 1");
        user.put("address2", "Apt 4");
        user.put("country", "United States");
        user.put("state", "California");
        user.put("city", "Sacramento");
        user.put("zipcode", "90001");
        user.put("mobile_number", "+1234567890");
        return user;
    }
}