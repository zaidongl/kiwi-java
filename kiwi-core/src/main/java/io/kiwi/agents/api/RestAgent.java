package io.kiwi.agents.api;

import static io.restassured.RestAssured.given;

import io.kiwi.agents.common.Agent;
import io.kiwi.config.api.RestAgentConfig;
import io.kiwi.context.StepResult;
import io.restassured.config.*;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

/*
    REST API agent based on Rest Assured to interact with RESTful services.
 */
public class RestAgent extends Agent {
    private final RestAgentConfig agentConfig;

    public RestAgent(RestAgentConfig config) {
        this.agentConfig = config;
        this.name = config.getName();
    }

    public StepResult sendRequest(String method, String url, Map<String, String> headers, Map<String, String> params, String body) {
        String fullUrl = agentConfig.getBaseUrl() + url;
        RequestSpecification request = given().config(RestAssuredConfig.config()
            .httpClient(HttpClientConfig.httpClientConfig()
                .setParam("http.connection.timeout", agentConfig.getConnectionTimeout())));

        if(headers != null && !headers.isEmpty()){
            request.headers(headers);
        }
        if(params != null && !params.isEmpty()){
            request.params(params);
        }
        if (body != null && !body.isEmpty()) {
            request.body(body);
        }

        Response response;
        switch (method.toUpperCase()) {
            case "GET":
                response = request.when().get(fullUrl);
                break;
            case "POST":
                response = request.when().post(fullUrl);
                break;
            case "PUT":
                response = request.when().put(fullUrl);
                break;
            case "DELETE":
                response = request.when().delete(fullUrl);
                break;
            case "PATCH":
                response = request.when().patch(fullUrl);
                break;
            default:
                return new StepResult(StepResult.Status.PASSED, "Unsupported HTTP method: " + method, null);
        }

        return new StepResult(StepResult.Status.PASSED, "", response);
    }
}
