package io.kiwi;


import io.cucumber.core.cli.Main;
import io.kiwi.config.common.Configurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class Runner {
    private static final Logger logger = LogManager.getLogger(Runner.class);

    /**
     * Extract tags from command line arguments
     * @param args
     * @return tags
     */
    public static String getTags(String[] args) {
        String tags = null;
        int i=0;
        for(; i<args.length; i++) {
            if(args[i].equals("--tags") && i+1 < args.length) {
                tags = args[i+1];
                i++;
                break;
            }
        }
        if(tags != null){
            for(; i<args.length; i++) {
                if(args[i].startsWith("--")) {
                    break;
                }
            }
        }
        return tags;
    }

    public static void run(String[] cucumberOptions) throws IOException {
        Configurator configurator = Configurator.getInstance();
        logger.info("Running Kiwi tests...");
        logger.info("Test Project Name: {}", configurator.getTestProjectName());
        logger.info("Environment: {}", configurator.getEnvironment());
        logger.info("Cucumber Options: \n{}", String.join(" ", cucumberOptions));

        Main.main(cucumberOptions);
    }
}