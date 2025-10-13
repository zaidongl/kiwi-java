package io.kiwi.config.common;

import com.esotericsoftware.yamlbeans.YamlReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Configurator {
    //constants
    public static final String PROPERTY_ROOT_CONFIG_FILE = "rootConfigFile";
    private static final String ROOT_CONFIG_FIELD_PROJECT_NAME = "test-project-name";
    private static final String ROOT_CONFIG_FIELD_ENVIRONMENT = "environment";
    private static final String PROPERTY_ENVIRONMENT = "env";

    private static Configurator instance;
    private static final Logger logger = LogManager.getLogger(Configurator.class);

    private String testProjectName;
    private String environment;

    private final Map<String, AgentConfig> agentConfigs;

    private Configurator() throws IOException {
        // Private constructor to prevent instantiation
        String configFolderPath = System.getProperty(PROPERTY_ROOT_CONFIG_FILE, "./config");
        agentConfigs = new HashMap<>();
        loadConfig(configFolderPath);
    }

    public static Configurator getInstance() throws IOException {
        if (instance == null) {
            instance = new Configurator();
        }
        return instance;
    }

    private void loadConfig(String configFolderPath) throws IOException {
        // Load configuration settings
        logger.info("Loading configuration from: {}", configFolderPath);
        String rootConfigFile = configFolderPath + "/config.yml";
        try(YamlReader reader = new YamlReader(new FileReader(rootConfigFile))){
            Object config = reader.read();
            if (!(config instanceof Map)) {
                throw new IOException("Invalid configuration format in " + rootConfigFile);
            }
            Map<String, Object> configMap = (Map<String, Object>) config;
            testProjectName = (String) configMap.get(ROOT_CONFIG_FIELD_PROJECT_NAME);
            //Get environment from system properties if it's passed from command line, read from config file by default
            environment = System.getProperty(PROPERTY_ENVIRONMENT, (String) configMap.get(ROOT_CONFIG_FIELD_ENVIRONMENT));
            logger.info("Root configuration loaded successfully.");
        }catch(IOException e){
            logger.error("Error loading root configuration file: {}", e.getMessage());
            throw e;
        }

        //load agents config
        String agentsConfigFile = configFolderPath + "/" + environment + "/agents.yml";
        loadAgentsConfig(agentsConfigFile);
    }

    private void loadAgentsConfig(String agentConfigFile) throws IOException {
        logger.info("Loading configuration for agents");
        try(YamlReader reader = new YamlReader(new FileReader(agentConfigFile))){
            Object config = reader.read();
            if (!(config instanceof ArrayList<?>)) {
                throw new IOException("Invalid configuration format in " + agentConfigFile);
            }
            for(AgentConfig item : (ArrayList<AgentConfig>) config) {
                agentConfigs.put(item.getName(), item);
            }
            logger.info("Configuration for agents loaded successfully.");
        }catch(IOException e){
            logger.error("Error loading configuration file for agents {}", e.getMessage());
            throw e;
        }
    }

    public String getTestProjectName() {
        return testProjectName;
    }

    public String getEnvironment() {
        return environment;
    }

    public AgentConfig getAgentConfig(String agentName) {
        return agentConfigs.get(agentName);
    }
}
