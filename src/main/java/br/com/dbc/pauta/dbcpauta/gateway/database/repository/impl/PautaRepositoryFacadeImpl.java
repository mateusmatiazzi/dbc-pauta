package br.com.dbc.pauta.dbcpauta.gateway.database.repository.impl;

import br.com.dbc.pauta.dbcpauta.gateway.database.entity.Pauta;
import br.com.dbc.pauta.dbcpauta.gateway.database.repository.PautaRepositoryFacade;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PautaRepositoryFacadeImpl implements PautaRepositoryFacade {

    private final PautaRepository pautaRepository;

    @Override
    public void delete(Pauta pauta) {
        pautaRepository.delete(pauta);
    }

    @Override
    public void save(Pauta pauta) {
        pautaRepository.save(pauta);
    }

    @Override
    public Pauta findByNome(String nome) {
        return pautaRepository.findByNome(nome);
    }
}
