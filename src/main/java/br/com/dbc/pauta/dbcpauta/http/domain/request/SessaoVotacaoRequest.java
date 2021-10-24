package br.com.dbc.pauta.dbcpauta.http.domain.request;

import br.com.dbc.pauta.dbcpauta.http.domain.SessaoDTO;
import lombok.Data;

@Data
public class SessaoVotacaoRequest {

    private Long pautaId;
    private SessaoDTO sessaoDTO;
}
