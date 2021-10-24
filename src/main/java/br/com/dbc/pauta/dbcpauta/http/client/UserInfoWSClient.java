package br.com.dbc.pauta.dbcpauta.http.client;

import br.com.dbc.pauta.dbcpauta.http.client.enumeration.Permissao;

public interface UserInfoWSClient {
    Permissao buscaPermissaoUsuario(String cpf);
}
