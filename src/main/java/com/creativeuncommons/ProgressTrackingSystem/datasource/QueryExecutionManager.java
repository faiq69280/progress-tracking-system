package com.creativeuncommons.ProgressTrackingSystem.datasource;

import com.creativeuncommons.ProgressTrackingSystem.querybuilder.QueryBuilder;
import com.creativeuncommons.ProgressTrackingSystem.querybuilder.SQLBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryExecutionManager {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public QueryExecutionManager(NamedParameterJdbcTemplate namedParameterJdbcTemplate){
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }


    public Integer executeDML(QueryBuilder queryBuilder) throws SQLException {
        MapSqlParameterSource sqlParameterSource = queryBuilder.getQueryParams();
        String query = queryBuilder.build();
        return namedParameterJdbcTemplate.update(query, sqlParameterSource);
    }


    public List<Map<String, Object>> executeSQL(SQLBuilder queryBuilder) throws SQLException {
        MapSqlParameterSource mapSqlParameterSource = queryBuilder.getQueryParams();
        return namedParameterJdbcTemplate.query(queryBuilder.build(), mapSqlParameterSource, new GenericRowMapper());
    }

    private static class GenericRowMapper implements RowMapper<Map<String, Object>> {

        @Override
        public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
            Map<String, Object> objectMap = new HashMap<>();

            ResultSetMetaData rsmd = rs.getMetaData();

            for (int columnIdx = 1; columnIdx <= rsmd.getColumnCount(); columnIdx++) {
                String columnName = rsmd.getColumnName(columnIdx);
                objectMap.put(columnName, rs.getObject(columnName));
            }

            return objectMap;
        }
    }

}
