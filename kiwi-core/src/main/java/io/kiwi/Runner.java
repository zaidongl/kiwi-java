package io.kiwi;


import io.cucumber.core.cli.Main;
import io.kiwi.config.common.Configurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Runner {
    private static final Logger logger = LogManager.getLogger(Runner.class);

    /**
     * Extract tags from command line arguments
     * @param args
     * @return tags
     */
    @Deprecated
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

    /**
     * Parse arguments from command line arguments
     * @param args command line args
     * @return map with key as arg name and value as arg value
     */
    public static Map<String, String> parseArgs(String[] args) {
        Map<String, String> parsedArgs= new HashMap<>();
        int i=0;
        for(; i<args.length; i++) {
            if(args[i].equals("--tags") && i+1 < args.length) {
                String tags = args[i+1];
                parsedArgs.put("tags", tags);
                i++;
            }else if(args[i].equals("--env") && i+1 < args.length){
                String env = args[i+1];
                parsedArgs.put("env", env);
                i++;
            }
        }

        return parsedArgs;
    }

    public static void run(String[] cucumberOptions) throws IOException {
        run(cucumberOptions, null);
    }

    public static void run(String[] cucumberOptions, Map<String, String> properties) throws IOException {
        if(properties != null){
            for(String key : properties.keySet()){
                System.setProperty(key, properties.get(key));
            }
        }
        Configurator configurator = Configurator.getInstance();
        logger.info("Running Kiwi tests...");
        logger.info("Test Project Name: {}", configurator.getTestProjectName());
        logger.info("Environment: {}", configurator.getEnvironment());
        logger.info("Cucumber Options: \n{}", String.join(" ", cucumberOptions));

        Main.main(cucumberOptions);
    }
}