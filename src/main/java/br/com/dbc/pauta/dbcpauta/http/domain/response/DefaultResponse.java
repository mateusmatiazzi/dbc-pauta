package br.com.dbc.pauta.dbcpauta.http.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DefaultResponse {
    protected String status = "SUCCESS";
    private List<String> messages = new ArrayList<>();

    public DefaultResponse(String status, String message) {
        this.status = status;
        this.messages = Collections.singletonList(message);
    }

    public void setMessage(String message) {
        messages = Collections.singletonList(message);
    }

    public void add(String message) {
        messages.add(message);
    }
}
