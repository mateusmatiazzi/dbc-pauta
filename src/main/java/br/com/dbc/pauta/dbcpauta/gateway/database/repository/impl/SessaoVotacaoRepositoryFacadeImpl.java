package br.com.dbc.pauta.dbcpauta.gateway.database.repository.impl;

import br.com.dbc.pauta.dbcpauta.gateway.database.entity.SessaoVotacao;
import br.com.dbc.pauta.dbcpauta.gateway.database.repository.SessaoVotacaoRepositoryFacade;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SessaoVotacaoRepositoryFacadeImpl implements SessaoVotacaoRepositoryFacade {

    private final SessaoVotacaoRepository sessaoVotacaoRepository;

    @Override
    public void save(SessaoVotacao sessaoVotacao) {
        sessaoVotacaoRepository.save(sessaoVotacao);
    }
}
