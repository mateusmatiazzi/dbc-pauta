package br.com.dbc.pauta.dbcpauta.usecase;

import br.com.dbc.pauta.dbcpauta.exception.BusinessException;
import br.com.dbc.pauta.dbcpauta.gateway.database.entity.Pauta;
import br.com.dbc.pauta.dbcpauta.gateway.database.entity.SessaoVotacao;
import br.com.dbc.pauta.dbcpauta.gateway.database.repository.PautaRepositoryFacade;
import br.com.dbc.pauta.dbcpauta.gateway.database.repository.SessaoVotacaoRepositoryFacade;
import br.com.dbc.pauta.dbcpauta.http.domain.request.SessaoVotacaoRequest;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static java.util.Objects.isNull;

@Service
@AllArgsConstructor
public class IniciarVotacao {

    private final SessaoVotacaoRepositoryFacade sessaoVotacaoRepositoryFacade;
    private final PautaRepositoryFacade pautaRepositoryFacade;

    public SessaoVotacao executar(SessaoVotacaoRequest request) {
        validaSeCamposEstaoCorretos(request);
        return salvaNovaSessaodeVotos(request);
    }

    private void validaSeCamposEstaoCorretos(SessaoVotacaoRequest request) {
        if (isNull(request)) throw new BusinessException("É necessário os dados da sessão de votação");

        if (isNull(request.getPautaId()))
            throw new BusinessException("É obrigatório que seja informado o Id da pauta que terá sua sessão iniciada");

        if (isNull(buscaPauta(request.getPautaId()))) throw new BusinessException("Pauta não encontrada");

        if (request.getSessaoDTO().getTempoDeVotacao() <= 0)
            throw new BusinessException("Tempo de votação deverá ser maior que zero");
    }

    private Pauta buscaPauta(Long pautaId) {
        return pautaRepositoryFacade.findById(pautaId);
    }

    private SessaoVotacao salvaNovaSessaodeVotos(SessaoVotacaoRequest request) {
        Pauta pauta = buscaPauta(request.getPautaId());
        SessaoVotacao sessaoVotacao = DozerBeanMapperBuilder.buildDefault().map(request.getSessaoDTO(), SessaoVotacao.class);

        sessaoVotacao.setDataInicio(LocalDateTime.now());
        sessaoVotacao.setPauta(pauta);
        sessaoVotacaoRepositoryFacade.save(sessaoVotacao);
        pauta.setSessaoVotacao(sessaoVotacao);
        pautaRepositoryFacade.save(pauta);
        return sessaoVotacao;
    }

}
