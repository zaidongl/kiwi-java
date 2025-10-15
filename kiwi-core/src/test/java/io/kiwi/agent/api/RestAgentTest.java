package io.kiwi.agent.api;

import io.kiwi.agents.api.RestAgent;
import io.kiwi.config.api.RestAgentConfig;
import io.kiwi.context.StepResult;
import io.kiwi.security.auth.BearerToken;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import static io.restassured.RestAssured.given;

import java.util.Map;

public class RestAgentTest {
    @Test
    public void sendRequestWithBearerTokenAuth() {
        RestAgentConfig config = Mockito.mock(RestAgentConfig.class);
        BearerToken token = Mockito.mock(BearerToken.class);
        Mockito.when(config.getBaseUrl()).thenReturn("http://localhost:8080");
        Mockito.when(config.getRequestAuth()).thenReturn(token);
        Mockito.when(token.getToken()).thenReturn("test-token");

        RestAgent agent = new RestAgent(config);

        Response response = Mockito.mock(Response.class);
        Mockito.when(response.getStatusCode()).thenReturn(200);

        try (MockedStatic<RestAssured> restAssuredMockedStatic = Mockito.mockStatic(RestAssured.class)) {
            RequestSpecification requestSpecification = Mockito.mock(RequestSpecification.class);
            Mockito.when(requestSpecification.config(Mockito.any())).thenReturn(requestSpecification);
            Mockito.when(requestSpecification.headers(Mockito.anyMap())).thenReturn(requestSpecification);
            Mockito.when(requestSpecification.when()).thenReturn(requestSpecification);
            Mockito.when(requestSpecification.get("http://localhost:8080/api/test")).thenReturn(response);

            restAssuredMockedStatic.when(RestAssured::given).thenReturn(requestSpecification);

            StepResult result = agent.sendRequest("GET", "/api/test", null, null, null);

            Assertions.assertEquals(StepResult.Status.PASSED, result.getStatus());
            Assertions.assertEquals(200, ((Response)result.getData()).getStatusCode());
        }
    }
}
