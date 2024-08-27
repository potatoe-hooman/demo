package com.assignment.demo.Utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class DatabaseUtil {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Executes a SELECT query and returns the result as a List of Maps.
     * Each Map represents a row with column names as keys.
     * 
     * @param query the SQL query to execute
     * @return a List of Maps where each Map is a row
     */
    public List<Map<String, Object>> executeSelectQuery(String query) {
        return jdbcTemplate.queryForList(query);
    }

    /**
     * Executes an INSERT, UPDATE, or DELETE query.
     * 
     * @param query the SQL query to execute
     * @return the number of rows affected
     */
    public int executeUpdateQuery(String query) {
        return jdbcTemplate.update(query);
    }

    /**
     * Executes a query and returns a single object, typically used for single-row or aggregate queries.
     * 
     * @param query the SQL query to execute
     * @param rowMapper a RowMapper to convert the result set into the desired object
     * @param <T> the type of the object to return
     * @return the mapped object, or null if no rows found
     */
    public <T> T executeQueryForObject(String query, RowMapper<T> rowMapper) {
        return jdbcTemplate.queryForObject(query, rowMapper);
    }

    /**
     * Executes a batch of SQL queries.
     * 
     * @param queries an array of SQL queries to execute
     * @return an array of the number of rows affected by each query
     */
    public int[] executeBatchUpdate(String[] queries) {
        return jdbcTemplate.batchUpdate(queries);
    }
}
