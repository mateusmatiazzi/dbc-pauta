package br.com.dbc.pauta.dbcpauta.usecase;

import br.com.dbc.pauta.dbcpauta.exception.BusinessException;
import br.com.dbc.pauta.dbcpauta.gateway.database.entity.Pauta;
import br.com.dbc.pauta.dbcpauta.gateway.database.entity.SessaoVotacao;
import br.com.dbc.pauta.dbcpauta.gateway.database.repository.PautaRepositoryFacade;
import br.com.dbc.pauta.dbcpauta.gateway.database.repository.SessaoVotacaoRepositoryFacade;
import br.com.dbc.pauta.dbcpauta.http.domain.SessaoDTO;
import br.com.dbc.pauta.dbcpauta.http.domain.request.SessaoVotacaoRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IniciarVotacaoTest {

    @InjectMocks
    private IniciarVotacao iniciarVotacao;

    @Mock
    private PautaRepositoryFacade pautaRepositoryFacade;

    @Mock
    private SessaoVotacaoRepositoryFacade sessaoVotacaoRepositoryFacade;

    @Test
    void deveFalharQuandoNaoTiverOsDadosDeUmaSessao() {
        BusinessException businessException = assertThrows(BusinessException.class, () -> iniciarVotacao.executar(null));
        assertEquals("É necessário os dados da sessão de votação", businessException.getMessage());
    }

    @Test
    void deveFalharQuandoNaoTiverOIdDaPauta() {
        SessaoVotacaoRequest sessaoVotacaoRequest = gerarSessaoRequest();
        sessaoVotacaoRequest.setPautaId(null);

        BusinessException businessException = assertThrows(BusinessException.class, () -> iniciarVotacao.executar(sessaoVotacaoRequest));
        assertEquals("É obrigatório que seja informado o Id da pauta que terá sua sessão iniciada", businessException.getMessage());
    }

    @Test
    void deveFalharQuandoTempoDeVotacao() {
        SessaoVotacaoRequest sessaoVotacaoRequest = gerarSessaoRequest();
        sessaoVotacaoRequest.getSessaoDTO().setTempoDeVotacao(0);
        when(pautaRepositoryFacade.findById(any())).thenReturn(new Pauta());

        BusinessException businessException = assertThrows(BusinessException.class, () -> iniciarVotacao.executar(sessaoVotacaoRequest));
        assertEquals("Tempo de votação deverá ser maior que zero", businessException.getMessage());
    }

    @Test
    void deveFalharQuandoNaoExistirAPauta() {
        SessaoVotacaoRequest sessaoVotacaoRequest = gerarSessaoRequest();
        when(pautaRepositoryFacade.findById(any())).thenReturn(null);

        BusinessException businessException = assertThrows(BusinessException.class, () -> iniciarVotacao.executar(sessaoVotacaoRequest));
        assertEquals("Pauta não encontrada", businessException.getMessage());
    }

    @Test
    void deveIniciarUmaSessaoDeVotacao() {
        SessaoVotacaoRequest sessaoVotacaoRequest = gerarSessaoRequest();
        when(pautaRepositoryFacade.findById(any())).thenReturn(new Pauta());

        SessaoVotacao sessaoVotacao = iniciarVotacao.executar(sessaoVotacaoRequest);

        assertEquals(1, sessaoVotacao.getTempoDeVotacao());
        verify(pautaRepositoryFacade, times(2)).findById(any());
        verify(pautaRepositoryFacade).save(any());
        verify(sessaoVotacaoRepositoryFacade).save(any());
    }

    private SessaoVotacaoRequest gerarSessaoRequest() {
        SessaoVotacaoRequest sessaoVotacaoRequest = new SessaoVotacaoRequest();
        sessaoVotacaoRequest.setPautaId(1L);
        sessaoVotacaoRequest.setSessaoDTO(gerarSessaoDTO());

        return sessaoVotacaoRequest;
    }

    private SessaoDTO gerarSessaoDTO() {
        SessaoDTO sessaoDTO = new SessaoDTO();
        sessaoDTO.setId(1L);
        sessaoDTO.setTempoDeVotacao(1);

        return sessaoDTO;
    }
}