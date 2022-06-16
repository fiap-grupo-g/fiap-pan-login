package br.com.pan.login.authentication.domain;

import java.util.List;

public interface Person {

    String getId();

    String getUserName();

    String getEmail();

    String getPersonType();

    List<String> getPassword();

    boolean isActive();
}
