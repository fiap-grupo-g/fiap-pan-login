package br.com.pan.login.authentication.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "legalPerson")
public record LegalPerson(@Id String id, List<String> password, String fantasyName, String email, String cnpj,
                          boolean isActive) implements Person {

    @Override
    public String getId() {
        return id();
    }

    @Override
    public String getUserName() {
        return fantasyName();
    }

    @Override
    public String getEmail() {
        return email();
    }

    @Override
    public String getPersonType() {
        return PersonType.LEGAL.toString();
    }

    @Override
    public List<String> getPassword() {
        return password();
    }
}
