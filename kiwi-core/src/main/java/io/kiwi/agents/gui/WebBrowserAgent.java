package io.kiwi.agents.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Marker interface for web browser agents.
 */
public interface WebBrowserAgent {
    Logger logger = LogManager.getLogger(WebBrowserAgent.class);

    static Map<String, String> loadElements(String elementsYamlPath) {
        // Load elements from YAML file
        Map<String, String> elementRepo = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try{
            Map pageToElementMap = mapper.readValue(new File(elementsYamlPath), Map.class);
            for(Object pageName : pageToElementMap.keySet()){
                Map<String, String> elementMap = (Map<String, String>) pageToElementMap.get(pageName);
                for(String elementName : elementMap.keySet()){
                    String locator = elementMap.get(elementName);
                    elementRepo.put(pageName + "." + elementName, locator);
                }
            }
            return elementRepo;
        } catch (IOException e) {
            logger.error("Error loading Web UI elements from {}: {}", elementsYamlPath, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    static Map<String, String> loadElementRepo(String repoPath) {
        Path path = Path.of(repoPath);
        if(Files.isDirectory(path)){
            //load all yaml files in the directory
            Map<String, String> elementRepo = new HashMap<>();
            try{
                Files.list(path).filter(p -> p.toString().endsWith(".yml") || p.toString().endsWith(".yaml")).forEach(p -> {
                    loadElements(p.toString());
                });
                return elementRepo;
            } catch (IOException e) {
                logger.error("Error loading Web UI elements from directory {}: {}", repoPath, e.getMessage());
                throw new RuntimeException(e);
            }
        } else {
            return loadElements(repoPath);
        }
    }

    String getElementLocator(String elementName);

    void startBrowser(boolean headless);

    void stopBrowser();

    void navigateTo(String url);

    void click(String elementName);

    void type(String elementName, String text);

    String readText(String elementName);

    void press(String elementName, String key);

    boolean isVisible(String elementName);

    void waitForSelector(String elementName) throws InterruptedException;

    boolean isOnPage(String pageName);

    byte[] captureScreenshot();
}
