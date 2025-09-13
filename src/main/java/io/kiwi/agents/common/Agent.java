package io.kiwi.agents.common;

public abstract class Agent {
    protected String name;

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }
}
