package br.com.dbc.pauta.dbcpauta.http.domain.request;

import br.com.dbc.pauta.dbcpauta.gateway.database.entity.Voto;
import lombok.Data;

@Data
public class VotoRequest {
    private Long usuarioId;
    private Long pautaId;
    private Voto voto;
}
