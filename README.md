# Automation Exercise API Test Automation

A Java-based API test automation project for the [Automation
Exercise](https://www.automationexercise.com/) application. The project
uses REST Assured and TestNG to verify API behavior, validate response
data through DTOs, and generate test execution reports with Allure.

## Tech Stack

-   **Java** --- programming language
-   **Maven** --- build and dependency management
-   **REST Assured** --- REST API testing
-   **TestNG** --- test execution, assertions, and suite configuration
-   **Jackson** --- JSON serialization and deserialization
-   **AssertJ** --- fluent assertions (where used)
-   **Allure** --- test reports and HTTP request/response attachments
-   **SLF4J + Logback** --- application and test execution logging

## Project Scope

The tests cover the API endpoints provided by Automation Exercise,
including:

### Products

-   Get all products
-   Verify behavior when an unsupported HTTP method is used
-   Search for products
-   Verify the response when the required search parameter is missing

### Brands

-   Get all brands and validate the response.

### Users

-   Register a user
-   Verify login
-   Verify login with invalid or missing credentials
-   Get user details by email
-   Update account information
-   Delete an account
-   Verify unsupported or invalid account operations

The exact set of scenarios may evolve as the project grows.

## Project Structure

The code is organized into API clients, configuration, DTOs, and test
classes.

``` text
src
└── test
    ├── java
    │   └── com
    │       └── project
    │           ├── autoExerciseAPI
    │           │   ├── api
    │           │   ├── config
    │           │   └── dto
    │           └── autoExerciseAPITest
    │               └── tests
    └── resources
        ├── config.properties
        └── testng.xml
```

> The tree above describes the main package areas. Actual class names
> and resource locations may differ as the project develops.

## Configuration

The base URL is configured through the project properties file.

Example:

``` properties
base.url=https://www.automationexercise.com
```

Keep environment-specific values in configuration rather than
hard-coding them in test methods. Do not commit credentials, personal
data, or other secrets to the repository.

## Running the Tests

### Prerequisites

-   JDK compatible with the Java version configured in `pom.xml`
-   Maven
-   Internet access to reach Automation Exercise

Check the installed versions:

``` bash
java -version
mvn -version
```

### Run the test suite

From the project root directory:

``` bash
mvn clean test
```

The Maven Surefire plugin runs the TestNG suite according to the project
configuration.

## Test Execution and Parallelism

TestNG is used to organize and execute the tests. The suite is
configured for parallel execution, so independent test methods can run
concurrently.

Tests that create user accounts use unique email addresses to reduce
conflicts between parallel executions. Created test accounts should be
deleted during cleanup to avoid leaving unnecessary test data on the
application.

## Reporting with Allure

Allure annotations are used to organize tests in the report, for
example:

-   `@Epic` --- high-level project area
-   `@Feature` --- functional area
-   `@Story` --- user-facing behavior or scenario group
-   `@Severity` --- business impact of a failure

The REST Assured integration can attach HTTP request and response
details to test results.

After running the tests, Allure result files are normally written to:

``` text
target/allure-results
```

If the Allure command-line tool is installed, generate and open the
report with:

``` bash
allure serve target/allure-results
```

The `allure serve` command starts a local report server and opens the
report in a browser. The report can only be generated when the test run
has produced Allure result files.

## Logging

SLF4J with Logback is used for test execution logging. Logs help
investigate request/response behavior and diagnose failures. Request and
response logging is configured through REST Assured filters.

## Notes

-   The Automation Exercise API may return JSON content with a
    `text/html` response content type for some endpoints. The project
    configures REST Assured to parse the relevant responses as JSON.
-   API-level `responseCode` values in the response body are validated
    separately from the actual HTTP status code. They are not
    necessarily the same.
-   Tests should validate observable API behavior and avoid relying on
    unstable or undocumented response details.

## Project Goal

The goal of this project is to practice maintainable API test automation
with Java, REST Assured, and TestNG, using reusable API components,
DTO-based response mapping, configuration management, parallel
execution, logging, and test reporting.
