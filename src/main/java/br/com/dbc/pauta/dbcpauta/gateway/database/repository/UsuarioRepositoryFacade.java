package br.com.dbc.pauta.dbcpauta.gateway.database.repository;

import br.com.dbc.pauta.dbcpauta.gateway.database.entity.Usuario;

public interface UsuarioRepositoryFacade {
    Usuario findById(Long id);

    void save(Usuario usuario);
}
