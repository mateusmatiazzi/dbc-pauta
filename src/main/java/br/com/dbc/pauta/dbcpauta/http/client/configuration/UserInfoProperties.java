package br.com.dbc.pauta.dbcpauta.http.client.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Data
@Configuration
@ConfigurationProperties(prefix = "api.user-info")
public class UserInfoProperties {
    private String endpoint;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
