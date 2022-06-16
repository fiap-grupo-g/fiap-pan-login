package br.com.pan.login.authentication.models.response;

public record ApiError(long timestamp, String message) {
    public ApiError(String message) {
        this(System.currentTimeMillis(), message);
    }
}