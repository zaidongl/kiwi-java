package io.kiwi.agents.gui;

import io.kiwi.agents.common.Agent;
import io.kiwi.config.gui.SeleniumAgentConfig;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
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
                ChromeOptions options = new ChromeOptions();
                if(headless){
                    options.addArguments("--headless");
                }
                this.driver = new ChromeDriver(options);
                break;
            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if(headless){
                    firefoxOptions.addArguments("--headless");
                }
                this.driver = new FirefoxDriver(firefoxOptions);
                break;
            case "edge":
                EdgeOptions edgeOptions = new EdgeOptions();
                if(headless){
                    edgeOptions.addArguments("--headless");
                }
                this.driver = new EdgeDriver(edgeOptions);
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
        TakesScreenshot screenshotTaker = (TakesScreenshot) this.driver;
        return screenshotTaker.getScreenshotAs(OutputType.BYTES);
    }
}
