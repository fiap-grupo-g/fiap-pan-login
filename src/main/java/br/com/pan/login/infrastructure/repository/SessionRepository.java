package br.com.pan.login.infrastructure.repository;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DigestUtils;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
public class SessionRepository {

    private final RedisTemplate<String, String> session;

    public SessionRepository(@Qualifier("sessionTemplate") RedisTemplate<String, String> session) {
        this.session = session;
    }

    public String getSession(String key) {
        return session.opsForValue().get(key);
    }

    public String saveSession(String accessToken, long timeout) {
        var key = DigestUtils.sha1DigestAsHex(accessToken);
        session.opsForValue().set(key, accessToken, Duration.ofMillis(timeout));

        return key;
    }
}
