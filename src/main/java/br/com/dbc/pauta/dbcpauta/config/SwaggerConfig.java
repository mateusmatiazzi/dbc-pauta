package br.com.dbc.pauta.dbcpauta.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@RequiredArgsConstructor
public class SwaggerConfig {

    @Value("${server.host:localhost}")
    private String swaggerHost;

    @Value("${server.port:8080}")
    private String swaggerPort;

    @Bean
    public Docket config() {
        return new Docket(DocumentationType.SWAGGER_2)
                .host(swaggerHost + ":" + swaggerPort)
                .select()
                .apis(RequestHandlerSelectors.basePackage("br.com.dbc.pauta"))
                .paths(PathSelectors.any())
                .build();
    }

}
