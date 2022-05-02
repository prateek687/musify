package com.plugsurfung.musify.api.coverart.service;

import com.plugsurfung.musify.api.coverart.client.CoverArtClient;
import com.plugsurfung.musify.api.model.ArtistAlbum;
import com.plugsurfung.musify.api.model.CoverArtImageResponse;
import com.plugsurfung.musify.api.model.Image;
import com.plugsurfung.musify.api.model.ReleaseGroup;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class CoverArtService {

    private static final Logger log = LoggerFactory.getLogger(CoverArtService.class);
    private final CoverArtClient webClient;

    public CoverArtService(CoverArtClient webClient) {
        this.webClient = webClient;
    }

    public Publisher<ArtistAlbum> fetchCoverArt(ReleaseGroup releaseGroup) {

        return webClient.fetchCoverArtImage(releaseGroup.id())
                .flatMap(image -> Mono.just(new ArtistAlbum(releaseGroup.id(), releaseGroup.title(), getCoverArtImage(image).image())))
                .onErrorResume(e -> {
                    // if image is not found then we return the album without error else return error
                    if (e instanceof WebClientResponseException.NotFound) {
                        return Mono.just(new ArtistAlbum(releaseGroup.id(), releaseGroup.title(), null));
                    }
                    else return Mono.error(e);
                });
    }

    public Mono<List<ArtistAlbum>> fetchCoverArtsForAlbums(List<ReleaseGroup> releaseGroups) {
        log.info("Fetching cover arts for albums.");
        return Flux.fromStream(releaseGroups.stream())
                .flatMap(this::fetchCoverArt)
                .collectList();
    }

    private Image getCoverArtImage(CoverArtImageResponse coverArtImageResponse) {
        Image defaultImageResponse = new Image("NO IMAGE FOUND!");
        return CollectionUtils.isEmpty(coverArtImageResponse.images()) ? defaultImageResponse :
                coverArtImageResponse.images().stream().findFirst().orElseGet(() -> defaultImageResponse);
    }
}
