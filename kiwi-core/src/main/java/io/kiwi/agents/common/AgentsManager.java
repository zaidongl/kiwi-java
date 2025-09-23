package io.kiwi.agents.common;

import io.kiwi.config.common.AgentConfig;
import io.kiwi.config.common.Configurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.HashMap;
import java.util.Map;

public class AgentsManager {
    private static final Logger logger = LogManager.getLogger(AgentsManager.class);
    private static AgentsManager instance;
    private final Map<String, Agent> agents;

    private AgentsManager() {
        agents = new HashMap<String, Agent>();
    }

    public static AgentsManager getInstance() {
        if (instance == null) {
            instance = new AgentsManager();
        }
        return instance;
    }

    public Agent getAgent(String agentName) {
        if (!agents.containsKey(agentName)) {
            try {
                AgentConfig agentConfig = Configurator.getInstance().getAgentConfig(agentName);
                Agent agent = (Agent)Class.forName(agentConfig.getAgentClassName()).getConstructor(agentConfig.getClass()).newInstance(agentConfig);
                agents.put(agentName, agent);
                logger.info("Agent {} created successfully.", agentName);
            } catch (Exception e) {
                logger.error("Error creating agent {}: {}", agentName, e.getMessage());
                return null;
            }
        }

        return agents.get(agentName);
    }
}
