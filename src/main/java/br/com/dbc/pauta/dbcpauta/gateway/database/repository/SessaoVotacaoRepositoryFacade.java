package br.com.dbc.pauta.dbcpauta.gateway.database.repository;

import br.com.dbc.pauta.dbcpauta.gateway.database.entity.SessaoVotacao;

public interface SessaoVotacaoRepositoryFacade {
    SessaoVotacao findById(Long id);

    void save(SessaoVotacao sessaoVotacao);
}
