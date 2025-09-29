package io.kiwi.config.common;

import io.kiwi.agents.common.Agent;

public class AgentConfig {
    private String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getAgentClassName(){
        return Agent.class.getName();
    }
}
