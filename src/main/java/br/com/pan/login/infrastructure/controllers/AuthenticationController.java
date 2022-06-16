package br.com.pan.login.infrastructure.controllers;


import br.com.pan.login.authentication.exceptions.BadRequestException;
import br.com.pan.login.authentication.exceptions.NotFoundException;
import br.com.pan.login.authentication.exceptions.UnauthorizedException;
import br.com.pan.login.authentication.models.AuthenticatedUser;
import br.com.pan.login.authentication.models.AuthenticationIntent;
import br.com.pan.login.authentication.models.request.LoginRequest;
import br.com.pan.login.authentication.models.request.PreLoginRequest;
import br.com.pan.login.authentication.models.response.ApiResponse;
import br.com.pan.login.authentication.services.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/pre-login")
    public ResponseEntity<ApiResponse<AuthenticationIntent>> preLogin(@RequestHeader("User-Agent") String userAgent,
                                                                      @Validated @RequestBody PreLoginRequest preLoginRequest) throws BadRequestException {
        var authenticationIntent = authenticationService.preLogin(userAgent, preLoginRequest);

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(authenticationIntent));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthenticatedUser>> login(@Validated @RequestBody LoginRequest loginRequest,
                                                                @RequestHeader("X-Pan-Intent-Id") String intentId,
                                                                @RequestHeader("User-Agent") String userAgent) throws BadRequestException, UnauthorizedException, NotFoundException {
        var jwt = this.authenticationService.authenticateUser(loginRequest, intentId, userAgent);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(jwt));
    }
}
