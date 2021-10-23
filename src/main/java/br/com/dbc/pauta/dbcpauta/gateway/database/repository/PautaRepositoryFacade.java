package br.com.dbc.pauta.dbcpauta.gateway.database.repository;

import br.com.dbc.pauta.dbcpauta.gateway.database.entity.Pauta;

public interface PautaRepositoryFacade {

    void delete(Pauta pauta);

    void save(Pauta pauta);

    Pauta findByNome(String nome);
}
