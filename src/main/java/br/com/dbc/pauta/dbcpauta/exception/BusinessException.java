package br.com.dbc.pauta.dbcpauta.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;


@Data
@EqualsAndHashCode(callSuper = true)
@ResponseStatus(UNPROCESSABLE_ENTITY)
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 4411901247684745331L;
    private final String messages;

    public BusinessException(String msg) {
        super(msg);
        this.messages = msg;
    }

}
