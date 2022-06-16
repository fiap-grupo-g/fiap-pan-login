package br.com.pan.login.authentication.services;

import br.com.pan.login.authentication.models.Session;
import br.com.pan.login.infrastructure.config.JwtConfig;
import br.com.pan.login.infrastructure.config.SecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

@Component
public record JwtService(JwtConfig jwtConfig, ObjectMapper objectMapper) {

    private static SecretKey SECRET_KEY;

    @PostConstruct
    private void postConstruct() {
        var secretKey = jwtConfig.getSecret().getBytes();
        SECRET_KEY = new SecretKeySpec(secretKey, 0, secretKey.length, SecurityConfig.HMAC_SHA_256);
    }

    public String generateToken(Session session) {
        var now = new Date();
        var expiryDate = new Date(now.getTime() + jwtConfig.getExpirationTimeout());

        return Jwts.builder()
                .setSubject(session.id())
                .claim("userName", session.userName())
                .claim("personType", session.personType())
                .claim("email", session.email())
                .setIssuedAt(now)
                .signWith(SECRET_KEY)
                .setExpiration(expiryDate)
                .compact();
    }

    public Session getSession(String accessToken) throws ExpiredJwtException,
            MalformedJwtException, SignatureException, IllegalArgumentException {
        var claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(accessToken)
                .getBody();

        return new Session(claims);
    }

    public Date getTokenExpiryDate(String token) throws ExpiredJwtException,
            MalformedJwtException, SignatureException, IllegalArgumentException {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }
}
