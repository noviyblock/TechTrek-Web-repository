package com.startupgame.config;

import com.startupgame.client.MLApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class RestClientConfig {

    @Bean
    RestClient mlRestClient(@Value("${ml.base-url}") String baseUrl) {
        return RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    @Bean
    MLApiClient mlApiClient(RestClient mlRestClient) {
        return HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(mlRestClient))
                .build()
                .createClient(MLApiClient.class);
    }

}