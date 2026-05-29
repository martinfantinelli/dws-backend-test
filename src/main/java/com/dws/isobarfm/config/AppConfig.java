package com.dws.isobarfm.config;

import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.client.RestClient;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Bean
    public RestClient restClient(ApiProperties apiProperties) {
        ClientHttpRequestFactorySettings settings = ClientHttpRequestFactorySettings.DEFAULTS
            .withConnectTimeout(apiProperties.connectTimeout())
            .withReadTimeout(apiProperties.readTimeout());

        return RestClient.builder()
            .baseUrl(apiProperties.baseUrl())
            .requestFactory(ClientHttpRequestFactories.get(settings))
            .build();
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToBandSortConverter());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods("GET");
    }
}
