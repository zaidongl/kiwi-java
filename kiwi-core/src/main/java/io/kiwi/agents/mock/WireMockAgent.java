package io.kiwi.agents.mock;

import com.github.tomakehurst.wiremock.direct.DirectCallHttpServerFactory;
import io.kiwi.config.mock.WireMockAgentConfig;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public class WireMockAgent {
    private final WireMockAgentConfig config;
    private WireMockServer wireMockServer;

    public WireMockAgent(WireMockAgentConfig config) {
        this.config = config;
        WireMockConfiguration wireMockConfig = options().usingFilesUnderDirectory(config.getMappingsPath());
        if(config.getNeedClientAuth()){
            wireMockConfig.httpsPort(config.getPort())
                          .needClientAuth(true)
                          .keystorePath(config.getKeystorePath())
                          .keystorePassword(config.getKeystorePassword())
                          .trustStorePath(config.getKeystorePath())
                          .trustStorePassword(config.getKeystorePassword());
        } else {
            wireMockConfig.port(config.getPort());
        }
        this.wireMockServer = new WireMockServer(wireMockConfig);
    }

    public void start() {
        if (!wireMockServer.isRunning()) {
            wireMockServer.start();
        }
    }

    public void stop() {
        if (wireMockServer.isRunning()) {
            wireMockServer.stop();
        }
    }
}
