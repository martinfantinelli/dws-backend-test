package com.dws.isobarfm.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("isobar.fm API")
                .description("REST API that proxies band and album data from the isobar.fm catalogue. " +
                    "Supports listing, searching, and sorting bands, and retrieving full album details with tracklists.")
                .version("1.0.0")
                .contact(new Contact()
                    .name("DWS – Dentsu World Services")
                    .url("https://www.dentsu.com")))
            .servers(List.of(
                new Server().url("http://localhost:8080").description("Local development")
            ));
    }
}
