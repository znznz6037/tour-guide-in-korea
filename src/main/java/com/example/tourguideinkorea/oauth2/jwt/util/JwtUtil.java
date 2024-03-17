package com.example.tourguideinkorea.oauth2.jwt.util;

import com.example.tourguideinkorea.oauth2.service.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * @author Kim Keumtae
 */
@Service
public class JwtUtil {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private static final String AUTHORITIES_KEY = "authorities";

    //@Value("${jwt.secretKey}")
    private final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private String base64Key = Encoders.BASE64.encode(key.getEncoded());

    private long tokenValidityInMilliseconds;
    private long tokenValidityInMillisecondsForRememberMe;


    public JwtUtil() {
        this.tokenValidityInMilliseconds = 3600000;
        this.tokenValidityInMillisecondsForRememberMe = 3600000;
    }

    public String createToken(Authentication authentication) {
        return createToken(authentication, false);
    }

    public String createToken(Authentication authentication, Boolean rememberMe) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity;
        if (rememberMe) {
            validity = new Date(now + this.tokenValidityInMillisecondsForRememberMe);
        } else {
            validity = new Date(now + this.tokenValidityInMilliseconds);
        }
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        return Jwts.builder()
                .setId(customUserDetails.getId())
                .setSubject(customUserDetails.getEmail())
                .setIssuedAt(new Date())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key)
                .setExpiration(validity)
                .compact();
    }

    public String createAdminToken() {
        return Jwts.builder()
                .setSubject("admin")
                .claim(AUTHORITIES_KEY, "ROLE_ADMIN")
                .signWith(key)
                .setExpiration(new Date((new Date()).getTime() + 1000 * 3600 * 24 * 365))
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(claims.getId(), claims.getSubject(), authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public String getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(authToken);
            return true;
//        } catch (SignatureException e) {
//            log.info("Invalid JWT signature.");
//            log.trace("Invalid JWT signature trace: {}", e);
//        } catch (MalformedJwtException e) {
//            log.info("Invalid JWT token.");
//            log.trace("Invalid JWT token trace: {}", e);
//        } catch (ExpiredJwtException e) {
//            log.info("Expired JWT token.");
//            log.trace("Expired JWT token trace: {}", e);
//        } catch (UnsupportedJwtException e) {
//            log.info("Unsupported JWT token.");
//            log.trace("Unsupported JWT token trace: {}", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
            log.trace("JWT token compact of handler are invalid trace: {}", e);
        }
        return false;
    }
}