package br.com.pan.login.infrastructure.controllers;

import br.com.pan.login.infrastructure.repository.SessionRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {
    private final SessionRepository sessionRepository;

    public TokenController(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @GetMapping("/token/{id}")
    public String getToken(@PathVariable("id") String key) {
        return sessionRepository.getSession(key);
    }
}
