package io.kiwi.config.common;

import io.kiwi.agents.common.Agent;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mozilla.javascript.tools.shell.Environment;

import java.io.IOException;
import java.net.URL;

public class ConfiguratorTest {
    private static String configPath = null;

    @BeforeAll
    public static void setup() {
        URL resourcesPath = ConfiguratorTest.class.getResource("/");
        configPath = resourcesPath.getPath() + "config";
        System.setProperty(Configurator.PROPERTY_ROOT_CONFIG_FILE, configPath);
    }

    @Test
    public void testLoadConfig() throws IOException {
        Assertions.assertEquals("dev", Configurator.getInstance().getEnvironment());
        Assertions.assertEquals("sample-test-project", Configurator.getInstance().getTestProjectName());
    }

    @Test
    public void testGetAgent() throws IOException {
        AgentConfig agentConfig = Configurator.getInstance().getAgentConfig("SampleRestAgent");
        Assertions.assertNotNull(agentConfig);
        Assertions.assertEquals("io.kiwi.agents.api.RestAgent", agentConfig.getAgentClassName());
    }
}
