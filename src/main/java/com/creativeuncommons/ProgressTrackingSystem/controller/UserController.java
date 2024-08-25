package com.creativeuncommons.ProgressTrackingSystem.controller;

import com.creativeuncommons.ProgressTrackingSystem.security.JwtUtils;
import com.creativeuncommons.ProgressTrackingSystem.security.AuthenticationResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.creativeuncommons.ProgressTrackingSystem.service.UserService;
import com.creativeuncommons.ProgressTrackingSystem.model.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    JwtUtils jwtUtils;


    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) throws SQLException {

         return new ResponseEntity<>(userService.register(user), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody User user) throws SQLException {

        User authentication = userService.login(user);
        UserDetails userDetails =  org.springframework.security.core.userdetails
                .User.builder()
                .username(authentication.getUserName())
                .password(authentication.getPassword())
                .authorities("ADMIN","USER")
                .build();

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(),
                userDetails.getAuthorities()
        ));
        String jwtToken = jwtUtils.getJwtFromUserName(userDetails);
        List<String> roles = userDetails.getAuthorities().stream().map(
                Objects::toString
        ).toList();

       AuthenticationResponse response =  new AuthenticationResponse(jwtToken,roles, userDetails.getUsername());

        return new ResponseEntity<>(
                response,HttpStatus.OK
        );

    }

}
