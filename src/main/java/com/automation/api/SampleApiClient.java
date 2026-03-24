package com.automation.api;

import com.automation.config.EnvironmentConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

/**
 * Minimal hybrid example: hit base URL before/after UI flows (HTML or JSON).
 */
public final class SampleApiClient {

    private final RequestSpecification spec;

    public SampleApiClient(EnvironmentConfig env) {
        this.spec = given().baseUri(env.getApiBaseUrl());
    }

    public Response get(String path) {
        return spec.get(path).then().extract().response();
    }
}
