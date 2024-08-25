package com.creativeuncommons.ProgressTrackingSystem.security;

import com.creativeuncommons.ProgressTrackingSystem.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    JwtUtils jwtUtils;


    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse
                                    response,
                                    FilterChain filterChain) throws ServletException, IOException{
              try {
                  String jwt = jwtUtils.getJwtFromHeader(httpServletRequest);
                  if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                      String userName = jwtUtils.getUserNameFromJwtToken(jwt);
                      UserDetails userDetails = userService.loadUserByUsername(userName);
                      UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                              userName, null, userDetails.getAuthorities()
                      );
                      token.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                      SecurityContextHolder.getContext().setAuthentication(token);
                  }

              }
              catch(Exception e){
                  throw e;
              }

              filterChain.doFilter(httpServletRequest, response);
    }

    @Override
    public boolean shouldNotFilter(HttpServletRequest request){
        return request.getServletPath().startsWith("/user");
    }


}
