package br.com.pan.login.authentication.services;


import br.com.pan.login.authentication.domain.Intent;
import br.com.pan.login.authentication.exceptions.BadRequestException;
import br.com.pan.login.authentication.exceptions.UnauthorizedException;
import br.com.pan.login.authentication.models.request.LoginRequest;
import org.springframework.data.util.Pair;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class SecurityService {
    private static final int KEYBOARD_SIZE = 10;
    private static final int PASSWORD_LENGTH = 6;

    private final PasswordEncoder passwordEncoder;

    public SecurityService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public static LinkedList<Pair<Integer, Integer>> scrambledKeyboard() {
        var keyboard = new LinkedList<Pair<Integer, Integer>>();

        for (int i = 0; i < KEYBOARD_SIZE; i += 2) {
            var pair = Pair.of(i, i + 1);

            if (ThreadLocalRandom.current().nextInt() % 2 == 0) {
                keyboard.addFirst(pair);
            } else {
                keyboard.addLast(pair);
            }
        }

        return keyboard;
    }

    public void validateRequestIdentity(Intent intent, String userAgent) throws BadRequestException, UnauthorizedException {
        if (intent == null) {
            throw new BadRequestException("Por favor, realize o login novamente.");
        }

        if (!intent.userAgent().equals(userAgent)) {
            throw new UnauthorizedException("Session hijacked");
        }
    }

    public void validateScrambledPassword(LoginRequest loginRequest, List<String> password) {
        var passwd = new ArrayDeque<>(password);

        for (int i = 0; i <= PASSWORD_LENGTH; i += 2) {
            var digit = passwd.pop();

            if (!passwordEncoder.matches(loginRequest.password().get(i), digit) && !passwordEncoder.matches(loginRequest.password().get(i + 1), digit)) {
                throw new BadRequestException("Senha incorreta");
            }
        }
    }
}
