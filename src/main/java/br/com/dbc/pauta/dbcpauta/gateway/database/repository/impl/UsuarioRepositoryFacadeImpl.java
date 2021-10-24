package br.com.dbc.pauta.dbcpauta.gateway.database.repository.impl;

import br.com.dbc.pauta.dbcpauta.exception.UsuarioNaoEcontradoException;
import br.com.dbc.pauta.dbcpauta.gateway.database.entity.Usuario;
import br.com.dbc.pauta.dbcpauta.gateway.database.repository.UsuarioRepositoryFacade;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UsuarioRepositoryFacadeImpl implements UsuarioRepositoryFacade {

    private final UsuarioRepository usuarioRepository;

    @Override
    public Usuario findById(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new UsuarioNaoEcontradoException(id));
    }

    @Override
    public void save(Usuario usuario) {
        usuarioRepository.save(usuario);
    }
}
