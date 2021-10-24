package br.com.dbc.pauta.dbcpauta.gateway.database.repository.impl;

import br.com.dbc.pauta.dbcpauta.gateway.database.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
