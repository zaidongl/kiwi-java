package io.kiwi.agent.common;

import io.kiwi.agents.api.RestAgent;
import io.kiwi.agents.common.Agent;
import io.kiwi.agents.common.AgentsManager;
import io.kiwi.config.common.Configurator;
import io.kiwi.config.common.ConfiguratorTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URL;

public class AgentsManagerTest {
    private static String configPath;

    @BeforeAll
    public static void setup() {
        URL resourcesPath = ConfiguratorTest.class.getResource("/");
        configPath = resourcesPath.getPath() + "config";
        System.setProperty(Configurator.PROPERTY_ROOT_CONFIG_FILE, configPath);
    }

    @Test
    public void testGetAgent() {
        Agent agent1 = AgentsManager.getInstance().getAgent("SampleRestAgent");
        Assertions.assertNotNull(agent1);
        Assertions.assertEquals(RestAgent.class.getName(), agent1.getClass().getName());
        Agent agent2 = AgentsManager.getInstance().getAgent("SampleRestAgent");
        Assertions.assertSame(agent1, agent2);
    }
}
