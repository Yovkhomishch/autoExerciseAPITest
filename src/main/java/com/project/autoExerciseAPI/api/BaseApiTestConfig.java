package com.project.autoExerciseAPI.api;

import com.project.autoExerciseAPI.config.ConfigReader;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.parsing.Parser;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

public abstract class BaseApiTestConfig {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected RequestSpecification requestSpec;
    protected UsersApi usersApi;
    protected ProductsApi productsApi;
    protected BrandsApi brandsApi;

    static {

        RestAssured.registerParser("text/html", Parser.JSON);

        RestAssured.config = RestAssuredConfig.config()
                .objectMapperConfig(
                        ObjectMapperConfig.objectMapperConfig()
                                .defaultObjectMapperType(ObjectMapperType.JACKSON_2)
                );

        OutputStream threadSafeStream = new OutputStream() {
            private final ThreadLocal<ByteArrayOutputStream> threadBuffer =
                    ThreadLocal.withInitial(ByteArrayOutputStream::new);
            private final Logger restLog = LoggerFactory.getLogger("RestAssured");

            @Override
            public void write(int b) {
                threadBuffer.get().write(b);
            }

            @Override
            public void write(byte[] b, int off, int len) {
                threadBuffer.get().write(b, off, len);
            }

            @Override
            public void flush() {
                ByteArrayOutputStream buffer = threadBuffer.get();
                String message = buffer.toString(StandardCharsets.UTF_8);
                if (!message.trim().isEmpty()) {
                    restLog.info(message.trim());
                }
                buffer.reset();
            }

            @Override
            public void close() {
                threadBuffer.remove();
            }
        };

        PrintStream slf4jStream = new PrintStream(threadSafeStream, true);

        RestAssured.filters(
                new AllureRestAssured(),
                new RequestLoggingFilter(slf4jStream),
                new ResponseLoggingFilter(slf4jStream)
        );
    }

    @BeforeMethod(alwaysRun = true)
    public void setUpApi(Method method) {
        MDC.put("testName", method.getName());

        log.debug("Initializing API clients and request specification...");

        String baseUrl = ConfigReader.getRequiredProperty("base.url");

        requestSpec = new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .build();

        usersApi = new UsersApi(requestSpec);
        productsApi = new ProductsApi(requestSpec);
        brandsApi = new BrandsApi(requestSpec);

        log.debug("API clients successfully initialized for base URL: {}", baseUrl);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownApi() {
        MDC.clear();
    }
}