package com.creativeuncommons.ProgressTrackingSystem.query;

import org.springframework.data.util.Pair;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.util.*;

public class WhereFiltersBuilder<T> {
    StringBuilder filters;

    MapSqlParameterSource paramValues;

    public WhereFiltersBuilder(T field, OPERATOR op, Object value) {
        filters = new StringBuilder();
        paramValues = new MapSqlParameterSource();
        addToMap(field, value, "where", op);
    }


    public WhereFiltersBuilder<T> and(T field, OPERATOR op, Object value) {
        addToMap(field, value, "and", op);
        return this;
    }

    public WhereFiltersBuilder<T> or(T field, OPERATOR op, Object value) {
        addToMap(field, value, "or", op);
        return this;
    }

    public String build() {
        return filters.toString();
    }

    private void addToMap(T field, Object value, String operation, OPERATOR op) {
        if (value instanceof Pair) {
            Pair<String, Object> pair = (Pair) value;
            paramValues.addValue(pair.getFirst(), pair.getSecond());
            filters.append(" %s %s %s :%s".formatted(operation,field.toString(),op.toString(),pair.getFirst()));
        } else
            filters.append(" %s %s %s %s".formatted(operation, field.toString(), op.toString(), value.toString()));
    }


    public MapSqlParameterSource getQueryParams() {
        return paramValues;
    }


}
