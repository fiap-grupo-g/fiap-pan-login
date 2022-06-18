package br.com.pan.login.infrastructure.config;

import br.com.pan.login.authentication.domain.Intent;
import com.mongodb.client.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class DatabaseConfig {

    @Value("${br.com.pan.mongo.database}")
    private String database;

    @Bean(name = "sessionTemplate")
    public RedisTemplate<String, String> sessionTemplate(RedisConnectionFactory connectionFactory) {
        var template = new RedisTemplate<String, String>();
        template.setConnectionFactory(connectionFactory);

        return template;
    }

    @Bean(name = "authenticationTemplate")
    public RedisTemplate<String, Intent> authenticationTemplate(RedisConnectionFactory connectionFactory) {
        var template = new RedisTemplate<String, Intent>();
        template.setConnectionFactory(connectionFactory);

        return template;
    }

    @Bean(name = "accessSecurityControlTemplate")
    public StringRedisTemplate accessSecurityControlTemplate(RedisConnectionFactory connectionFactory) {
        var template = new StringRedisTemplate();
        template.setConnectionFactory(connectionFactory);

        return template;
    }


    @Bean
    public MongoTemplate mongoTemplate(MongoClient client) {
        return new MongoTemplate(client, database);
    }
}
