package io.kiwi.security.auth;

public class BearerToken implements HttpRequestAuth{
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
