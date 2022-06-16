package br.com.pan.login.authentication.models;

public record Token(String tokenType, String accessToken, long tokenExpiry) {
}
