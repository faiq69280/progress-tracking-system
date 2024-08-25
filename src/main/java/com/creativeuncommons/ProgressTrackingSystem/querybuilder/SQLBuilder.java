package com.creativeuncommons.ProgressTrackingSystem.querybuilder;

import com.creativeuncommons.ProgressTrackingSystem.query.WhereFiltersBuilder;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.util.*;

public class SQLBuilder implements QueryBuilder {

    StringBuilder sb;
    MapSqlParameterSource queryParamsMap;

    public SQLBuilder() {
        sb = new StringBuilder();
        queryParamsMap = new MapSqlParameterSource();
    }

    public SQLBuilder select(List<String> selectionColumns, Boolean distinct) {
        sb.append(" Select %s %s".formatted(distinct ? "DISTINCT" : "", String.join(",",
                selectionColumns)));
        return this;
    }

    public SQLBuilder withCTE(SQLBuilder sqlBuilder, String alias) {
        sb.append(" with %s as (%s)".formatted(alias, sqlBuilder.toString()));
        return this;
    }

    public SQLBuilder from(String TABLE) {
        sb.append(" from %s".formatted(TABLE));
        return this;
    }


    public SQLBuilder join(String TABLE1, String TABLE2, String key1, String key2) {
        sb.append(" join %s on %s.%s = %s.%s".formatted(TABLE2, TABLE1, key1, TABLE2, key2));
        return this;
    }

    public SQLBuilder leftJoin(String TABLE1, String TABLE2, String key1, String key2) {
        sb.append(" left join %s on %s.%s = %s.%s".formatted(TABLE1, TABLE1, key1, TABLE2, key2));
        return this;
    }

    public SQLBuilder rightJoin(String TABLE1, String TABLE2, String key1, String key2) {
        sb.append(" right join %s on %s.%s = %s.%s".formatted(TABLE1, TABLE1, key1, TABLE2, key2));
        return this;
    }


    public SQLBuilder fullOuterJoin(String TABLE1, String TABLE2, String key1, String key2) {
        sb.append(" full outer join %s on %s.%s = %s.%s".formatted(TABLE1, TABLE1, key1, TABLE2, key2));
        return this;
    }

    public SQLBuilder where(WhereFiltersBuilder<String> whereFiltersBuilder) {
        sb.append(" %s".formatted(whereFiltersBuilder.build()));

        whereFiltersBuilder.getQueryParams().getValues().forEach((key, value) -> {
            if (!queryParamsMap.hasValue(key))
                queryParamsMap.addValue(key, value);
        });

        return this;
    }

    public MapSqlParameterSource getQueryParams() {
        return queryParamsMap;
    }

    public String build() {
        return sb.toString();
    }

}
