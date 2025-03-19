package com.example.BankSystem.service;

import io.jsonwebtoken.Claims;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "IZQTSSqsC5fWaaBO7MJvRdt41IZQTSSqsC5fWaaBO7MJvRdt41";

    public String extractUserName(String authToken) {
        return extractClaim(authToken, Claims::getSubject);
    }

    public <T> T extractClaim(String authToken, Function<Claims, T> claimsResolver) {
        final Claims claims = extractClaims(authToken);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(String.valueOf(userDetails))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSigninKey(), Jwts.SIG.HS256)
                .compact();
    }

    public boolean isTokenValid(String authToken, UserDetails userDetails) {
        final String username = extractUserName(authToken);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(authToken);
    }

    private boolean isTokenExpired(String authToken) {
        return extractExpiration(authToken).before(new Date());
    }

    private Date extractExpiration(String authToken) {
        return extractClaim(authToken, Claims::getExpiration);
    }

    private Claims extractClaims(String authToken) {
        return Jwts
                .parser()
                .verifyWith(getSigninKey())
                .build()
                .parseSignedClaims(authToken)
                .getPayload();
    }

    private SecretKey getSigninKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
