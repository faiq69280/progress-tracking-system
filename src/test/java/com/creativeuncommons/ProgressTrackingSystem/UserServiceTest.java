package com.creativeuncommons.ProgressTrackingSystem;

import com.creativeuncommons.ProgressTrackingSystem.model.User;
import com.creativeuncommons.ProgressTrackingSystem.repository.UserRepository;
import com.creativeuncommons.ProgressTrackingSystem.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.sql.SQLException;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @MockBean
    UserRepository repository;

    @Autowired
    @InjectMocks
    UserService userService;


    @Test
    public void testRegister() throws SQLException {
        User testUser = new User(UserRepositoryTests.TEST_UUID,
                UserRepositoryTests.TEST_NAME,
                UserRepositoryTests.TEST_PASSWORD,
                UserRepositoryTests.TEST_SALT,
                UserRepositoryTests.TEST_EMAIL);

        userService.register(testUser);

    }

}
