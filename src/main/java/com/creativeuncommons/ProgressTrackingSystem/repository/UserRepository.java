package com.creativeuncommons.ProgressTrackingSystem.repository;

import com.creativeuncommons.ProgressTrackingSystem.datasource.QueryExecutionManager;
import com.creativeuncommons.ProgressTrackingSystem.model.Role;
import com.creativeuncommons.ProgressTrackingSystem.model.User;
import com.creativeuncommons.ProgressTrackingSystem.query.*;
import com.creativeuncommons.ProgressTrackingSystem.querybuilder.InsertionBuilder;
import com.creativeuncommons.ProgressTrackingSystem.querybuilder.QueryBuilder;
import com.creativeuncommons.ProgressTrackingSystem.querybuilder.SQLBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import javax.management.Query;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepository {


    UserQueries queryProvider;
    QueryExecutionManager queryExecutionManager;
    LinkUserRolesQueryProvider linkUserRolesQueryProvider;
    RoleQueryProvider roleQueryProvider;

    public UserRepository(@Autowired UserQueries queryProvider,
                          @Autowired QueryExecutionManager queryExecutionManager,
                          @Autowired LinkUserRolesQueryProvider linkUserRolesQueryProvider,
                          @Autowired RoleQueryProvider roleQueryProvider) {
        this.queryExecutionManager = queryExecutionManager;
        this.queryProvider = queryProvider;
        this.linkUserRolesQueryProvider = linkUserRolesQueryProvider;
        this.roleQueryProvider = roleQueryProvider;
    }

    public User save(User user) throws SQLException {
        InsertionBuilder<USER_SCHEMA_DATA> insertionBuilder = queryProvider.getSQL_INSERT();

        insertionBuilder.setParamValues(Map.of(USER_SCHEMA_DATA.ID_COLUMN, user.getId(),
                USER_SCHEMA_DATA.USER_NAME_COLUMN, user.getUserName(),
                USER_SCHEMA_DATA.USER_EMAIL_COLUMN, user.getEmail(),
                USER_SCHEMA_DATA.SALT_COLUMN, user.getSalt(),
                USER_SCHEMA_DATA.USER_PASSWORD_COLUMN, user.getPassword()));

        queryExecutionManager.executeDML(insertionBuilder);

        for (Role role : user.getRoles()) {
            QueryBuilder queryBuilder = linkUserRolesQueryProvider.setUserRolesQuery(user.getId(), role.getUuid());
            queryExecutionManager.executeDML(queryBuilder);
        }

        return findById(user.getId()).orElseGet(() -> null);
    }


    public Optional<User> findById(UUID id) throws SQLException {

        WhereFiltersBuilder<String> whereFiltersBuilder = new WhereFiltersBuilder<String>(
                USER_SCHEMA_DATA.ID_COLUMN.toString(), OPERATOR.EQUALS, Pair.of("id", id)
        );

        return loadUser(whereFiltersBuilder);
    }


    public Optional<User> findByNameAndPassword(String name, String password) throws SQLException {

        WhereFiltersBuilder<String> whereFiltersBuilder = new WhereFiltersBuilder<String>(
                USER_SCHEMA_DATA.USER_NAME_COLUMN.toString(), OPERATOR.EQUALS, Pair.of("user_name", name)
        ).and(USER_SCHEMA_DATA.USER_PASSWORD_COLUMN.toString(), OPERATOR.EQUALS, Pair.of("password", password));

        return loadUser(whereFiltersBuilder);
    }

    public Optional<User> findByName(String name) throws SQLException {


        WhereFiltersBuilder<String> whereFiltersBuilder = new WhereFiltersBuilder<String>(
                USER_SCHEMA_DATA.USER_NAME_COLUMN.toString(), OPERATOR.EQUALS, Pair.of("user_name", name)
        );

        return loadUser(whereFiltersBuilder);
    }

    public Optional<User> findByEmailAndPassword(String email, String password) throws SQLException {

        WhereFiltersBuilder<String> whereFiltersBuilder = new WhereFiltersBuilder<String>(
                USER_SCHEMA_DATA.USER_EMAIL_COLUMN.toString(), OPERATOR.EQUALS, Pair.of("email", email)
        ).and(USER_SCHEMA_DATA.USER_PASSWORD_COLUMN.toString(), OPERATOR.EQUALS, Pair.of("password", password));

        return loadUser(whereFiltersBuilder);
    }

    public List<Role> getRoles() throws SQLException {
        return queryExecutionManager
                .executeSQL(roleQueryProvider.getSQL_LOAD())
                .stream()
                .map(Role::buildFromMap)
                .toList();
    }


    private void setUserRoles(User user) throws SQLException {
        SQLBuilder rolesQuery = queryProvider.getUserRolesQuery(user.getId());
        user.setRoles(
                queryExecutionManager
                        .executeSQL(rolesQuery)
                        .stream()
                        .map(Role::buildFromMap)
                        .toList()
        );
    }

    private Optional<User> loadUser(WhereFiltersBuilder<String> whereFiltersBuilder) throws SQLException {
        SQLBuilder sqlBuilder = queryProvider.getSQL_LOAD();

        sqlBuilder.where(whereFiltersBuilder);

        List<Map<String, Object>> rows = queryExecutionManager.executeSQL(sqlBuilder);

        Optional<User> user = rows.stream().map(User::buildFromMap).findFirst();

        if (user.isPresent())
            setUserRoles(user.get());
        return user;
    }


}
