package br.com.pan.login.infrastructure.repository;


import br.com.pan.login.authentication.domain.Intent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DigestUtils;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
public class SessionRepository {

    private final RedisTemplate<String, String> session;
    private final RedisTemplate<String, Intent> authentication;

    public SessionRepository(@Qualifier("sessionTemplate") RedisTemplate<String, String> session,
                             @Qualifier("authenticationTemplate") RedisTemplate<String, Intent> loginIntent) {
        this.session = session;
        this.authentication = loginIntent;
    }

    public String saveSession(String accessToken, long timeout) {
        var key = DigestUtils.sha1DigestAsHex(accessToken);
        session.opsForValue().set(key, accessToken, Duration.ofMillis(timeout));

        return key;
    }

    public String getSession(String key) {
        return session.opsForValue().get(key);
    }

    public void saveLoginIntent(Intent intent, long timeout) {
        authentication.opsForValue().set(intent.intentId(), intent, Duration.ofMillis(timeout));
    }

    public Intent getLoginIntent(String intentId) {
        return authentication.opsForValue().get(intentId);
    }
}
