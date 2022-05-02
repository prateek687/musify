package com.plugsurfung.musify.api.coverart.client;

import com.plugsurfung.musify.api.model.CoverArtImageResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class CoverArtClient {
    private final WebClient coverArtWebClient;

    public CoverArtClient(WebClient coverArtWebClient) {
        this.coverArtWebClient = coverArtWebClient;
    }

    public Mono<CoverArtImageResponse> fetchCoverArtImage(String releaseGroupId) {
        return coverArtWebClient
                .get()
                .uri(releaseGroupId)
                .retrieve()
                .bodyToMono(CoverArtImageResponse.class);
    }
}
