package br.com.dbc.pauta.dbcpauta.usecase;

import br.com.dbc.pauta.dbcpauta.exception.BusinessException;
import br.com.dbc.pauta.dbcpauta.gateway.database.entity.Pauta;
import br.com.dbc.pauta.dbcpauta.gateway.database.entity.SessaoVotacao;
import br.com.dbc.pauta.dbcpauta.gateway.database.repository.PautaRepositoryFacade;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static java.util.Objects.isNull;

@Service
@AllArgsConstructor
public class ContabilizaResultadoVotacao {

    private final PautaRepositoryFacade pautaRepositoryFacade;

    public String executar(Long pautaId) {
        validaSeCamposEstaoCorretos(pautaId);

        Pauta pauta = buscaPauta(pautaId);
        validaSeASessaoFoiIniciada(pauta);

        SessaoVotacao sessaoVotacao = pauta.getSessaoVotacao();
        validaSeAVotacaoFoiEncerrada(sessaoVotacao);

        return geraStringDoResultado(pautaId) + contabilizaVotos(sessaoVotacao);
    }

    private void validaSeCamposEstaoCorretos(Long pautaId) {
        if (isNull(pautaId)) throw new BusinessException("É necessário informar o id da pauta");
    }

    private Pauta buscaPauta(Long pautaId) {
        return pautaRepositoryFacade.findById(pautaId);
    }

    private void validaSeASessaoFoiIniciada(Pauta pauta) {
        if (isNull(pauta.getSessaoVotacao())) throw new BusinessException("A sessão de votação ainda não foi iniciada");
    }


    private void validaSeAVotacaoFoiEncerrada(SessaoVotacao sessaoVotacao) {
        if (isSessaoExpirada(sessaoVotacao)) throw new BusinessException("A votação ainda não foi encerrada");
    }

    private boolean isSessaoExpirada(SessaoVotacao sessaoVotacao) {
        LocalDateTime horaLimiteParaVotacao = sessaoVotacao.getDataInicio().plusMinutes(sessaoVotacao.getTempoDeVotacao());
        LocalDateTime horaAtual = LocalDateTime.now();

        return horaLimiteParaVotacao.isBefore(horaAtual);
    }


    private String contabilizaVotos(SessaoVotacao sessaoVotacao) {
        if (sessaoVotacao.getQteSim().equals(sessaoVotacao.getQteNao()))
            return "A votação terminou em empate com " + sessaoVotacao.getQteSim() + " votos para os dois lados";

        return "A votação terminou com " + sessaoVotacao.getQteSim() + " votos para SIM e " + sessaoVotacao.getQteNao() + " votos para o NÃO";
    }

    private String geraStringDoResultado(Long pautaId) {
        return "O resultado da Pauta de id " + pautaId.toString() + " foi:\n";
    }
}
