package io.kiwi.agents.gui;

import io.kiwi.config.gui.PlaywrightAgentConfig;
import io.kiwi.agents.common.Agent;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class PlaywrightAgent extends Agent implements WebBrowserAgent{
    private static final Logger logger = LogManager.getLogger(PlaywrightAgent.class);
    private final PlaywrightAgentConfig config;
    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;
    private final Map<String, String> elementRepo;

    public PlaywrightAgent(PlaywrightAgentConfig config) {
        this.config = config;
        this.name = config.getName();
        elementRepo = WebBrowserAgent.loadElementRepo(config.getElementRepo());
        startBrowser();
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
        if(!browser.isConnected()){
            startBrowser();
        }
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

    public String readText(String elementName) {
        waitForSelector(elementName);
        String selector = getElementLocator(elementName);
        return page.textContent(selector);
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

    public boolean isOnPage(String pageName){
        String pageRootLocator = getElementLocator(pageName + ".root");
        page.waitForSelector(pageRootLocator, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
        return page.isVisible(pageRootLocator);
    }

    public byte[] captureScreenshot() {
        return page.screenshot();
    }
}
