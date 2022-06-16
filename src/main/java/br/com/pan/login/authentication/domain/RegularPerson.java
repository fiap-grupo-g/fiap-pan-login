package br.com.pan.login.authentication.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "regularPerson")
public record RegularPerson(@Id String id, List<String> password, String firstName, String email, String birthDate,
                            String gender, boolean isActive) implements Person {

    @Override
    public String getId() {
        return id();
    }

    @Override
    public String getUserName() {
        return firstName();
    }

    @Override
    public String getEmail() {
        return email();
    }

    @Override
    public String getPersonType() {
        return PersonType.REGULAR.toString();
    }

    @Override
    public List<String> getPassword() {
        return password();
    }
}
