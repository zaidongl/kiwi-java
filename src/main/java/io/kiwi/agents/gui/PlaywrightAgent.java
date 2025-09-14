package io.kiwi.agents.gui;

import io.kiwi.config.gui.PlaywrightAgentConfig;
import io.kiwi.agents.common.Agent;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class PlaywrightAgent extends Agent implements WebBrowserAgent{
    private static final Logger logger = LogManager.getLogger(PlaywrightAgent.class);
    private final PlaywrightAgentConfig config;
    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;
    private Map<String, String> elementRepo;

    public PlaywrightAgent(PlaywrightAgentConfig config) {
        this.config = config;
        this.name = config.getName();
        loadElementRepo(config.getElementRepo());
        startBrowser();
    }

    private void loadElements(String elementsYamlPath) {
        // Load elements from YAML file
        elementRepo = new HashMap<>();
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
        } catch (IOException e) {
            logger.error("Error loading Web UI elements from {}: {}", elementsYamlPath, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void loadElementRepo(String repoPath) {
        Path path = Path.of(repoPath);
        if(Files.isDirectory(path)){
            //load all yaml files in the directory
            elementRepo = new HashMap<>();
            try{
                Files.list(path).filter(p -> p.toString().endsWith(".yml") || p.toString().endsWith(".yaml")).forEach(p -> {
                    loadElements(p.toString());
                });
            } catch (IOException e) {
                logger.error("Error loading Web UI elements from directory {}: {}", repoPath, e.getMessage());
                throw new RuntimeException(e);
            }
        } else {
            loadElements(repoPath);
        }
    }

    /**
     * Get the locator of an element by its name in the format of "PageName.ElementName"
     * @param name The name of the element in the format of "PageName.ElementName"
     * @return The locator of the element, can be xpath or selector
     */
    public String getElementLocator(String name){
        return elementRepo.get(name);
    }

    public void startBrowser() {
        startBrowser(this.config.getHeadless());
    }
    public void startBrowser(boolean headless) {
        playwright = Playwright.create();
        switch(this.config.getBrowserType().toLowerCase()) {
            case "chrome", "chromium" -> browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(headless));
            case "firefox" -> browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(headless));
            case "webkit", "safari" -> browser = playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(headless));
            default -> throw new IllegalArgumentException("Unsupported browser type: " + this.config.getBrowserType());
        }
        context = browser.newContext();
        page = context.newPage();
        page.setDefaultTimeout(this.config.getBrowserTimeout());
    }

    public void stopBrowser() {
        page.close();
        context.close();
        browser.close();
        playwright.close();
    }

    public void navigateTo(String url) {
        page.navigate(url);
    }

    public void click(String elementName) {
        String selector = getElementLocator(elementName);
        page.click(selector);
    }

    public void click(String elementName, Page.ClickOptions options) {
        String selector = getElementLocator(elementName);
        page.click(selector, options);
    }

    public void type(String elementName, String text) {
        String selector = getElementLocator(elementName);
        page.fill(selector, text);
    }

    public void press(String elementName, String key) {
        String selector = getElementLocator(elementName);
        page.press(selector, key);
    }

    public void readText(String elementName) {
        String selector = getElementLocator(elementName);
        String text = page.textContent(selector);
    }

    public boolean isVisible(String elementName) {
        String selector = getElementLocator(elementName);
        return page.isVisible(selector);
    }

    public void waitForSelector(String elementName) {
        waitForSelector(elementName, WaitForSelectorState.VISIBLE);
    }

    public void waitForSelector(String elementName, WaitForSelectorState state) {
        String selector = getElementLocator(elementName);
        page.waitForSelector(selector, new Page.WaitForSelectorOptions().setState(state));
    }

    public void isOnPage(String pageName){
        String pageRootLocator = getElementLocator(pageName + ".root");
        page.waitForSelector(pageRootLocator, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
    }

    public byte[] captureScreenshot() {
        return page.screenshot();
    }
}
