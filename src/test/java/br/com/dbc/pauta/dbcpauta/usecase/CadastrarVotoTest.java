package br.com.dbc.pauta.dbcpauta.usecase;

import br.com.dbc.pauta.dbcpauta.exception.BusinessException;
import br.com.dbc.pauta.dbcpauta.gateway.database.entity.Pauta;
import br.com.dbc.pauta.dbcpauta.gateway.database.entity.SessaoVotacao;
import br.com.dbc.pauta.dbcpauta.gateway.database.entity.Usuario;
import br.com.dbc.pauta.dbcpauta.gateway.database.entity.Voto;
import br.com.dbc.pauta.dbcpauta.gateway.database.repository.PautaRepositoryFacade;
import br.com.dbc.pauta.dbcpauta.gateway.database.repository.SessaoVotacaoRepositoryFacade;
import br.com.dbc.pauta.dbcpauta.gateway.database.repository.UsuarioRepositoryFacade;
import br.com.dbc.pauta.dbcpauta.http.client.UserInfoWSClient;
import br.com.dbc.pauta.dbcpauta.http.domain.request.VotoRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static br.com.dbc.pauta.dbcpauta.gateway.database.entity.Voto.NAO;
import static br.com.dbc.pauta.dbcpauta.gateway.database.entity.Voto.SIM;
import static br.com.dbc.pauta.dbcpauta.http.client.enumeration.Permissao.ABLE_TO_VOTE;
import static br.com.dbc.pauta.dbcpauta.http.client.enumeration.Permissao.UNABLE_TO_VOTE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CadastrarVotoTest {

    @InjectMocks
    private CadastrarVoto cadastrarVoto;

    @Mock
    private PautaRepositoryFacade pautaRepositoryFacade;

    @Mock
    private SessaoVotacaoRepositoryFacade sessaoVotacaoRepositoryFacade;

    @Mock
    private UsuarioRepositoryFacade usuarioRepositoryFacade;

    @Mock
    private UserInfoWSClient userInfoWSClient;

    @Test
    void deveFalharQuandoNaoTiverOsDadosDeUmVoto() {
        BusinessException businessException = assertThrows(BusinessException.class, () -> cadastrarVoto.executar(null));
        assertEquals("É necessário os dados do voto", businessException.getMessage());
    }

    @Test
    void deveFalharQuandoNaoTiverOIdDaPauta() {
        VotoRequest votoRequest = gerarVotoRequest(SIM);
        votoRequest.setPautaId(null);

        BusinessException businessException = assertThrows(BusinessException.class, () -> cadastrarVoto.executar(votoRequest));
        assertEquals("É obrigatório que seja informado o Id da Pauta", businessException.getMessage());
    }

    @Test
    void deveFalharQuandoNaoTiverOIdDoUsuario() {
        VotoRequest votoRequest = gerarVotoRequest(SIM);
        votoRequest.setUsuarioId(null);

        BusinessException businessException = assertThrows(BusinessException.class, () -> cadastrarVoto.executar(votoRequest));
        assertEquals("É obrigatório que seja informado o Id do usuario", businessException.getMessage());
    }

    @Test
    void deveFalharQuandoNaoTiverOVoto() {
        VotoRequest votoRequest = gerarVotoRequest(SIM);
        votoRequest.setVoto(null);

        BusinessException businessException = assertThrows(BusinessException.class, () -> cadastrarVoto.executar(votoRequest));
        assertEquals("É obrigatório que seja informado o voto", businessException.getMessage());
    }

    @Test
    void deveFalharQuandoAVotacaoEstiverEncerrada() {
        VotoRequest votoRequest = gerarVotoRequest(SIM);
        when(pautaRepositoryFacade.findById(any())).thenReturn(gerarPauta(gerarSessaoPorAno(2000)));
        when(usuarioRepositoryFacade.findById(any())).thenReturn(gerarUsuario(1L));
        when(userInfoWSClient.buscaPermissaoUsuario(any())).thenReturn(ABLE_TO_VOTE);

        BusinessException businessException = assertThrows(BusinessException.class, () -> cadastrarVoto.executar(votoRequest));
        assertEquals("A votação já foi encerrada", businessException.getMessage());
    }

    @Test
    void deveFalharQuandoUmUsuarioJaVotouNaSessao() {
        VotoRequest votoRequest = gerarVotoRequest(SIM);
        when(pautaRepositoryFacade.findById(any())).thenReturn(gerarPauta(gerarSessaoPorAno(2030)));
        when(usuarioRepositoryFacade.findById(any())).thenReturn(gerarUsuario(1L));
        when(userInfoWSClient.buscaPermissaoUsuario(any())).thenReturn(ABLE_TO_VOTE);

        BusinessException businessException = assertThrows(BusinessException.class, () -> cadastrarVoto.executar(votoRequest));
        assertEquals("O usuário já votou nessa sessão", businessException.getMessage());
    }

    @Test
    void deveFalharQuandoOUsuarioNaoTiverPermissao() {
        VotoRequest votoRequest = gerarVotoRequest(SIM);
        when(pautaRepositoryFacade.findById(any())).thenReturn(gerarPauta(gerarSessaoPorAno(2030)));
        when(usuarioRepositoryFacade.findById(any())).thenReturn(gerarUsuario(1L));
        when(userInfoWSClient.buscaPermissaoUsuario(any())).thenReturn(UNABLE_TO_VOTE);

        BusinessException businessException = assertThrows(BusinessException.class, () -> cadastrarVoto.executar(votoRequest));
        assertEquals("Usuário com Cpf 11111111111 não tem permissão para votar", businessException.getMessage());
    }

    @Test
    void deveCadastrarUmVotoSim() {
        testaCadastroDeUmVoto(SIM);
    }

    @Test
    void deveCadastrarUmVotoNao() {
        testaCadastroDeUmVoto(NAO);
    }

    private void testaCadastroDeUmVoto(Voto sim) {
        VotoRequest votoRequest = gerarVotoRequest(sim);
        when(pautaRepositoryFacade.findById(any())).thenReturn(gerarPauta(gerarSessaoPorAno(2030)));
        when(usuarioRepositoryFacade.findById(any())).thenReturn(gerarUsuario(2L));
        when(userInfoWSClient.buscaPermissaoUsuario(any())).thenReturn(ABLE_TO_VOTE);

        cadastrarVoto.executar(votoRequest);

        verify(pautaRepositoryFacade).findById(any());
        verify(sessaoVotacaoRepositoryFacade).save(any());
        verify(usuarioRepositoryFacade).save(any());
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
        sessaoVotacao.setUsuariosQueVotaram(new ArrayList<>(Arrays.asList(gerarUsuario(1L), gerarUsuario(3L))));

        return sessaoVotacao;
    }

    private VotoRequest gerarVotoRequest(Voto voto) {
        VotoRequest votoRequest = new VotoRequest();
        votoRequest.setPautaId(1L);
        votoRequest.setUsuarioId(1L);
        votoRequest.setVoto(voto);

        return votoRequest;
    }

    private Usuario gerarUsuario(Long id) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setCpf("11111111111");
        usuario.setNome("Mateus");

        return usuario;
    }

}