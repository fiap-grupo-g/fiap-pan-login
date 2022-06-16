package br.com.pan.login.authentication.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ApiException extends RuntimeException {

    public static final String BACKEND_UNAVAILABLE = "Um erro inesperado aconteceu, tente novamente";

    public ApiException(String message) {
        super(message);
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
