package br.com.pan.login.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtConfig {
    @Value("${br.com.pan.jwt.secret}")
    private String secret;
    @Value("${br.com.pan.jwt.tokenPrefix}")
    private String tokenPrefix;
    @Value("${br.com.pan.jwt.header}")
    private String header;
    @Value("${br.com.pan.jwt.expirationTimeout}")
    private long expirationTimeout;
    @Value("${br.com.pan.jwt.expirationTimeout}")
    private long minimumTimeout;

    public String getSecret() {
        return secret;
    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }

    public String getHeader() {
        return header;
    }

    public long getExpirationTimeout() {
        return expirationTimeout;
    }

    public long getMinimumTimeout() {
        return minimumTimeout;
    }
}
