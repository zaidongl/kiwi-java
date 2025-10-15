package io.kiwi.agents.mock;

import io.kiwi.agents.common.Agent;
import io.kiwi.config.mock.WireMockAgentConfig;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public class WireMockAgent extends Agent {
    private static final Logger logger = LogManager.getLogger(WireMockAgent.class);
    private final WireMockAgentConfig config;
    private final WireMockServer wireMockServer;

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
        logger.info("Starting WireMock server on port: {}", config.getPort());
        if (!wireMockServer.isRunning()) {
            wireMockServer.start();
            logger.info("WireMock server started successfully.");
        }
    }

    public void stop() {
        logger.info("Stopping WireMock server on port: {}", config.getPort());
        if (wireMockServer.isRunning()) {
            wireMockServer.stop();
            logger.info("WireMock server stopped successfully.");
        }
    }
}
