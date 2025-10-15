package io.kiwi.examples;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import io.kiwi.Runner;

import java.io.IOException;
import java.util.Map;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
        logger.info("Starting Regression Tests for sample app...");
        Map<String, String> parsedArgs = Runner.parseArgs(args);

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