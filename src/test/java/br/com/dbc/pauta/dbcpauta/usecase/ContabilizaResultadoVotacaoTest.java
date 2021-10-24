package br.com.dbc.pauta.dbcpauta.usecase;

import br.com.dbc.pauta.dbcpauta.exception.BusinessException;
import br.com.dbc.pauta.dbcpauta.gateway.database.entity.Pauta;
import br.com.dbc.pauta.dbcpauta.gateway.database.entity.SessaoVotacao;
import br.com.dbc.pauta.dbcpauta.gateway.database.repository.PautaRepositoryFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContabilizaResultadoVotacaoTest {

    @InjectMocks
    private ContabilizaResultadoVotacao contabilizaResultadoVotacao;

    @Mock
    private PautaRepositoryFacade pautaRepositoryFacade;

    @Test
    void deveFalharQuandoNaoTiverOsDadosDaPauta() {
        BusinessException businessException = assertThrows(BusinessException.class, () -> contabilizaResultadoVotacao.executar(null));
        assertEquals("É necessário informar o id da pauta", businessException.getMessage());
    }

    @Test
    void deveFalharQuandoASessaoNaoForIniciada() {
        when(pautaRepositoryFacade.findById(any())).thenReturn(gerarPauta(null));

        BusinessException businessException = assertThrows(BusinessException.class, () -> contabilizaResultadoVotacao.executar(1L));
        assertEquals("A sessão de votação ainda não foi iniciada", businessException.getMessage());
    }

    @Test
    void deveFalharQuandoASessaoNaoEstiverExpirada() {
        when(pautaRepositoryFacade.findById(any())).thenReturn(gerarPauta(gerarSessaoPorAno(2020)));

        BusinessException businessException = assertThrows(BusinessException.class, () -> contabilizaResultadoVotacao.executar(1L));
        assertEquals("A votação ainda não foi encerrada", businessException.getMessage());
    }

    @Test
    void deveContabilizarOResultadoDaVotacaoComVencedor() {
        when(pautaRepositoryFacade.findById(any())).thenReturn(gerarPauta(gerarSessaoPorAno(2030)));

        String resultado = contabilizaResultadoVotacao.executar(1L);

        assertEquals("O resultado da Pauta de id 1 foi:\n" +
                "A votação terminou com 10 votos para SIM e 8 votos para o NÃO", resultado);
    }

    @Test
    void deveContabilizarOResultadoDaVotacaoComEmpate() {
        SessaoVotacao sessaoVotacao = gerarSessaoPorAno(2030);
        sessaoVotacao.setQteNao(10L);
        when(pautaRepositoryFacade.findById(any())).thenReturn(gerarPauta(sessaoVotacao));

        String resultado = contabilizaResultadoVotacao.executar(1L);

        assertEquals("O resultado da Pauta de id 1 foi:\n" +
                "A votação terminou em empate com 10 votos para os dois lados", resultado);
    }

    private Pauta gerarPauta(SessaoVotacao sessaoVotacao) {
        Pauta pauta = new Pauta();
        pauta.setId(1L);
        pauta.setNome("Nome");
        pauta.setDescricao("Descricao");
        pauta.setSessaoVotacao(sessaoVotacao);

        return pauta;
    }

    private SessaoVotacao gerarSessaoPorAno(int year) {
        SessaoVotacao sessaoVotacao = new SessaoVotacao();
        sessaoVotacao.setDataInicio(LocalDateTime.of(year, 10, 10, 10, 10, 10));
        sessaoVotacao.setTempoDeVotacao(5);
        sessaoVotacao.setQteSim(10L);
        sessaoVotacao.setQteNao(8L);

        return sessaoVotacao;
    }

}