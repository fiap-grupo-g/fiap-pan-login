package br.com.pan.login.authentication.models.request;

import jakarta.validation.constraints.NotBlank;

public record PreLoginRequest(@NotBlank String cpfOrCnpj) {
}
