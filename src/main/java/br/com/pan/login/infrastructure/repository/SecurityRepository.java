package br.com.pan.login.infrastructure.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.Duration;

@Repository
public class SecurityRepository {

    private final StringRedisTemplate accessSecurityControlTemplate;

    public SecurityRepository(@Qualifier("accessSecurityControlTemplate") StringRedisTemplate accessSecurityControlTemplate) {
        this.accessSecurityControlTemplate = accessSecurityControlTemplate;
    }

    public Integer getWrongPasswordAttempt(String cpfOrCnpj) {
        var attempts = accessSecurityControlTemplate.opsForValue().get(cpfOrCnpj);

        return StringUtils.hasText(attempts) ? Integer.parseInt(attempts) : 0;
    }

    public void wrongPasswordCount(String cpfOrCnpj, long timeout) {
        var hasKey = accessSecurityControlTemplate.hasKey(cpfOrCnpj);

        if (Boolean.TRUE.equals(hasKey)) {
            accessSecurityControlTemplate.opsForValue().increment(cpfOrCnpj);
        } else {
            accessSecurityControlTemplate.opsForValue().set(cpfOrCnpj, "1", Duration.ofDays(timeout));
        }
    }
}
