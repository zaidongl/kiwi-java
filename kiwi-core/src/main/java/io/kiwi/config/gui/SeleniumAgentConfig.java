package io.kiwi.config.gui;

import io.kiwi.agents.gui.SeleniumAgent;
import io.kiwi.config.common.AgentConfig;

public class SeleniumAgentConfig extends AgentConfig{
    public static final String CONFIG_WEB_DRIVER = "web-driver";
    public static final String CONFIG_HEADLESS = "headless";

    private String webDriver = "chrome";
    private Boolean headless = true;
    private int browserTimeout = 30000;
    private String elementRepo = "data/web-gui-repo";

    public String getWebDriver() {
        return webDriver;
    }

    public void setWebDriver(String webDriver) {
        this.webDriver = webDriver;
    }

    public Boolean getHeadless() {
        return headless;
    }

    public void setHeadless(Boolean headless) {
        this.headless = headless;
    }

    public int getBrowserTimeout() {
        return browserTimeout;
    }

    public void setBrowserTimeout(int browserTimeout) {
        this.browserTimeout = browserTimeout;
    }

    public String getElementRepo() {
        return elementRepo;
    }

    public void setElementRepo(String elementRepo) {
        this.elementRepo = elementRepo;
    }

    @Override
    public String getAgentClassName() {
        return SeleniumAgent.class.getName();
    }
}
