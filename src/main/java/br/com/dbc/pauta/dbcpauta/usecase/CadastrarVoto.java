package br.com.dbc.pauta.dbcpauta.usecase;

import br.com.dbc.pauta.dbcpauta.exception.BusinessException;
import br.com.dbc.pauta.dbcpauta.gateway.database.entity.SessaoVotacao;
import br.com.dbc.pauta.dbcpauta.gateway.database.entity.Usuario;
import br.com.dbc.pauta.dbcpauta.gateway.database.entity.Voto;
import br.com.dbc.pauta.dbcpauta.gateway.database.repository.PautaRepositoryFacade;
import br.com.dbc.pauta.dbcpauta.gateway.database.repository.SessaoVotacaoRepositoryFacade;
import br.com.dbc.pauta.dbcpauta.gateway.database.repository.UsuarioRepositoryFacade;
import br.com.dbc.pauta.dbcpauta.http.client.UserInfoWSClient;
import br.com.dbc.pauta.dbcpauta.http.client.enumeration.Permissao;
import br.com.dbc.pauta.dbcpauta.http.domain.request.VotoRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static br.com.dbc.pauta.dbcpauta.http.client.enumeration.Permissao.UNABLE_TO_VOTE;
import static java.util.Objects.isNull;

@Service
@AllArgsConstructor
public class CadastrarVoto {

    private final UsuarioRepositoryFacade usuarioRepositoryFacade;
    private final SessaoVotacaoRepositoryFacade sessaoVotacaoRepositoryFacade;
    private final PautaRepositoryFacade pautaRepositoryFacade;
    private final UserInfoWSClient userInfoWSClient;

    public void executar(VotoRequest votoRequest) {
        validaSeCamposEstaoCorretos(votoRequest);

        Usuario usuario = buscaUsuario(votoRequest.getUsuarioId());
        SessaoVotacao sessaoVotacao = buscaSessaoVotacao(votoRequest.getPautaId());

        validaSeUsuarioTemPermissaoDeVoto(usuario);
        validaSeAVotacaoJaFoiEncerrada(sessaoVotacao);
        validaSeOUsuarioJaVotouNaSessao(usuario, sessaoVotacao);

        atualizaSessaoVotacao(votoRequest, usuario, sessaoVotacao);
        atualizaSessoesVotodasDoUsuario(usuario, sessaoVotacao);
    }

    private void validaSeCamposEstaoCorretos(VotoRequest votoRequest) {
        if (isNull(votoRequest)) throw new BusinessException("?? necess??rio os dados do voto");

        if (isNull(votoRequest.getUsuarioId()))
            throw new BusinessException("?? obrigat??rio que seja informado o Id do usuario");

        if (isNull(votoRequest.getPautaId()))
            throw new BusinessException("?? obrigat??rio que seja informado o Id da Pauta");

        if (isNull(votoRequest.getVoto())) throw new BusinessException("?? obrigat??rio que seja informado o voto");
    }

    private void validaSeOUsuarioJaVotouNaSessao(Usuario usuario, SessaoVotacao sessaoVotacao) {
        if (sessaoVotacao.getUsuariosQueVotaram().contains(usuario))
            throw new BusinessException("O usu??rio j?? votou nessa sess??o");
    }

    private void validaSeUsuarioTemPermissaoDeVoto(Usuario usuario) {
        Permissao permissao = userInfoWSClient.buscaPermissaoUsuario(usuario.getCpf());

        if (permissao.equals(UNABLE_TO_VOTE))
            throw new BusinessException("Usu??rio com Cpf " + usuario.getCpf() + " n??o tem permiss??o para votar");
    }

    private void validaSeAVotacaoJaFoiEncerrada(SessaoVotacao sessaoVotacao) {
        LocalDateTime horaLimiteParaVotacao = sessaoVotacao.getDataInicio().plusMinutes(sessaoVotacao.getTempoDeVotacao());
        LocalDateTime horaAtual = LocalDateTime.now();

        if (horaLimiteParaVotacao.isBefore(horaAtual))
            throw new BusinessException("A vota????o j?? foi encerrada");
    }

    private void atualizaSessaoVotacao(VotoRequest votoRequest, Usuario usuario, SessaoVotacao sessaoVotacao) {
        List<Usuario> usuariosQueVotaram = sessaoVotacao.getUsuariosQueVotaram();
        usuariosQueVotaram.add(usuario);
        sessaoVotacao.setUsuariosQueVotaram(usuariosQueVotaram);

        if (votoRequest.getVoto().equals(Voto.SIM)) {
            sessaoVotacao.setQteSim(sessaoVotacao.getQteSim() + 1);
        } else {
            sessaoVotacao.setQteNao(sessaoVotacao.getQteNao() + 1);
        }

        sessaoVotacaoRepositoryFacade.save(sessaoVotacao);
    }


    private void atualizaSessoesVotodasDoUsuario(Usuario usuario, SessaoVotacao sessaoVotacao) {
        List<SessaoVotacao> sessoesVotadas = usuario.getSessoesVotadas();
        sessoesVotadas.add(sessaoVotacao);
        usuario.setSessoesVotadas(sessoesVotadas);

        usuarioRepositoryFacade.save(usuario);
    }

    private Usuario buscaUsuario(Long id) {
        return usuarioRepositoryFacade.findById(id);
    }

    private SessaoVotacao buscaSessaoVotacao(Long id) {
        return pautaRepositoryFacade.findById(id).getSessaoVotacao();
    }
}
