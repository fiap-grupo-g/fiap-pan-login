package br.com.pan.login.authentication.models;

public record AuthenticatedUser(String username, Token token) {
}
