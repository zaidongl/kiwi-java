package io.kiwi.config.gui;

import io.kiwi.agents.gui.PlaywrightAgent;
import io.kiwi.config.common.AgentConfig;

public class PlaywrightAgentConfig extends AgentConfig{
    public static final String CONFIG_BROWSER_TYPE = "browser-type";
    public static final String CONFIG_HEADLESS = "headless";

    private String browserType = "chrome";
    private Boolean headless = true;
    private int browserTimeout = 30000;
    private String elementRepo = "data/web-gui-repo";

    public String getBrowserType() {
        return browserType;
    }

    public void setBrowserType(String browserType) {
        this.browserType = browserType;
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
        return PlaywrightAgent.class.getName();
    }
}
