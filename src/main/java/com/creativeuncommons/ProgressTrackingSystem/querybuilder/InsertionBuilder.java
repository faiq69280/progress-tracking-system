package com.creativeuncommons.ProgressTrackingSystem.querybuilder;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.util.*;

public class InsertionBuilder<T extends Enum<T>> implements QueryBuilder {


    Class<T> enumType;
    String TABLE;

    MapSqlParameterSource queryParams;

    public InsertionBuilder(Class<T> enumType, String TABLE) {
        this.TABLE = TABLE;
        this.enumType = enumType;
        queryParams = new MapSqlParameterSource();
    }

    public String build() {
        return """
                INSERT INTO %s(%s) VALUES(%s)
                """.formatted(TABLE,
                String.join(",", Arrays.stream(this.enumType.getEnumConstants()).map(Object::toString).toList()),
                String.join(",", Arrays.stream(this.enumType.getEnumConstants()).map(val -> ":" + val.toString()).toList())
        );
    }

    public InsertionBuilder<T> setParamValues(Map<T, Object> paramValues) {
        paramValues.forEach((k, v) -> {
            queryParams.addValue(k.toString(), v);
        });
        return this;
    }

    public MapSqlParameterSource getQueryParams() {
        return queryParams;
    }


}
