package io.kiwi.config.mock;

import io.kiwi.agents.mock.WireMockAgent;
import io.kiwi.config.common.AgentConfig;

public class WireMockAgentConfig extends AgentConfig {
    private int port = 8080;
    private String host = "localhost";
    private boolean needClientAuth = false;
    private String keyStoreType = "JKS";
    private String keystorePath = "./config/mock/wiremock/keystore.jks";
    private String keystorePassword = "password";
    private String truststorePath = "./config/mock/wiremock/truststore.jks";
    private String truststorePassword = "password";
    private String mappingsPath = "./config/mock/wiremock";

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public boolean getNeedClientAuth() {
        return needClientAuth;
    }

    public void setNeedClientAuth(boolean needClientAuth) {
        this.needClientAuth = needClientAuth;
    }

    public String getKeyStoreType() {
        return keyStoreType;
    }

    public void setKeyStoreType(String keyStoreType) {
        this.keyStoreType = keyStoreType;
    }

    public String getKeystorePath() {
        return keystorePath;
    }

    public void setKeystorePath(String keystorePath) {
        this.keystorePath = keystorePath;
    }

    public String getKeystorePassword() {
        return keystorePassword;
    }

    public void setKeystorePassword(String keystorePassword) {
        this.keystorePassword = keystorePassword;
    }

    public String getTruststorePath() {
        return truststorePath;
    }

    public void setTruststorePath(String truststorePath) {
        this.truststorePath = truststorePath;
    }

    public String getTruststorePassword() {
        return truststorePassword;
    }

    public void setTruststorePassword(String truststorePassword) {
        this.truststorePassword = truststorePassword;
    }

    public String getMappingsPath() {
        return mappingsPath;
    }

    public void setMappingsPath(String mappingsPath) {
        this.mappingsPath = mappingsPath;
    }

    @Override
    public String getAgentClassName() {
        return WireMockAgent.class.getName();
    }
}
