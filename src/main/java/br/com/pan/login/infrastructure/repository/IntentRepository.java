package br.com.pan.login.infrastructure.repository;

import br.com.pan.login.authentication.domain.Intent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;


@Repository
public class IntentRepository {

    private final RedisTemplate<String, Intent> intentTemplate;

    public IntentRepository(@Qualifier("authenticationTemplate") RedisTemplate<String, Intent> loginIntent) {
        this.intentTemplate = loginIntent;
    }

    public Intent getLoginIntent(String intentId) {
        return intentTemplate.opsForValue().get(intentId);
    }

    public void saveLoginIntent(Intent intent, long timeout) {
        this.intentTemplate.opsForValue().set(intent.intentId(), intent, Duration.ofMillis(timeout));
    }
}