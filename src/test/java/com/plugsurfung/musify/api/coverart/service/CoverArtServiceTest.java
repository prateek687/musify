package com.plugsurfung.musify.api.coverart.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plugsurfung.musify.api.FileReaderUtil;
import com.plugsurfung.musify.api.coverart.client.CoverArtClient;
import com.plugsurfung.musify.api.model.ArtistAlbum;
import com.plugsurfung.musify.api.model.CoverArtImageResponse;
import com.plugsurfung.musify.api.model.ReleaseGroup;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CoverArtServiceTest {

    private static final String MBID = "f27ec8db-af05-4f36-916e-3d57f91ecf5e";
    private static final String SUCCESS_COVER_ART_RESPONSE = FileReaderUtil.responseAsStringFromFile("/coverartresponse.json");
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final CoverArtClient coverArtClient = mock(CoverArtClient.class);

    @Test
    void testFetchCoverArtImage() throws JsonProcessingException {
        CoverArtImageResponse coverArtImageResponse = OBJECT_MAPPER.readValue(SUCCESS_COVER_ART_RESPONSE, CoverArtImageResponse.class);

        when(coverArtClient.fetchCoverArtImage(MBID)).thenReturn(Mono.just(coverArtImageResponse));

        CoverArtService coverArtService = new CoverArtService(coverArtClient);

        Publisher<ArtistAlbum> coverArtImagePublisher = coverArtService.fetchCoverArt(new ReleaseGroup(MBID, "testTitle", "album"));

        StepVerifier.create(coverArtImagePublisher)
                .expectNextMatches(response -> response != null && response.id().equals(MBID) && response.imageUrl() != null)
                .verifyComplete();
    }
}
