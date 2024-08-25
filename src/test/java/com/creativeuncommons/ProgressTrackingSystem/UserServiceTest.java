package com.creativeuncommons.ProgressTrackingSystem;

import com.creativeuncommons.ProgressTrackingSystem.model.Role;
import com.creativeuncommons.ProgressTrackingSystem.model.User;
import com.creativeuncommons.ProgressTrackingSystem.repository.UserRepository;
import com.creativeuncommons.ProgressTrackingSystem.security.CustomPasswordEncoder;
import com.creativeuncommons.ProgressTrackingSystem.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    UserRepository repository;

    @Mock
    CustomPasswordEncoder passwordEncoder;

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

        List<Role> allRoles = List.of(new Role(UUID.randomUUID(), "ADMIN"),
                new Role(UUID.randomUUID(), "USER"));


        List<Role> userRoles = List.of(new Role(null, "USER"));
        testUser.setRoles(userRoles);

        when(repository.getRoles()).thenReturn(allRoles);
        when(repository.findByName(testUser.getUserName())).thenReturn(Optional.empty());
        when(repository.save(testUser)).thenReturn(testUser);
        when(passwordEncoder.encode(testUser.getPassword())).thenReturn(testUser.getPassword());
        when(passwordEncoder.getSalt()).thenReturn(testUser.getSalt());

        User registeredUser = userService.register(testUser);
        assertNotNull(registeredUser);
        assertNotNull(registeredUser.getRoles().stream().findFirst().orElseGet(() -> null));
        assertNotNull(registeredUser.getRoles().stream().findFirst().get().getUuid());
        assertEquals(testUser.getRoles().stream().findFirst().get().getRoleName(),registeredUser.getRoles().stream().findFirst().get().getRoleName());
    }

}
