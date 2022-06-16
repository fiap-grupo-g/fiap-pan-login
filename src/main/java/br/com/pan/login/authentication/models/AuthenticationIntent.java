package br.com.pan.login.authentication.models;

import org.springframework.data.util.Pair;

import java.util.List;

public record AuthenticationIntent(String intentId, List<Pair<Integer, Integer>> keyboard) {
}
