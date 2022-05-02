package com.plugsurfung.musify.api.artist.service;

import com.plugsurfung.musify.api.coverart.service.CoverArtService;
import com.plugsurfung.musify.api.model.ArtistDetailsResponse;
import com.plugsurfung.musify.api.model.MusicBrainzResponse;
import com.plugsurfung.musify.api.musicbrainz.service.MusicBrainzService;
import com.plugsurfung.musify.api.wikidata.service.WikiDataService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static reactor.core.scheduler.Schedulers.boundedElastic;

@Service
public class ArtistService {

    private final MusicBrainzService musicBrainzService;
    private final CoverArtService coverArtService;
    private final WikiDataService wikiDataService;

    public ArtistService(MusicBrainzService musicBrainzService,
                         CoverArtService coverArtService,
                         WikiDataService wikiDataService) {
        this.musicBrainzService = musicBrainzService;
        this.coverArtService = coverArtService;
        this.wikiDataService = wikiDataService;
    }

    @TimeLimiter(name="artistDetailsTimeLimiter")
    @CircuitBreaker(name="artistDetailsCB")
    public Mono<ArtistDetailsResponse> getArtistDetails(String mbid) {

        var musicBrainzResponsePublisher = musicBrainzService.fetchMusicBrainzArtist(mbid);

        return Mono.from(musicBrainzResponsePublisher).publishOn(boundedElastic()).flatMap(musicBrainzResponse -> {
            var albumsMono = coverArtService.fetchCoverArtsForAlbums(musicBrainzResponse.releaseGroups());
            var descriptionMono = Mono.from(wikiDataService.fetchWikipageTitleFromWikidata(extractWikiDataResourceId(musicBrainzResponse)));

            return albumsMono.zipWith(descriptionMono)
                    .map(tuple -> new ArtistDetailsResponse(musicBrainzResponse.id(),
                            musicBrainzResponse.name(), musicBrainzResponse.gender(),
                            musicBrainzResponse.country(), musicBrainzResponse.disambiguation(),
                            tuple.getT2(), tuple.getT1()));
        });
    }

    private String extractWikiDataResourceId(MusicBrainzResponse musicBrainzResponse) {
        return musicBrainzResponse.relations().stream()
                .filter(relation -> relation.type().equalsIgnoreCase("wikidata")).findFirst()
                .map(relation -> {
                    Matcher m = Pattern.compile(".*\\/wiki\\/([a-zA-Z0-9]+)(\\/?).*").matcher(relation.url().resource());
                    String wikidataTitle = "";
                    if (m.matches()) {
                        wikidataTitle = m.group(1);
                    }
                    return wikidataTitle;
                } ).orElse("");
    }
}
