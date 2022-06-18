package br.com.pan.login.authentication.services;


import br.com.pan.login.authentication.domain.Intent;
import br.com.pan.login.authentication.exceptions.BadRequestException;
import br.com.pan.login.authentication.exceptions.UnauthorizedException;
import br.com.pan.login.authentication.models.request.LoginRequest;
import br.com.pan.login.infrastructure.repository.SecurityRepository;
import org.springframework.data.util.Pair;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class SecurityService {

    private static final int KEYBOARD_SIZE = 10;
    private static final Integer MAX_PASSWORD_ATTEMPT = 3;
    private static final long WRONG_PASSWORD_TIMEOUT = 1;

    private final PasswordEncoder passwordEncoder;
    private final SecurityRepository securityRepository;

    public SecurityService(PasswordEncoder passwordEncoder, SecurityRepository securityRepository) {
        this.passwordEncoder = passwordEncoder;
        this.securityRepository = securityRepository;
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
        var passwd = new LinkedList<>(password);
        int x = 0;
        int y = 10;

        while (!passwd.isEmpty()) {
            var firstDigit = passwd.removeFirst();
            var lastDigit = passwd.removeLast();

            if (passwordMatches(loginRequest, firstDigit, x) || passwordMatches(loginRequest, lastDigit, y)) {
                securityRepository.wrongPasswordCount(loginRequest.cpfOrCnpj(), WRONG_PASSWORD_TIMEOUT);
                throw new BadRequestException("Senha incorreta");
            }
            x += 2;
            y -= 2;
        }
    }

    private boolean passwordMatches(LoginRequest loginRequest, String digit, int i) {
        return !passwordEncoder.matches(loginRequest.password().get(i), digit) && !passwordEncoder.matches(loginRequest.password().get(i + 1), digit);
    }

    public void isUserBlocked(String cpfOrCnpj) throws UnauthorizedException {
        var passwordAttempt = securityRepository.getWrongPasswordAttempt(cpfOrCnpj);

        if (passwordAttempt != null && passwordAttempt >= MAX_PASSWORD_ATTEMPT) {
            throw new UnauthorizedException("Senha bloqueada, porfavor contacte o suporte");
        }
    }
}

