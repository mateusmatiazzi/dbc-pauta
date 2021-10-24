package br.com.dbc.pauta.dbcpauta.http.client.response;

import br.com.dbc.pauta.dbcpauta.http.client.enumeration.Permissao;
import lombok.Data;

@Data
public class UserInfoResponse {
    private Permissao status;
}
