package com.creativeuncommons.ProgressTrackingSystem.querybuilder;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.util.List;
import java.util.Map;

public interface QueryBuilder {

    public String build();

    public MapSqlParameterSource getQueryParams();

}
