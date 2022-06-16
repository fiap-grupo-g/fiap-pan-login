package br.com.pan.login.authentication.models;

import br.com.pan.login.authentication.domain.Person;
import io.jsonwebtoken.Claims;

public record Session(String id,
                      String userName,
                      String personType,
                      String email) {
    public Session(Person person) {
        this(person.getId(), person.getUserName(), person.getPersonType(), person.getEmail());
    }

    public Session(Claims claims) {
        this(claims.getId(), claims.get("userName", String.class), claims.get("personType", String.class), claims.get("email", String.class));
    }
}