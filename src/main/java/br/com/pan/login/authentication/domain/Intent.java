package br.com.pan.login.authentication.domain;

import java.io.Serializable;

public record Intent(String intentId, PersonType personType, String userAgent) implements Serializable {
}
