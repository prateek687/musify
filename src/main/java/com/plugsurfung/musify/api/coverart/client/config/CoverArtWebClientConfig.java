package com.plugsurfung.musify.api.coverart.client.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;

@Configuration
public class CoverArtWebClientConfig {

    @Value("${webClient.maxConnections}")
    private int maxConnections;

    @Value("${webClient.idleTime}")
    private int idleTime;

    @Value("${webClient.lifeTime}")
    private int lifeTime;

    @Value("${webClient.responseTimeout}")
    private int responseTimeOut;

    @Value("${webClient.maxInMemorySize}")
    private Integer maxInMemorySize;

    @Value("${api.url.coverart}")
    private String coverArtUrl;

    @Bean(name = "coverArtWebClient")
    public WebClient coverArtWebClient() {

        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(maxInMemorySize))
                .build();
        ConnectionProvider connectionProvider =
                ConnectionProvider.builder("coverArtWebClientBuilder")
                        .maxConnections(maxConnections)
                        .maxIdleTime(Duration.ofMillis(idleTime))
                        .maxLifeTime(Duration.ofMillis(lifeTime))
                        .build();
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(
                        HttpClient.create(connectionProvider)
                                .responseTimeout(Duration.ofSeconds(responseTimeOut))
                                .followRedirect(true)
                ))
                .baseUrl(coverArtUrl)
                .exchangeStrategies(strategies)
                .build();
    }

}
