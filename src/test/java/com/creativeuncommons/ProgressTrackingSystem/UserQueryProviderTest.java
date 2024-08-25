package com.creativeuncommons.ProgressTrackingSystem;

import com.creativeuncommons.ProgressTrackingSystem.query.*;
import com.creativeuncommons.ProgressTrackingSystem.querybuilder.QueryBuilder;
import com.creativeuncommons.ProgressTrackingSystem.querybuilder.SQLBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class UserQueryProviderTest {


    UserQueries userQueryProvider = new UserQueries();


    @Test
    public void testQueries() {
        QueryBuilder SQL = userQueryProvider.getSQL_LOAD();


        assertNotNull(SQL.build());

        Map<USER_SCHEMA_DATA, Object> values = Map.of(USER_SCHEMA_DATA.USER_PASSWORD_COLUMN, "pass",
                USER_SCHEMA_DATA.USER_EMAIL_COLUMN, "email",
                USER_SCHEMA_DATA.USER_NAME_COLUMN, "name", USER_SCHEMA_DATA.ID_COLUMN, UUID.randomUUID(),
                USER_SCHEMA_DATA.SALT_COLUMN, "salt");

        String SQL_INSERT = userQueryProvider.getSQL_INSERT().build();
        assertNotNull(SQL_INSERT);


        SQLBuilder sqlBuilder = new SQLBuilder();

        WhereFiltersBuilder<String> whereFiltersBuilder = new WhereFiltersBuilder<String>(
                USER_SCHEMA_DATA.USER_EMAIL_COLUMN.toString(),
                OPERATOR.EQUALS,
                Pair.of(USER_SCHEMA_DATA.USER_NAME_COLUMN.toString() ,
                        "testmail@yahoo.com"));

        sqlBuilder.select(List.of(USER_SCHEMA_DATA.USER_NAME_COLUMN.toString(),
                USER_SCHEMA_DATA.USER_EMAIL_COLUMN.toString()),
                false).from("users")
                .where(whereFiltersBuilder);

        sqlBuilder.build();
    }


}
