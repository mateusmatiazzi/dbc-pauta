package br.com.dbc.pauta.dbcpauta.usecase;

import br.com.dbc.pauta.dbcpauta.exception.BusinessException;
import br.com.dbc.pauta.dbcpauta.gateway.database.entity.SessaoVotacao;
import br.com.dbc.pauta.dbcpauta.gateway.database.entity.Usuario;
import br.com.dbc.pauta.dbcpauta.gateway.database.entity.Voto;
import br.com.dbc.pauta.dbcpauta.gateway.database.repository.PautaRepositoryFacade;
import br.com.dbc.pauta.dbcpauta.gateway.database.repository.SessaoVotacaoRepositoryFacade;
import br.com.dbc.pauta.dbcpauta.gateway.database.repository.UsuarioRepositoryFacade;
import br.com.dbc.pauta.dbcpauta.http.domain.request.VotoRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Objects.isNull;

@Service
@AllArgsConstructor
public class CadastrarVoto {

    private final UsuarioRepositoryFacade usuarioRepositoryFacade;
    private final SessaoVotacaoRepositoryFacade sessaoVotacaoRepositoryFacade;
    private final PautaRepositoryFacade pautaRepositoryFacade;

    public void executar(VotoRequest votoRequest) {
        validaSeCamposEstaoCorretos(votoRequest);

        Usuario usuario = buscaUsuario(votoRequest.getUsuarioId());
        SessaoVotacao sessaoVotacao = buscaSessaoVotacao(votoRequest.getPautaId());

        validaSeAVotacaoJaFoiEncerrada(sessaoVotacao);
        validaSeOUsuarioJaVotouNaSessao(usuario, sessaoVotacao);

        atualizaSessaoVotacao(votoRequest, usuario, sessaoVotacao);
        atualizaSessoesVotodasDoUsuario(usuario, sessaoVotacao);
    }

    private void validaSeCamposEstaoCorretos(VotoRequest votoRequest) {
        if (isNull(votoRequest)) throw new BusinessException("É necessário os dados do voto");

        if (isNull(votoRequest.getUsuarioId()))
            throw new BusinessException("É obrigatório que seja informado o Id do usuario");

        if (isNull(votoRequest.getPautaId()))
            throw new BusinessException("É obrigatório que seja informado o Id da Pauta");

        if (isNull(votoRequest.getVoto())) throw new BusinessException("É obrigatório que seja informado o voto");
    }

    private void validaSeOUsuarioJaVotouNaSessao(Usuario usuario, SessaoVotacao sessaoVotacao) {
        if (sessaoVotacao.getUsuariosQueVotaram().contains(usuario))
            throw new BusinessException("O usuário já votou nessa sessão");
    }

    private void validaSeAVotacaoJaFoiEncerrada(SessaoVotacao sessaoVotacao) {
        LocalDateTime horaLimiteParaVotacao = sessaoVotacao.getDataInicio().plusMinutes(sessaoVotacao.getTempoDeVotacao());
        LocalDateTime horaAtual = LocalDateTime.now();

        if (horaLimiteParaVotacao.isBefore(horaAtual))
            throw new BusinessException("A votação já foi encerrada");
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
