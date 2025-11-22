package com.ms_maching.ms_maching.auth.config;

import com.ms_maching.ms_maching.auth.repository.UserRepository;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.security.Principal;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class JwtUtil {
    private static final String SECRET_KEY = "66f7a641bfd54843861382807f5b5c1bd065100dbaaa9f206afee62b39b1ee43";
    private static final long EXPIRATION_TIME_MS = 86_400_000L;  // 24h en ms

    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    @Autowired(required = false)
    private UserRepository userRepository;

    public String generateToken(Authentication auth, Long empresaId) {
        String username = auth.getName();
        List<String> roles = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        JwtBuilder builder = Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
                .signWith(key, SignatureAlgorithm.HS256);

        if (empresaId != null) {
            builder.claim("empresaId", empresaId);
        }

        return builder.compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        return (List<String>) Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("roles");
    }

    public Long extractEmpresaId(String token) {
        Object empresaId = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("empresaId");
        if (empresaId == null) return null;
        return Long.valueOf(empresaId.toString());
    }

    // ============ MÉTODOS NUEVOS PARA WEBSOCKET ==============

    /**
     * Extrae el userId del Principal usando el username
     */
    public Long getUserIdFromPrincipal(Principal principal, UserRepository userRepository) {
        try {
            if (principal != null) {
                String username = principal.getName();
                return userRepository.findByUsername(username)
                        .map(user -> user.getId())
                        .orElse(null);
            }
            return null;
        } catch (Exception e) {
            log.error("Error extrayendo userId del principal: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Extrae el userId del token JWT usando el username
     */
    public Long getUserIdFromToken(String token, UserRepository userRepository) {
        try {
            String username = extractUsername(token);
            if (username != null && userRepository != null) {
                return userRepository.findByUsername(username)
                        .map(user -> user.getId())
                        .orElse(null);
            }
            return null;
        } catch (Exception e) {
            log.error("Error extrayendo userId del token: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Extrae el token del header Authorization
     */
    public String extractTokenFromHeader(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    /**
     * Extrae el username del token JWT
     */
    public String extractUsernameFromToken(String token) {
        try {
            return extractUsername(token);
        } catch (Exception e) {
            log.error("Error extrayendo username del token: {}", e.getMessage());
            return null;
        }
    }
}