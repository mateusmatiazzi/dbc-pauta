package br.com.dbc.pauta.dbcpauta.usecase;

import br.com.dbc.pauta.dbcpauta.exception.BusinessException;
import br.com.dbc.pauta.dbcpauta.gateway.database.entity.Pauta;
import br.com.dbc.pauta.dbcpauta.gateway.database.repository.PautaRepositoryFacade;
import br.com.dbc.pauta.dbcpauta.http.domain.PautaDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CadastrarPautaTest {

    @InjectMocks
    private CadastrarPauta cadastrarPauta;

    @Mock
    private PautaRepositoryFacade pautaRepositoryFacade;

    @Test
    void deveFalharQuandoNaoTiverOsDadosDeUmaPauta() {
        BusinessException businessException = assertThrows(BusinessException.class, () -> cadastrarPauta.executar(null));
        assertEquals("É necessário os dados da pauta", businessException.getMessage());
    }

    @Test
    void deveFalharQuandoNaoTiverNomeDaPauta() {
        PautaDTO pautaDTO = gerarPautaDTO();
        pautaDTO.setNome(null);

        BusinessException businessException = assertThrows(BusinessException.class, () -> cadastrarPauta.executar(pautaDTO));
        assertEquals("É obrigatório que a pauta tenha um nome", businessException.getMessage());
    }

    @Test
    void deveFalharQuandoNaoTiverDescricaoDaPauta() {
        PautaDTO pautaDTO = gerarPautaDTO();
        pautaDTO.setDescricao(null);

        BusinessException businessException = assertThrows(BusinessException.class, () -> cadastrarPauta.executar(pautaDTO));
        assertEquals("É obrigatório que a pauta tenha uma descrição", businessException.getMessage());
    }

    @Test
    void deveFalharQuandoTiverUmaPautaComOMesmoNome() {
        PautaDTO pautaDTO = gerarPautaDTO();
        when(pautaRepositoryFacade.findByNome(any())).thenReturn(new Pauta());

        BusinessException businessException = assertThrows(BusinessException.class, () -> cadastrarPauta.executar(pautaDTO));
        assertEquals("Já existe uma pauta com esse nome", businessException.getMessage());
    }

    @Test
    void deveCriarUmaNovaPauta() {
        cadastrarPauta.executar(gerarPautaDTO());
        verify(pautaRepositoryFacade).save(any());
    }

    private PautaDTO gerarPautaDTO() {
        PautaDTO pautaDTO = new PautaDTO();
        pautaDTO.setNome("Nome da Pauta");
        pautaDTO.setDescricao("Descricao");
        pautaDTO.setId(1L);

        return pautaDTO;
    }
}