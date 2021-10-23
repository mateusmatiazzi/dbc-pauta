package br.com.dbc.pauta.dbcpauta.gateway.database.repository.impl;

import br.com.dbc.pauta.dbcpauta.gateway.database.entity.Pauta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PautaRepository extends JpaRepository<Pauta, Long> {
    Pauta findByNome(String nome);
}
