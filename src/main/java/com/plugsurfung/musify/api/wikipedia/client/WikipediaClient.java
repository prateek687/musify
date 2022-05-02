package com.plugsurfung.musify.api.wikipedia.client;

import com.plugsurfung.musify.api.model.WikipediaResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class WikipediaClient {

    private final WebClient wikipediaWebClient;

    public WikipediaClient(WebClient wikipediaWebClient) {
        this.wikipediaWebClient = wikipediaWebClient;
    }

    public Mono<WikipediaResponse> getDescriptionFromWikipedia(String title) {
        return wikipediaWebClient
                .get()
                .uri(title)
                .retrieve()
                .bodyToMono(WikipediaResponse.class);
    }
}
