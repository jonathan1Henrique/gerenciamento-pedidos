package com.gerenciador.auth.jwt;


import com.gerenciador.auth.entity.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtService {

    private final String secretKey = "Zk1tB5U7J2hhd6T1UOtqVhXzYYbRv0ZyZJ7lK9m7v1Y";

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey));
    }

    public String generateToken(Usuario usuario) {
        return Jwts.builder()
                .setSubject(usuario.getUsername())
                .claim("role", usuario.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)))
                .signWith(Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey)), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername());
    }
}
