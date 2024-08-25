package com.creativeuncommons.ProgressTrackingSystem.config;

import com.creativeuncommons.ProgressTrackingSystem.datasource.QueryExecutionManager;
import com.creativeuncommons.ProgressTrackingSystem.query.LinkUserRolesQueryProvider;
import com.creativeuncommons.ProgressTrackingSystem.query.UserQueries;
import com.creativeuncommons.ProgressTrackingSystem.security.CustomPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.management.Query;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

@Configuration
public class Beans {


    @Bean
    public Logger logger() {
        return Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    }


    @Bean
    @Qualifier("customPasswordEncoder")
    public CustomPasswordEncoder passwordEncoder() {
        return new CustomPasswordEncoder();
    }

    @Bean
    @Qualifier("userQueryProvider")
    public UserQueries userQueryProvider() {
        return new UserQueries();
    }
/*
    @Bean
    @Qualifier("postgresConnection")
    public Connection postgresConnection(@Value("${spring.datasource.url}") String url,
                                         @Value("${spring.datasource.username}") String username,
                                         @Value("${spring.datasource.password}") String password) throws SQLException {
        return DriverManager.getConnection(url,username,password);
    }
 */

    @Bean
    DriverManagerDataSource dataSource(@Value("${spring.datasource.url}") String url,
                                       @Value("${spring.datasource.username}") String username,
                                       @Value("${spring.datasource.password}") String password,
                                       @Value("${spring.datasource.driver-class-name}") String driverName){
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setUrl(url);
        driverManagerDataSource.setUsername(username);
        driverManagerDataSource.setPassword(password);
        driverManagerDataSource.setDriverClassName(driverName);
        Properties properties = new Properties();
        properties.put("autoCommit",false);
        driverManagerDataSource.setConnectionProperties(properties);
        return driverManagerDataSource;
    }

    @Bean
    @Qualifier("postgresJdbcNamedParamTemplate")
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(@Autowired DriverManagerDataSource driverManagerDataSource) throws SQLException {
        return new NamedParameterJdbcTemplate(driverManagerDataSource);
    }

    @Bean
    public QueryExecutionManager queryExecutionManager(@Autowired NamedParameterJdbcTemplate jdbcTemplate){
        return new QueryExecutionManager(jdbcTemplate);
    }


    @Bean
    public PlatformTransactionManager transactionManager(@Autowired DriverManagerDataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public LinkUserRolesQueryProvider linkUserRolesQueryProvider(){
        return new LinkUserRolesQueryProvider();
    }

}
