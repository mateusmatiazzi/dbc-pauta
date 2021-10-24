package br.com.dbc.pauta.dbcpauta.http.domain.response;

import br.com.dbc.pauta.dbcpauta.http.domain.SessaoDTO;
import lombok.Data;

@Data
public class SessaoVotacaoResponse {
    private String mensagem;
    private SessaoDTO sessaoDTO;
}
