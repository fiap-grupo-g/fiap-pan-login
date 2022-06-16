package br.com.pan.login.authentication.models.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record LoginRequest(@NotBlank String cpfOrCnpj, @NotEmpty List<String> password) {
}