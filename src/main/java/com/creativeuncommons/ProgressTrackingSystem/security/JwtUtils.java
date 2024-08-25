package com.creativeuncommons.ProgressTrackingSystem.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;


@Component
public class JwtUtils {

    @Autowired
    Logger logger;

    @Value("${spring.app.jwt.secret}")
    String jwtSecret;

    @Value("${spring.app.jwtExpirationMs}")
    Long jwtExpriationMs;


    public String getJwtFromHeader(HttpServletRequest request){

        String bearerToken = request.getHeader("Authorization");

        if(bearerToken != null && bearerToken.startsWith("Bearer")){
            bearerToken = bearerToken.replaceAll("Bearer\\s*","");
            if(bearerToken.isEmpty())
                 throw new IllegalArgumentException("Empty bearer token");
        }
        else
            throw new IllegalArgumentException("No Bearer Token provided");

        return bearerToken;
    }

    public String getJwtFromUserName(UserDetails userDetails){
        String userName = userDetails.getUsername();
        return Jwts.builder()
                .subject(userName)
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime()+jwtExpriationMs))
                .signWith(key())
                .compact();
    }
    public String getUserNameFromJwtToken(String token){
        return Jwts.parser().verifyWith((SecretKey) key())
                .build().parseSignedClaims(token)
                .getPayload()
                .getSubject();

    }
    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
    public boolean validateJwtToken(String authToken){
        try{
            Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(authToken);
            return true;
        }
        catch(JwtException ex){
            logger.log(Level.SEVERE,ex.getMessage());
        }
        catch(IllegalArgumentException ex){
            logger.log(Level.SEVERE,ex.getMessage());
        }

        return false;
    }

}
