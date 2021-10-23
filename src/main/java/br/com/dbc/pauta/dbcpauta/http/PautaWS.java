package br.com.dbc.pauta.dbcpauta.http;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/api")
@Api(tags = "Pauta", produces = APPLICATION_JSON_VALUE)
@CrossOrigin
public class PautaWS {

    @RequestMapping(value = "/teste", method = RequestMethod.GET)
    public String calculoValorFinanciado() {
        return "Hello World!";
    }
}
