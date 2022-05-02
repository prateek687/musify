package com.plugsurfung.musify.api.musicbrainz.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plugsurfung.musify.api.FileReaderUtil;
import com.plugsurfung.musify.api.model.MusicBrainzResponse;
import com.plugsurfung.musify.api.musicbrainz.client.MusicBrainzClient;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MusicBrainzServiceTest {

    private static final String MBID = "f27ec8db-af05-4f36-916e-3d57f91ecf5e";
    private static final String MBID_NOT_FOUND_ERROR = "Not Found";
    private static final String INVALID_MBID_ERROR = "Invalid mbid.";
    private static final String SUCCESS_MUSIC_BRAINZ_RESPONSE = FileReaderUtil.responseAsStringFromFile("/musicbrainzresponse.json");
    private static final String NAME = "Michael Jackson";
    private static final String GENDER = "Male";
    private static final String COUNTRY = "US";
    private static final String DISAMBIGUATION = "“King of Pop”";
    private static final String ALBUM_TITLE = "Got to Be There";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final MusicBrainzClient musicBrainzClient = mock(MusicBrainzClient.class);

    @Test
    void testFetchMusicBrainzArtist() throws JsonProcessingException {
        MusicBrainzResponse musicBrainzResponse =
                OBJECT_MAPPER.readValue(SUCCESS_MUSIC_BRAINZ_RESPONSE, MusicBrainzResponse.class);

        when(musicBrainzClient.fetchMusifyArtist(MBID)).thenReturn(Mono.just(musicBrainzResponse));

        MusicBrainzService musicBrainzService = new MusicBrainzService(musicBrainzClient);

        Publisher<MusicBrainzResponse> musicBrainzResponsePublisher = musicBrainzService.fetchMusicBrainzArtist(MBID);

        StepVerifier.create(musicBrainzResponsePublisher)
                .expectNextMatches(
                        response ->
                                response != null
                                        && response.id().equals(MBID)
                                        && response.name().equals(NAME)
                                        && response.gender().equals(GENDER)
                                        && response.country().equals(COUNTRY)
                                        && response.disambiguation().equals(DISAMBIGUATION)
                                        && response.releaseGroups().get(0).title().equals(ALBUM_TITLE))
                .verifyComplete();
    }

    @Test
    void testFetchMusicBrainzArtist_NotFound() throws JsonProcessingException {
        MusicBrainzResponse musicBrainzResponse =
                OBJECT_MAPPER.readValue("""
                        {
                          "error": "Not Found",
                          "help": "For usage, please see: https://musicbrainz.org/development/mmd"
                        }
                        """, MusicBrainzResponse.class);

        when(musicBrainzClient.fetchMusifyArtist(MBID)).thenReturn(Mono.just(musicBrainzResponse));

        MusicBrainzService musicBrainzService = new MusicBrainzService(musicBrainzClient);

        Publisher<MusicBrainzResponse> musicBrainzResponsePublisher = musicBrainzService.fetchMusicBrainzArtist(MBID);

        StepVerifier.create(musicBrainzResponsePublisher)
                .expectNextMatches(response -> response != null && response.error().equals(MBID_NOT_FOUND_ERROR))
                .verifyComplete();
    }

    @Test
    void testFetchMusicBrainzArtist_InvalidMBID() throws JsonProcessingException {
        MusicBrainzResponse musicBrainzResponse =
                OBJECT_MAPPER.readValue("""
                        {
                          "help": "For usage, please see: https://musicbrainz.org/development/mmd",
                          "error": "Invalid mbid."
                        }
                        """, MusicBrainzResponse.class);

        when(musicBrainzClient.fetchMusifyArtist(MBID)).thenReturn(Mono.just(musicBrainzResponse));

        MusicBrainzService musicBrainzService = new MusicBrainzService(musicBrainzClient);

        Publisher<MusicBrainzResponse> musicBrainzResponsePublisher = musicBrainzService.fetchMusicBrainzArtist(MBID);

        StepVerifier.create(musicBrainzResponsePublisher)
                .expectNextMatches(response -> response != null && response.error().equals(INVALID_MBID_ERROR))
                .verifyComplete();
    }
}
