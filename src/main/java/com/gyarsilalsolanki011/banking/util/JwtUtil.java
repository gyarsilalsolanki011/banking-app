package com.gyarsilalsolanki011.banking.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    @Value("${jwt.secret.key}")
    private String SECRET_KEY;

    @Value("${jwt.expiration.time}")
    private long EXPIRATION_TIME;

    // ✅ Method to generate signing key
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // ✅ Method to Generate Token
    public String generateToken(Authentication authentication){
        Map<String, Object> claims = new HashMap<>();

        // If the user has a role (Admin), add it to the token, otherwise assign "USER"
        if (authentication.getAuthorities().isEmpty()) {
            claims.put("role", "USER");
        } else {
            claims.put("role", authentication.getAuthorities().iterator().next().getAuthority());
        }
        return createToken(claims, authentication.getName());
    }

    // ✅ Method to Create Token
    public String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .subject(username)
                .claims(claims)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), Jwts.SIG.HS256)  // Updated method
                .compact();
    }

    // ✅ Method to Extract All Claims from Token
    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // ✅ Method to Extract Username from Token
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // ✅ Method to Validate Token
    public boolean validateToken(String token, org.springframework.security.core.userdetails.UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // ✅ Method to Check if Token is Expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // ✅ Method to Extract Expiration Date from Token
    private Date extractExpiration(String token) {
        return extractClaims(token).getExpiration();
    }
}
