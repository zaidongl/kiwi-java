package io.kiwi.config.data.sql;

import io.kiwi.agents.data.sql.SqlDbAgent;
import io.kiwi.config.common.AgentConfig;

public class SqlDbAgentConfig extends AgentConfig {
    private String databaseType;
    private String connectionUrl;
    private String username;
    private String password;
    private Integer connectionTimeout = 30000;
    private String driverClassName;

    @Override
    public String getAgentClassName() {
        return SqlDbAgent.class.getName();
    }

    public String getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
    }

    public String getConnectionUrl() {
        return connectionUrl;
    }

    public void setConnectionUrl(String connectionUrl) {
        this.connectionUrl = connectionUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(Integer connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }
}
