package br.com.pan.login.authentication.models.response;

public record ApiResponse<T>(long timestamp, T data) {
    public ApiResponse(T data) {
        this(System.currentTimeMillis(), data);
    }
}
