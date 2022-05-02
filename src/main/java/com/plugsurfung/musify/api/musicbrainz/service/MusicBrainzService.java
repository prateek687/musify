package com.plugsurfung.musify.api.musicbrainz.service;

import com.plugsurfung.musify.api.model.MusicBrainzResponse;
import com.plugsurfung.musify.api.musicbrainz.client.MusicBrainzClient;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import reactor.core.scheduler.Schedulers;

@Service
public class MusicBrainzService {

    private final MusicBrainzClient webClient;

    public MusicBrainzService(MusicBrainzClient webClient) {
        this.webClient = webClient;
    }

    @RateLimiter(name = "musicBrainzRateLimiter")
    public Publisher<MusicBrainzResponse> fetchMusicBrainzArtist(String mbid) {
        return webClient.fetchMusifyArtist(mbid).subscribeOn(Schedulers.immediate());
    }
}
