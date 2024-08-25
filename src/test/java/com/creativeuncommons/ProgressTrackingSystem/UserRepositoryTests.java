package com.creativeuncommons.ProgressTrackingSystem;

import com.creativeuncommons.ProgressTrackingSystem.datasource.QueryExecutionManager;
import com.creativeuncommons.ProgressTrackingSystem.model.Role;
import com.creativeuncommons.ProgressTrackingSystem.model.User;
import com.creativeuncommons.ProgressTrackingSystem.query.*;
import com.creativeuncommons.ProgressTrackingSystem.repository.UserRepository;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.sql.*;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;


@SpringBootTest
@RunWith(SpringRunner.class)
public class UserRepositoryTests {

    public static final UUID TEST_UUID = UUID.randomUUID();
    public static final String TEST_NAME = "test_name",
    TEST_EMAIL = "test-mail",
    TEST_PASSWORD = "test-password",
    TEST_SALT = "test-salt";

    @Autowired
    PlatformTransactionManager platformTransactionManager;

    TransactionStatus transactionStatus;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserQueries queryProvider;

    @Autowired QueryExecutionManager queryExecutionManager;

    @Autowired LinkUserRolesQueryProvider linkUserRolesQueryProvider;

    @Autowired RoleQueryProvider roleQueryProvider;


    @Before
    public void setup(){
        userRepository = new UserRepository(queryProvider,queryExecutionManager,linkUserRolesQueryProvider,roleQueryProvider);
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
        transactionStatus = platformTransactionManager.getTransaction(definition);
    }



    @After
    public void tearDown() throws SQLException{
          platformTransactionManager.rollback(transactionStatus);
    }


    @Test
    public void repositoryTests() throws SQLException {
        User testUser = new User(TEST_UUID,TEST_NAME,TEST_PASSWORD,TEST_SALT,TEST_EMAIL);
        testUser.setRoles(List.of(new Role(UUID.fromString("fc439672-09ca-47f1-bc19-d37036659b67"),"ADMIN")));
        User insertedUser = userRepository.save(testUser);
        assertNotNull(insertedUser);
        assertNotNull(insertedUser.getRoles());


        User foundUser = userRepository.findById(testUser.getId()).orElseGet(() -> null);
        assertNotNull(foundUser);
        assertEquals(testUser.getId(), foundUser.getId());

        User foundByName = userRepository.findByName(testUser.getUserName()).orElseGet(() -> null);
        assertNotNull(foundByName);
        assertEquals(testUser.getUserName(),foundByName.getUserName());

        User foundByEmailAndPassword = userRepository
                .findByEmailAndPassword(testUser.getEmail(), testUser.getPassword())
                .orElseGet(() -> null);

        assertNotNull(foundByEmailAndPassword);
        assertEquals(testUser.getEmail(),foundByEmailAndPassword.getEmail());
        assertEquals(testUser.getPassword(),foundByEmailAndPassword.getPassword());

        User foundByNameAndPassword = userRepository
                .findByNameAndPassword(testUser.getUserName(), testUser.getPassword())
                .orElseGet(() -> null);

        assertNotNull(foundByNameAndPassword);
        assertEquals(testUser.getUserName(),foundByNameAndPassword.getUserName());
        assertEquals(testUser.getPassword(),foundByNameAndPassword.getPassword());

        assertNotNull(userRepository.getRoles());

    }


}
