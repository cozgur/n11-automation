package com.company.qa.core.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * REST API client wrapping RestAssured.
 * Provides simple GET, POST, PUT, DELETE methods.
 */
public class ApiClient {

    private final String baseUrl;

    public ApiClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Response get(String path) {
        return baseRequest()
                .get(path);
    }

    public Response post(String path, Object body) {
        return baseRequest()
                .body(body)
                .post(path);
    }

    public Response put(String path, Object body) {
        return baseRequest()
                .body(body)
                .put(path);
    }

    public Response delete(String path) {
        return baseRequest()
                .delete(path);
    }

    private RequestSpecification baseRequest() {
        return RestAssured.given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON);
    }
}
