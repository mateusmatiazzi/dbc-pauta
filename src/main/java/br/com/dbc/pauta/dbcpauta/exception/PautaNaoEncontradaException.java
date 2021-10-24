package br.com.dbc.pauta.dbcpauta.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Data
@EqualsAndHashCode(callSuper = true)
@ResponseStatus(UNPROCESSABLE_ENTITY)
public class PautaNaoEncontradaException extends RuntimeException {
    public PautaNaoEncontradaException(Long id) {
        super("Pauta de id " + id + " não encontrada");
    }
}
