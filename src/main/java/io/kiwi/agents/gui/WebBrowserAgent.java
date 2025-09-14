package io.kiwi.agents.gui;

/**
 * Marker interface for web browser agents.
 */
public interface WebBrowserAgent {
    String getElementLocator(String elementName);

    void startBrowser(boolean headless);

    void stopBrowser();

    void navigateTo(String url);

    void click(String elementName);

    void type(String elementName, String text);

    void readText(String elementName);

    void press(String elementName, String key);

    boolean isVisible(String elementName);

    void waitForSelector(String elementName);

    void isOnPage(String pageName);

    byte[] captureScreenshot();
}
