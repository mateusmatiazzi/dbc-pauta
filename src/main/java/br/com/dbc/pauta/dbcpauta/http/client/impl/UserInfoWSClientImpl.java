package br.com.dbc.pauta.dbcpauta.http.client.impl;

import br.com.dbc.pauta.dbcpauta.http.client.UserInfoWSClient;
import br.com.dbc.pauta.dbcpauta.http.client.configuration.UserInfoProperties;
import br.com.dbc.pauta.dbcpauta.http.client.enumeration.Permissao;
import br.com.dbc.pauta.dbcpauta.http.client.response.UserInfoResponse;
import lombok.AllArgsConstructor;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Objects;

@Component
@AllArgsConstructor
public class UserInfoWSClientImpl implements UserInfoWSClient {

    private final UserInfoProperties properties;
    private final RestTemplate restTemplate;

    private static final String API_PERMISSAO_CPF = "/users/";

    @Retryable
    public Permissao buscaPermissaoUsuario(String cpf) {
        String url = UriComponentsBuilder
                .fromHttpUrl(properties.getEndpoint())
                .path(API_PERMISSAO_CPF + cpf)
                .buildAndExpand()
                .toUriString();

        return Objects.requireNonNull(restTemplate.getForObject(url, UserInfoResponse.class)).getStatus();
    }
}
