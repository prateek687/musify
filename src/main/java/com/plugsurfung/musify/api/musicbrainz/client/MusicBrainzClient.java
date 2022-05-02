package com.plugsurfung.musify.api.musicbrainz.client;

import com.plugsurfung.musify.api.model.MusicBrainzResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class MusicBrainzClient {

    private final WebClient musicBraninzWebClient;
    private static final String FORMAT = "fmt";
    private static final String INCLUDE = "inc";
    private static final String JSON = "json";
    private static final String URL_RELS_RELEASE_GROUPS = "url-rels+release-groups";

    public MusicBrainzClient(WebClient musicBrainzWebClient) {
        this.musicBraninzWebClient = musicBrainzWebClient;
    }

    public Mono<MusicBrainzResponse> fetchMusifyArtist(String mbid) {
        return musicBraninzWebClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path(mbid)
                                .queryParam(FORMAT, JSON)
                                .queryParam(INCLUDE, URL_RELS_RELEASE_GROUPS)
                                .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(MusicBrainzResponse.class);
    }
}
