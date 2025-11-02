package io.kiwi.agents.data.sql;

import io.kiwi.agents.common.Agent;
import io.kiwi.config.data.sql.SqlDbAgentConfig;
import io.kiwi.context.StepResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.*;

public class SqlDbAgent extends Agent {
    private static final Logger logger = LogManager.getLogger(SqlDbAgent.class);
    private final SqlDbAgentConfig config;
    private Connection connection;

    public SqlDbAgent(SqlDbAgentConfig config) {
        this.config = config;
        this.name = config.getName();
    }

    /**
     * Establishes a connection to the SQL database using the provided configuration.
     */
    public StepResult connect() {
        try{
            String driverClassName = config.getDriverClassName();
            if(driverClassName == null || driverClassName.isEmpty()) {
                return new StepResult(StepResult.Status.FAILED,
                        "JDBC driver class name is not provided and could not be inferred from database type: "
                                + config.getDatabaseType(), null);
            }

            Class.forName(driverClassName);
            Properties props = new Properties();
            props.setProperty("user", config.getUsername());
            props.setProperty("password", config.getPassword());

            DriverManager.setLoginTimeout(config.getConnectionTimeout() / 1000); // Convert milliseconds to seconds
            connection = DriverManager.getConnection(config.getConnectionUrl(), props);

            // set auto commit to false for transaction management
            connection.setAutoCommit(false);

            logger.info("Database connection established successfully for agent: {}", name);
            return new StepResult(StepResult.Status.PASSED,
                    "Database connection established successfully for agent: " + name, null);
        } catch (ClassNotFoundException e) {
            logger.error("JDBC Driver class not found: {}", e.getMessage());
            return new StepResult(StepResult.Status.FAILED,
                    "JDBC Driver class not found: " + e.getMessage(), null);
        }catch(Exception ex){
            logger.error("Error establishing database connection: {}", ex.getMessage());
            return new StepResult(StepResult.Status.FAILED,
                    "Error establishing database connection: " + ex.getMessage(), null);
        }
    }

    /**
     * Closes the database connection if it is open.
     */
    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                logger.info("Database connection closed successfully for agent: {}", name);
            }
        } catch (Exception ex) {
            logger.error("Error closing database connection: {}", ex.getMessage());
        }
    }

    /**
     * Execute SELECT query and return results
     * @param sqlQuery SQL SELECT query string
     * @return StepResult containing query results or error message
     */
    public StepResult executeQuery(String sqlQuery) {
        if (connection == null) {
            StepResult connectResult = connect();
            if (connectResult.getStatus() == StepResult.Status.FAILED) {
                return connectResult;
            }
        }

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery);
             ResultSet resultSet = statement.executeQuery()) {
            List<Map<String, Object>> results = new ArrayList<>();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                Map<String, Object> row = new LinkedHashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnLabel(i);
                    Object columnValue = resultSet.getObject(i);
                    row.put(columnName, columnValue);
                }
                results.add(row);
            }

            logger.info("Query executed successfully for agent: {}, returned {} rows", name, results.size());
            return new StepResult(StepResult.Status.PASSED,
                    "Query executed successfully for agent: " + name, results);
        } catch (SQLException ex) {
            logger.error("Error executing query: {}", ex.getMessage());
            return new StepResult(StepResult.Status.FAILED,
                    "Error executing query: " + ex.getMessage(), null);
        }
    }

    /**
     * Execute INSERT, UPDATE, DELETE query
     * @param sqlUpdate SQL INSERT, UPDATE, DELETE query string
     * @return StepResult containing number of affected rows or error message
     */
    public StepResult executeUpdate(String sqlUpdate) {
        if (connection == null) {
            StepResult connectResult = connect();
            if (connectResult.getStatus() == StepResult.Status.FAILED) {
                return connectResult;
            }
        }

        try (PreparedStatement statement = connection.prepareStatement(sqlUpdate)) {
            int affectedRows = statement.executeUpdate();
            connection.commit();
            logger.info("Update executed successfully for agent: {}, affected {} rows", name, affectedRows);
            return new StepResult(StepResult.Status.PASSED,
                    "Update executed successfully for agent: " + name + ", affected " + affectedRows + " rows", affectedRows);
        } catch (SQLException ex) {
            logger.error("Error executing update: {}", ex.getMessage());
            try {
                connection.rollback();
                logger.info("Transaction rolled back due to error");
            } catch (SQLException rollbackEx) {
                logger.error("Error during transaction rollback: {}", rollbackEx.getMessage());
            }
            return new StepResult(StepResult.Status.FAILED,
                    "Error executing update: " + ex.getMessage(), null);
        }
    }

    /**
     * Check if connection is active
     */
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed() && connection.isValid(5);
        } catch (SQLException ex) {
            logger.error("Error checking connection status: {}", ex.getMessage());
            return false;
        }
    }

    /**
     * Get the current database connection (for advanced usage)
     */
    protected Connection getConnection() {
        return connection;
    }

}
