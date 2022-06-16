package br.com.pan.login.authentication.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends Exception {

    public static final String DEFAULT_UNAUTHORIZED_MESSAGE = "Ops! Você não está autorizado a visualizar este recurso.";

    public static final String DISABLED_ACCESS = "Você não está autorizado a acessar esta aplicação.";

    public UnauthorizedException(String message) {
        super(message);
    }
}
