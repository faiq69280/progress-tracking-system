package com.creativeuncommons.ProgressTrackingSystem.query;

import com.creativeuncommons.ProgressTrackingSystem.querybuilder.InsertionBuilder;
import com.creativeuncommons.ProgressTrackingSystem.querybuilder.QueryBuilder;
import com.creativeuncommons.ProgressTrackingSystem.querybuilder.SQLBuilder;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractQueryProvider<T extends Enum<T>> {

    private Class<T> clazz;
    private String TABLE;
    public AbstractQueryProvider(Class<T> clazz,String TABLE) {
        this.clazz = clazz;
          this.TABLE = TABLE;
    }

    public SQLBuilder getSQL_LOAD(){
        return new SQLBuilder().select(
                Arrays.stream(clazz.getEnumConstants()).map(Object::toString).toList(),
                false).from(TABLE);
    }

    public InsertionBuilder<T> getSQL_INSERT(){
        return new InsertionBuilder<>(clazz,TABLE);
    }

}
