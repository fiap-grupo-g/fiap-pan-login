package br.com.pan.login.authentication.services;

import br.com.pan.login.authentication.domain.Intent;
import br.com.pan.login.authentication.exceptions.BadRequestException;
import br.com.pan.login.authentication.exceptions.NotFoundException;
import br.com.pan.login.authentication.exceptions.UnauthorizedException;
import br.com.pan.login.authentication.models.AuthenticatedUser;
import br.com.pan.login.authentication.models.AuthenticationIntent;
import br.com.pan.login.authentication.models.Session;
import br.com.pan.login.authentication.models.Token;
import br.com.pan.login.authentication.models.request.LoginRequest;
import br.com.pan.login.authentication.models.request.PreLoginRequest;
import br.com.pan.login.infrastructure.config.JwtConfig;
import br.com.pan.login.infrastructure.repository.IntentRepository;
import br.com.pan.login.infrastructure.repository.SessionRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
public class AuthenticationService {

    private static final long PRE_LOGIN_TIMEOUT = Duration.ofMinutes(3).toMillis();

    private final JwtConfig jwtConfig;
    private final JwtService jwtService;
    private final PersonService personService;
    private final SecurityService securityService;
    private final IntentRepository intentRepository;
    private final SessionRepository sessionRepository;

    public AuthenticationService(JwtConfig jwtConfig, JwtService jwtService,
                                 PersonService personService, SecurityService securityService,
                                 IntentRepository intentRepository, SessionRepository sessionRepository) {
        this.jwtConfig = jwtConfig;
        this.jwtService = jwtService;
        this.personService = personService;
        this.securityService = securityService;
        this.intentRepository = intentRepository;
        this.sessionRepository = sessionRepository;
    }

    public AuthenticationIntent preLogin(String userAgent, PreLoginRequest preLoginRequest) throws UnauthorizedException {
        securityService.isUserBlocked(preLoginRequest.cpfOrCnpj());

        var person = personService.getPersonType(preLoginRequest.cpfOrCnpj());
        var keyboard = SecurityService.scrambledKeyboard();
        var intentId = UUID.randomUUID().toString();

        intentRepository.saveLoginIntent(new Intent(intentId, person, userAgent), PRE_LOGIN_TIMEOUT);

        return new AuthenticationIntent(intentId, keyboard);
    }

    public AuthenticatedUser authenticateUser(LoginRequest loginRequest, String intentId, String userAgent) throws BadRequestException, UnauthorizedException, NotFoundException {
        securityService.isUserBlocked(loginRequest.cpfOrCnpj());

        var intent = intentRepository.getLoginIntent(intentId);
        securityService.validateRequestIdentity(intent, userAgent);
        var person = personService.getPerson(intent, loginRequest.cpfOrCnpj());
        securityService.validateScrambledPassword(loginRequest, person.getPassword());

        if (!person.isActive()) {
            throw new UnauthorizedException(UnauthorizedException.DISABLED_ACCESS);
        }

        var session = new Session(person);
        var accessToken = jwtService.generateToken(session);
        var shadowToken = sessionRepository.saveSession(accessToken, jwtConfig.getExpirationTimeout());

        return buildAccessToken(accessToken, shadowToken, session);
    }

    private AuthenticatedUser buildAccessToken(String accessToken, String shadowToken, Session session) {
        var expiryDate = jwtService.getTokenExpiryDate(accessToken);
        var token = new Token(jwtConfig.getTokenPrefix(), shadowToken, expiryDate.getTime());

        return new AuthenticatedUser(session.userName(), token);
    }
}
