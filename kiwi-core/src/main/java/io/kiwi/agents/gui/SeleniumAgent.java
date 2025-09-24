package io.kiwi.agents.gui;

import io.kiwi.agents.common.Agent;
import io.kiwi.config.gui.SeleniumAgentConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Map;

public class SeleniumAgent extends Agent implements WebBrowserAgent{
    private WebDriver driver = null;
    private final SeleniumAgentConfig config;
    private final Map<String, String> elementRepo;

    public SeleniumAgent(SeleniumAgentConfig config) {
        this.config = config;
        this.elementRepo = WebBrowserAgent.loadElementRepo(config.getElementRepo());
        startBrowser(config.getHeadless());
    }

    @Override
    public String getElementLocator(String elementName) {
        return this.elementRepo.get(elementName);
    }

    @Override
    public void startBrowser(boolean headless) {
        switch(config.getWebDriver()){
            case "chrome":
                this.driver = new ChromeDriver();
                break;
            case "firefox":
                this.driver = new FirefoxDriver();
                break;
            case "edge":
                this.driver = new EdgeDriver();
                break;
            default:
                throw new IllegalArgumentException("Unsupported WebDriver: " + config.getWebDriver());
        }
    }

    @Override
    public void stopBrowser() {
        if(this.driver != null){
            this.driver.quit();
            this.driver = null;
        }
    }

    @Override
    public void navigateTo(String url) {
        if(this.driver != null){
            this.driver.get(url);
        }
    }

    @Override
    public void click(String elementName) {
        if(this.driver != null){
            String locator = getElementLocator(elementName);
            this.driver.findElement(By.cssSelector(locator)).click();
        }
    }

    @Override
    public void type(String elementName, String text) {
        if(this.driver != null){
            String locator = getElementLocator(elementName);
            this.driver.findElement(By.cssSelector(locator)).sendKeys(text);
        }
    }

    @Override
    public String readText(String elementName) {
        if(this.driver != null){
            String locator = getElementLocator(elementName);
            waitForSelector(elementName);
            return this.driver.findElement(By.cssSelector(locator)).getText();
        }
        return null;
    }

    @Override
    public void press(String elementName, String key) {
        if(this.driver != null){
            String locator = getElementLocator(elementName);
            this.driver.findElement(By.cssSelector(locator)).sendKeys(key);
        }
    }

    @Override
    public boolean isVisible(String elementName) {
        if(this.driver != null){
            String locator = getElementLocator(elementName);
            return this.driver.findElement(By.cssSelector(locator)).isDisplayed();
        }
        return false;
    }

    @Override
    public void waitForSelector(String elementName) {
        if(this.driver != null){
            String locator = getElementLocator(elementName);
            WebDriverWait webDriverWait = new WebDriverWait(this.driver, Duration.ofMillis(config.getBrowserTimeout()));
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(locator)));
        }
    }

    @Override
    public boolean isOnPage(String pageName) {
        if(this.driver != null){
            String pageRootLocator = getElementLocator(pageName + ".root");
            WebDriverWait wait = new WebDriverWait(this.driver, Duration.ofMillis(config.getBrowserTimeout()));
            wait.until(driver -> driver.findElement(By.cssSelector(pageRootLocator)).isDisplayed());
            return true;
        }
        return false;
    }

    @Override
    public byte[] captureScreenshot() {
        return null; // To be implemented
    }
}
