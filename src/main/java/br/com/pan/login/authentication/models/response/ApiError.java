package br.com.pan.login.authentication.models.response;

public record ApiError(String message, long timestamp) {
    public ApiError(String message) {
        this(message, System.currentTimeMillis());
    }
}