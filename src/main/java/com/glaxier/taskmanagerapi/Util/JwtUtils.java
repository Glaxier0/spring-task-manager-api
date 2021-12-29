package com.glaxier.taskmanagerapi.Util;

import com.glaxier.taskmanagerapi.model.Users;
import com.glaxier.taskmanagerapi.service.UserDetailsImpl;
import com.glaxier.taskmanagerapi.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import java.util.Date;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;

@Component
public class JwtUtils {

    @Value("${secret_key}")
    private String SECRET_KEY;

    @Value("${jwt_expiration}")
    private int JWT_EXPIRATION;

    UserService userService;

    public JwtUtils(UserService userService) {
        this.userService = userService;
    }

    public String generateJwtToken(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject((userPrincipal.getEmail()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + JWT_EXPIRATION))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
    }


    public boolean validateJwtToken(String authToken) {
        try {
            Optional<Users> userData = userService.findByEmail(Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(authToken)
                    .getBody().getSubject());
            if (userData.isPresent()) {
                Users user = userData.get();
                if (user.getTokens().stream().anyMatch(authToken::equals)) {
                    return true;
                }
            }
        } catch (SignatureException e) {
            System.out.println("Invalid JWT signature: {}" + e.getMessage());
        } catch (MalformedJwtException e) {
            System.out.println("Invalid JWT token: {}" + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.out.println("JWT token is expired: {}" + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("JWT token is unsupported: {}" + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("JWT claims string is empty: {}" + e.getMessage());
        }

        return false;
    }

    public String getToken(HttpHeaders httpHeaders) {
        String token = httpHeaders.get("authorization").get(0).substring(7);
        return token;
    }
}