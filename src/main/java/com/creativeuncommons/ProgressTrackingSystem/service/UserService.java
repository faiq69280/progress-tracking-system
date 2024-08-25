package com.creativeuncommons.ProgressTrackingSystem.service;

import com.creativeuncommons.ProgressTrackingSystem.exceptions.SaveFailureException;
import com.creativeuncommons.ProgressTrackingSystem.model.Role;
import com.creativeuncommons.ProgressTrackingSystem.repository.UserRepository;
import com.creativeuncommons.ProgressTrackingSystem.security.CustomPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.creativeuncommons.ProgressTrackingSystem.model.User;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    @Qualifier("customPasswordEncoder")
    CustomPasswordEncoder passwordEncoder;



    public User register(User user) throws SaveFailureException, SQLException {

        if(userRepository.findByName(user.getUserName()).isPresent())
            throw new SaveFailureException("Couldn't register user: already exists",
                    (User)user);

        List<Role> roles = userRepository.getRoles();
        List<String> desiredRoles = user.getRoles().stream().map(role->role.getRoleName().toLowerCase()).toList();

        user.setRoles(roles.stream().filter(role->desiredRoles.contains(role.getRoleName().toLowerCase())).toList());

        passwordEncoder.generateSalt();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setSalt(passwordEncoder.getSalt());
        user.setId(UUID.randomUUID());

        return userRepository.save(user);
    }

    public User login(User user) throws AuthenticationCredentialsNotFoundException,SQLException
    {
        User fetchedUser = userRepository.findByName(user.getUserName()).orElseGet(()->{
            throw new UsernameNotFoundException("Couldn't find username in DB");
        });

        passwordEncoder.setSalt(fetchedUser.getSalt());
        return userRepository.findByNameAndPassword(
                user.getUserName(),
                passwordEncoder.encode(user.getPassword())
              ).orElseThrow(
                       () -> new AuthenticationCredentialsNotFoundException("Couldn't find credentials")
                     );

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        try {
            User user = userRepository.findByName(username).orElseGet(() -> {
                throw new UsernameNotFoundException("Couldn't find username in DB");
            });

            return org.springframework.security.core.userdetails
                    .User.builder()
                    .username(user.getUserName())
                    .password(user.getPassword())
                    .authorities(user.getRoles().stream().map(Role::getRoleName).toArray(String[]::new))
                    .build();
        }
        catch(SQLException sqlException)
        {
            throw new UsernameNotFoundException(sqlException.getMessage());
        }
    }



}
