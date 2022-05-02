package com.plugsurfung.musify.api.wikidata.client;

import com.plugsurfung.musify.api.model.WikiDataResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static java.lang.String.format;

@Service
public class WikiDataClient {

    private final WebClient wikidataWebClient;

    public WikiDataClient(WebClient wikidataWebClient) {
        this.wikidataWebClient = wikidataWebClient;
    }

    public Mono<WikiDataResponse> getWikiData(String resourceId) {
        return wikidataWebClient
                .get()
                .uri(format("%s.json", resourceId))
                .retrieve()
                .bodyToMono(WikiDataResponse.class);
    }
}
