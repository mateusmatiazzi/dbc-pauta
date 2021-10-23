package br.com.dbc.pauta.dbcpauta.http.domain.request;

import br.com.dbc.pauta.dbcpauta.http.domain.PautaDTO;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
public class PautaRequest {

    @Valid
    @NotNull
    private PautaDTO pautaDTO;
}
