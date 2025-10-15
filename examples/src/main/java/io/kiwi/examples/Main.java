package io.kiwi.examples;

import io.kiwi.config.common.Configurator;
import io.kiwi.config.mock.WireMockAgentConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import io.kiwi.Runner;

import java.io.IOException;
import java.util.Map;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
        logger.info("Starting Regression Tests for sample app...");
        Map<String, String> parsedArgs = Runner.parseArgs(args);

        String env = parsedArgs.getOrDefault("env", Configurator.getInstance().getEnvironment());
        if(env.equals("dev")){
            WireMockAgentConfig config = new WireMockAgentConfig();
            config.setPort(9090);
//        config.setNeedClientAuth(true);
            WireMockConfiguration wireMockConfig = options()
                    .port(config.getPort())
                    .usingFilesUnderDirectory("data/wiremock");
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
            WireMockServer wireMockServer = new WireMockServer(wireMockConfig);
            wireMockServer.start();
            logger.info("WireMock server started at port {}", config.getPort());
        }

        String[] cucumberOptions = new String[]{
                "features",
                "--glue", "io.kiwi.glue",
                "--glue", "io.kiwi.examples.glue",
                "--plugin", "html:target/cucumber-reports.html",
                "--tags", parsedArgs.getOrDefault("tags", "")
        };

        Runner.run(cucumberOptions, parsedArgs);
    }
}