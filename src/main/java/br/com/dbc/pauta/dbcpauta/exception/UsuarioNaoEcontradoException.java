package br.com.dbc.pauta.dbcpauta.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Data
@EqualsAndHashCode(callSuper = true)
@ResponseStatus(UNPROCESSABLE_ENTITY)
public class UsuarioNaoEcontradoException extends RuntimeException {
    public UsuarioNaoEcontradoException(Long id) {
        super("Usuário de id " + id + " não encontrado");
    }
}
