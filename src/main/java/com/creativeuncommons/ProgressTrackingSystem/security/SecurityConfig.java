package com.creativeuncommons.ProgressTrackingSystem.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    AuthEntryPointJwt authEntryPointJwt(){

        return new AuthEntryPointJwt();

    }


    @Bean
    AuthTokenFilter authTokenFilter(){
        return new AuthTokenFilter();
    }



    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity)
    throws Exception {

        return httpSecurity
                .authorizeHttpRequests(requests ->
                        requests.requestMatchers("/user/register").permitAll()
                                .requestMatchers("/user/login").permitAll()
                                .requestMatchers("/error/**").permitAll()
                                .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .exceptionHandling(exceptionHandler -> exceptionHandler.authenticationEntryPoint(authEntryPointJwt()))
               // .httpBasic(Customizer.withDefaults())
                .addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();

    }

}
