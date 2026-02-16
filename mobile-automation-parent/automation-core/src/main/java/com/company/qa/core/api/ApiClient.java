package com.company.qa.core.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * REST API client wrapping RestAssured for JSON-based HTTP communication.
 *
 * <p>All requests are sent with {@code Content-Type: application/json} and
 * {@code Accept: application/json} headers by default.</p>
 *
 * <p><b>Usage:</b></p>
 * <pre>{@code
 * ApiClient client = new ApiClient("https://api.example.com");
 * Response response = client.get("/users/1");
 * }</pre>
 */
public class ApiClient {

    private final String baseUrl;

    /**
     * Creates a new API client with the specified base URL.
     *
     * @param baseUrl the base URL for all requests (e.g. {@code "https://api.example.com"})
     */
    public ApiClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * Sends an HTTP GET request to the specified path.
     *
     * @param path the request path relative to the base URL
     * @return the HTTP {@link Response}
     */
    public Response get(String path) {
        return baseRequest()
                .get(path);
    }

    /**
     * Sends an HTTP POST request with the given body to the specified path.
     *
     * @param path the request path relative to the base URL
     * @param body the request body (serialized to JSON automatically)
     * @return the HTTP {@link Response}
     */
    public Response post(String path, Object body) {
        return baseRequest()
                .body(body)
                .post(path);
    }

    /**
     * Sends an HTTP PUT request with the given body to the specified path.
     *
     * @param path the request path relative to the base URL
     * @param body the request body (serialized to JSON automatically)
     * @return the HTTP {@link Response}
     */
    public Response put(String path, Object body) {
        return baseRequest()
                .body(body)
                .put(path);
    }

    /**
     * Sends an HTTP DELETE request to the specified path.
     *
     * @param path the request path relative to the base URL
     * @return the HTTP {@link Response}
     */
    public Response delete(String path) {
        return baseRequest()
                .delete(path);
    }

    /**
     * Creates a base request specification with the configured base URL,
     * JSON content type, and JSON accept header.
     *
     * @return a pre-configured {@link RequestSpecification}
     */
    private RequestSpecification baseRequest() {
        return RestAssured.given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON);
    }
}
