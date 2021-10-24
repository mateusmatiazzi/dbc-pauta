package br.com.dbc.pauta.dbcpauta.http;

import br.com.dbc.pauta.dbcpauta.http.domain.request.SessaoVotacaoRequest;
import br.com.dbc.pauta.dbcpauta.usecase.IniciarVotacao;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/api")
@Api(tags = "Votacao", produces = APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Validated
public class VotacaoWS {

    private final IniciarVotacao iniciarVotacao;

    @PostMapping("/iniciar-votacao}")
    public ResponseEntity<?> calculoValorFinanciado(@Valid @RequestBody SessaoVotacaoRequest sessaoVotacaoRequest) {
        return new ResponseEntity<>(iniciarVotacao.executar(sessaoVotacaoRequest), HttpStatus.OK);
    }
}
