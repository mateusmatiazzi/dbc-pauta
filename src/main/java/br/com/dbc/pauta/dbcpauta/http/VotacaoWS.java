package br.com.dbc.pauta.dbcpauta.http;

import br.com.dbc.pauta.dbcpauta.http.domain.request.SessaoVotacaoRequest;
import br.com.dbc.pauta.dbcpauta.http.domain.request.VotoRequest;
import br.com.dbc.pauta.dbcpauta.usecase.CadastrarVoto;
import br.com.dbc.pauta.dbcpauta.usecase.ContabilizaResultadoVotacao;
import br.com.dbc.pauta.dbcpauta.usecase.IniciarVotacao;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/api")
@Api(tags = "Votacao", produces = APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Validated
public class VotacaoWS {

    private final IniciarVotacao iniciarVotacao;
    private final CadastrarVoto cadastrarVoto;
    private final ContabilizaResultadoVotacao contabilizaResultadoVotacao;

    @PostMapping("/iniciar-votacao")
    public ResponseEntity<?> iniciarVotacao(@Valid @RequestBody SessaoVotacaoRequest sessaoVotacaoRequest) {
        return new ResponseEntity<>(iniciarVotacao.executar(sessaoVotacaoRequest), HttpStatus.OK);
    }

    @PostMapping("/cadastrar-voto")
    public ResponseEntity<?> cadastrarVoto(@Valid @RequestBody VotoRequest votoRequest) {
        cadastrarVoto.executar(votoRequest);
        return new ResponseEntity<>("Voto Cadastrado com Sucesso", HttpStatus.OK);
    }

    @GetMapping("/resultado-votacao/{id}")
    public ResponseEntity<?> retornaResultadoVotacao(@PathVariable @ApiParam("Pauta id") Long id) {
        return new ResponseEntity<>(contabilizaResultadoVotacao.executar(id), HttpStatus.OK);
    }
}
